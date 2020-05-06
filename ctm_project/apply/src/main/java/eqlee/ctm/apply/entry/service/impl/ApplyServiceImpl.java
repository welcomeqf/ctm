package eqlee.ctm.apply.entry.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.CityJwtBo;
import com.yq.jwt.entity.PrivilegeMenuQuery;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.DateUtil;
import com.yq.utils.IdGenerator;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.entry.dao.ApplyMapper;
import eqlee.ctm.apply.entry.entity.Apply;
import eqlee.ctm.apply.entry.entity.bo.*;
import eqlee.ctm.apply.entry.entity.query.*;
import eqlee.ctm.apply.entry.entity.vo.*;
import eqlee.ctm.apply.entry.service.IApplyService;
import eqlee.ctm.apply.entry.service.IExamineService;
import eqlee.ctm.apply.entry.vilidata.HttpUtils;
import eqlee.ctm.apply.line.entity.Line;
import eqlee.ctm.apply.line.service.ILineService;
import eqlee.ctm.apply.message.service.IMessageService;
import eqlee.ctm.apply.price.service.IPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;


/**
 * @Author qf
 * @Date 2019/9/17
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ApplyServiceImpl extends ServiceImpl<ApplyMapper, Apply> implements IApplyService {

    @Autowired
    private ILineService lineService;

    @Autowired
    private IPriceService priceService;

    @Autowired
    private IExamineService examineService;

    @Autowired
    private IMessageService messageService;

//    @Autowired
//    private  ApplyMapper applyMapper;

    @Autowired
    private HttpUtils httpUtils;

    private final String MONTH_PAY = "月结";

    private final String NOW_PAY = "现结";

    private final String AGENT_PAY = "面收";

    private final String APPLY_EXA = "报名审核";

    @Autowired
    private LocalUser localUser;

    @Override
    public ApplyPayResultQuery insertApply(ApplyVo applyVo) {
        LocalDate now = LocalDate.now();
        LocalDate outDate = DateUtil.parseDate(applyVo.getOutDate());
        long until = now.until(outDate, ChronoUnit.DAYS);

        if (until <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"亲~请报名今天之后的旅行！");
        }

        if (applyVo.getChildNumber() + applyVo.getOldNumber() + applyVo.getBabyNumber() + applyVo.getAdultNumber() <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "报名人数不能为0");
        }


        Line line = lineService.queryLineByName(applyVo.getLineName());
        IdGenerator idGenerator = new IdGenerator();
        LocalDate localDate = DateUtil.parseDate(applyVo.getOutDate());

        //查询报名表人数是否达到最大
        LambdaQueryWrapper<Apply> wrapper = new LambdaQueryWrapper<Apply>()
                .eq(Apply::getOutDate,localDate)
                .eq(Apply::getLineId,line.getId());
        List<Apply> list = baseMapper.selectList(wrapper);
        Integer number = 0;
        for (Apply apply : list) {
            number = number + apply.getAllNumber();
        }
        Integer allNumber = number + applyVo.getChildNumber() + applyVo.getOldNumber() + applyVo.getBabyNumber() + applyVo.getAdultNumber();
        if (allNumber > line.getMaxNumber()) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该天该线路人数名额不够");
        }

        //查询该天的价格情况
//        Price price = priceService.queryPrice(localDate,applyVo.getLineName());
//
//        if (price == null) {
//            throw new ApplicationException(CodeType.SERVICE_ERROR,"该天还未开放");
//        }
//
//        if (!price.getLineId().equals(line.getId())) {
//            throw new ApplicationException(CodeType.SERVICE_ERROR,"该线路该天还未设置价格");
//        }

        //算出总价格
        Double AdultPrice = applyVo.getAdultPrice()*applyVo.getAdultNumber();
        Double BabyPrice = applyVo.getBabyPrice()*applyVo.getBabyNumber();
        Double OldPrice = applyVo.getOldPrice()*applyVo.getOldNumber();
        Double ChildPrice = applyVo.getChildPrice()*applyVo.getChildNumber();
        Double AllPrice = AdultPrice + BabyPrice + OldPrice + ChildPrice;
        //算出总人数
        Integer AllNumber = applyVo.getAdultNumber() + applyVo.getChildNumber() + applyVo.getOldNumber() + applyVo.getBabyNumber();
        //生成报名单号
        String orderCode = idGenerator.getShortNo();

        UserLoginQuery user = localUser.getUser("用户信息");

        if ("运营人员".equals(user.getRoleName()) || "管理员".equals(user.getRoleName()) || "超级管理员".equals(user.getRoleName())) {
           //运营代录
           if (applyVo.getCompanyUserId() == null) {
              throw new ApplicationException(CodeType.SERVICE_ERROR, "代录请选择替录人员.");
           }
           //装配同行id
           //先查询该同行信息
           //-------------------
           UserQuery userById = null;
           try {
              String users = httpUtils.queryUserInfo(applyVo.getCompanyUserId());
              userById = JSONObject.parseObject (users,UserQuery.class);
           } catch (Exception e) {
              e.printStackTrace();
           }

           //同行代表名字
           if (userById != null) {
              applyVo.setCompanyUser(userById.getCname());
              applyVo.setCompanyTel(userById.getCompanyTel());
           }

           applyVo.setCompanyNameId(applyVo.getCompanyUserId());
           applyVo.setCName(user.getCname());
           applyVo.setUpdateUserId(user.getId());
        } else {
           //同行报名
           applyVo.setCompanyNameId(user.getCompanyId());
           applyVo.setCName(user.getCname());
           applyVo.setCompanyTel(user.getTel());
        }

        Company company = baseMapper.queryCompanyById(applyVo.getCompanyNameId());

        if (company == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "您的公司还未注册.");
        }

        //装配实体类
        Apply apply = new Apply();
       Long id = idGenerator.getNumberId();

       if (applyVo.getUpOrInsert() == 1) {
          //修改
          apply.setApplyNo(applyVo.getApplyNo());
          apply.setCreateUserId(applyVo.getCreateUserId());
          apply.setId(applyVo.getApplyId());

       } else {
          apply.setApplyNo(orderCode);
          apply.setCreateUserId(user.getId());
          apply.setId(id);
       }

       if ("运营人员".equals(user.getRoleName()) || "管理员".equals(user.getRoleName()) || "超级管理员".equals(user.getRoleName())) {

          //运营人员代录
          apply.setAdultNumber(applyVo.getAdultNumber());
          apply.setBabyNumber(applyVo.getBabyNumber());
          apply.setChildNumber(applyVo.getChildNumber());
          apply.setCompanyTel(applyVo.getCompanyTel());
          apply.setOldNumber(applyVo.getOldNumber());
          apply.setAllNumber(AllNumber);
          apply.setAllPrice(AllPrice);
          apply.setCompanyName(company.getCompanyName());
          apply.setCompanyUser(applyVo.getCompanyUser());
          apply.setContactName(applyVo.getContactName());
          apply.setContactTel(applyVo.getContactTel());
          apply.setPlace(applyVo.getPlace());
          apply.setRegion(line.getRegion());
          apply.setLineId(line.getId());
          apply.setCity(line.getCity());
          apply.setOutDate(localDate);
          apply.setUpdateUserId(applyVo.getUpdateUserId());
          apply.setCName(applyVo.getCName());
          apply.setMarketAllPrice(applyVo.getMarketAllPrice());
          apply.setMsPrice(applyVo.getMsPrice());
          apply.setApplyRemark(applyVo.getApplyRemark());

          apply.setCompanyId(applyVo.getCompanyNameId());
          apply.setType(applyVo.getType());

        } else {

          apply.setAdultNumber(applyVo.getAdultNumber());
          apply.setBabyNumber(applyVo.getBabyNumber());
          apply.setChildNumber(applyVo.getChildNumber());
          apply.setOldNumber(applyVo.getOldNumber());
          apply.setAllNumber(AllNumber);
          apply.setAllPrice(AllPrice);
          apply.setCompanyName(company.getCompanyName());
          apply.setCompanyUser(user.getCname());
          apply.setCompanyTel(applyVo.getCompanyTel());
          apply.setContactName(applyVo.getContactName());
          apply.setContactTel(applyVo.getContactTel());
          apply.setPlace(applyVo.getPlace());
          apply.setRegion(line.getRegion());
          apply.setLineId(line.getId());
          apply.setCity(line.getCity());
          apply.setOutDate(localDate);
          apply.setUpdateUserId(user.getId());
          apply.setCName(applyVo.getCName());
          apply.setMarketAllPrice(applyVo.getMarketAllPrice());
          apply.setMsPrice(applyVo.getMsPrice());
          apply.setApplyRemark(applyVo.getApplyRemark());

          apply.setCompanyId(applyVo.getCompanyNameId());
          apply.setCompanyTel(user.getTel());
          apply.setType(applyVo.getType());
        }

       //如果是包团
       if (applyVo.getType() == 2) {
          apply.setApplyPic(applyVo.getApplyPic());
       }


        //设置过期时间
        apply.setExpreDate(30);

        if (MONTH_PAY.equals(applyVo.getPayType())) {
            apply.setPayType(1);
            apply.setIsPayment(true);

           //如果是月结，判断月结金额是否达标
           //获取当前月份

           //查询当前月使用的金额
           List<Apply> applies = baseMapper.queryAllPriceToApply(applyVo.getCreateUserId());

           //算出本月已使用的金额
           Double sxAllPrice = 0.0;
           for (Apply apply1 : applies) {
              if (apply1.getAllPrice() != null) {
                 sxAllPrice += apply1.getAllPrice();
              }
           }
           //已使用的月结金额与额度比较
           if (applyVo.getSxPrice() == null) {
              applyVo.setSxPrice(0.0);
           }

           Double faPrice = applyVo.getSxPrice() - (sxAllPrice + AllPrice);

           Double syPrice = applyVo.getSxPrice() - sxAllPrice;

           if (faPrice < 0) {
              throw new ApplicationException(CodeType.SERVICE_ERROR, "您的额度不足，剩余额度:" + syPrice);
           }
        }
        if (NOW_PAY.equals(applyVo.getPayType())) {
            apply.setPayType(0);
        }
        if (AGENT_PAY.equals(applyVo.getPayType())) {

           //如果是面收   则判断面收金额是否为空
           if (applyVo.getMsPrice() == null) {
              throw new ApplicationException(CodeType.SERVICE_ERROR, "请输入面收金额");
           }

            apply.setPayType(2);
            apply.setIsPayment(true);

            //
           //获取当前月份

           //查询当前月使用的金额
           List<Apply> applies = baseMapper.queryAllPriceToApply(applyVo.getCreateUserId());

           //算出本月已使用的金额
           Double sxAllPrice = 0.0;
           for (Apply apply1 : applies) {
              if (apply1.getAllPrice() != null) {
                 sxAllPrice += apply1.getAllPrice();
              }
           }
           //已使用的月结金额与额度比较
           if (applyVo.getSxPrice() == null) {
              applyVo.setSxPrice(0.0);
           }

           Double faPrice = applyVo.getSxPrice() - (sxAllPrice + AllPrice);

           Double syPrice = applyVo.getSxPrice() - sxAllPrice;

           if (faPrice < 0) {
              throw new ApplicationException(CodeType.SERVICE_ERROR, "您的额度不足，剩余额度:" + syPrice);
           }
        }

        if (!MONTH_PAY.equals(applyVo.getPayType()) && !NOW_PAY.equals(applyVo.getPayType()) && !AGENT_PAY.equals(applyVo.getPayType())) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"您输入支付类型有误，请重新输入");
        }

       //单价
       apply.setAdultPrice(applyVo.getAdultPrice());
       apply.setBabyPrice(applyVo.getBabyPrice());
       apply.setChildPrice(applyVo.getChildPrice());
       apply.setOldPrice(applyVo.getOldPrice());

        int insert = baseMapper.insert(apply);

        ExamineAddVo vo = new ExamineAddVo();
        vo.setExamineType("0");
        vo.setApplyId(id);

        examineService.insertExamine(vo);

        if (insert <= 0) {
            log.error("insert apply fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"报名失败");
        }

        //查询所有管理员的id集合

//        List<Long> longList = new ArrayList<>();
//        List<UserAdminBo> idList = (List<UserAdminBo>) httpUtils.queryAllAdminInfo();
//
//        if (idList.size() == 0) {
//            throw new ApplicationException(CodeType.SERVICE_ERROR, "请将管理员的角色名设置为运营人员");
//        }
//
//       for (int i = 0; i <= idList.size()-1; i++) {
//          UserAdminBo bo = JSONObject.parseObject(String.valueOf(idList.get(i)),UserAdminBo.class);
//          longList.add(bo.getAdminId());
//       }
//
//        //批量增加所有运营审核的报名消息提醒
//        MsgAddVo msgVo = new MsgAddVo();
//        msgVo.setCreateId(user.getId());
//        msgVo.setMsgType(3);
//        msgVo.setMsg(NettyType.MSG_EXA.getMsg());
//        msgVo.setToId(longList);
////
//       messageService.addAllMsg(msgVo);

        //组装ApplyPayResultQuery 返回数据
        ApplyPayResultQuery query = new ApplyPayResultQuery();

        //返回报名单号
        query.setApplyNo(orderCode);

        //装配auto
        //去数据库查询是否有该用户的openId
        ApplyOpenIdVo idVo = baseMapper.queryPayInfo(user.getId());
        if (idVo == null) {
            query.setAuto(false);
        } else {
            query.setAuto(true);
        }
        return query;
    }

   /**
    * 补录
    * @param applyVo
    * @return
    */
   @Override
   public ApplyPayResultQuery insertWithApply(ApplyVo applyVo) {
      LocalDate now = LocalDate.now();
      LocalDate outDate = DateUtil.parseDate(applyVo.getOutDate());
      long until = now.until(outDate, ChronoUnit.DAYS);

      if (until < -2) {
         throw new ApplicationException(CodeType.SERVICE_ERROR,"已经过了补录的时间了");
      }

      if (applyVo.getChildNumber() + applyVo.getOldNumber() + applyVo.getBabyNumber() + applyVo.getAdultNumber() <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "报名人数不能为0");
      }


      Line line = lineService.queryLineByName(applyVo.getLineName());
      IdGenerator idGenerator = new IdGenerator();
      LocalDate localDate = DateUtil.parseDate(applyVo.getOutDate());

      //查询报名表人数是否达到最大
      LambdaQueryWrapper<Apply> wrapper = new LambdaQueryWrapper<Apply>()
            .eq(Apply::getOutDate,localDate)
            .eq(Apply::getLineId,line.getId());
      List<Apply> list = baseMapper.selectList(wrapper);
      Integer number = 0;
      for (Apply apply : list) {
         number = number + apply.getAllNumber();
      }
      Integer allNumber = number + applyVo.getChildNumber() + applyVo.getOldNumber() + applyVo.getBabyNumber() + applyVo.getAdultNumber();
      if (allNumber > line.getMaxNumber()) {
         throw new ApplicationException(CodeType.SERVICE_ERROR,"该天该线路人数名额不够");
      }

      //查询该天的价格情况
//        Price price = priceService.queryPrice(localDate,applyVo.getLineName());
//
//        if (price == null) {
//            throw new ApplicationException(CodeType.SERVICE_ERROR,"该天还未开放");
//        }
//
//        if (!price.getLineId().equals(line.getId())) {
//            throw new ApplicationException(CodeType.SERVICE_ERROR,"该线路该天还未设置价格");
//        }

      //算出总价格
      Double AdultPrice = applyVo.getAdultPrice()*applyVo.getAdultNumber();
      Double BabyPrice = applyVo.getBabyPrice()*applyVo.getBabyNumber();
      Double OldPrice = applyVo.getOldPrice()*applyVo.getOldNumber();
      Double ChildPrice = applyVo.getChildPrice()*applyVo.getChildNumber();
      Double AllPrice = AdultPrice + BabyPrice + OldPrice + ChildPrice;
      //算出总人数
      Integer AllNumber = applyVo.getAdultNumber() + applyVo.getChildNumber() + applyVo.getOldNumber() + applyVo.getBabyNumber();
      //生成报名单号
      String orderCode = idGenerator.getShortNo();

      UserLoginQuery user = localUser.getUser("用户信息");

      if ("运营人员".equals(user.getRoleName()) || "管理员".equals(user.getRoleName()) || "超级管理员".equals(user.getRoleName())) {

         //运营代录
         if (applyVo.getCompanyUserId() == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "代录请选择替录人员.");
         }
         //装配同行id
         //先查询该同行信息
         //-------------------
         UserQuery userById = null;
         try {
            String users = httpUtils.queryUserInfo(applyVo.getCompanyUserId());
            userById = JSONObject.parseObject (users,UserQuery.class);
         } catch (Exception e) {
            e.printStackTrace();
         }

         applyVo.setCompanyNameId(applyVo.getCompanyUserId());
         if (userById != null) {
            applyVo.setCompanyUser(userById.getCname());
            applyVo.setCompanyTel(userById.getCompanyTel());
         }
         applyVo.setUpdateUserId(user.getId());
         applyVo.setCName(user.getCname());

      } else {

         applyVo.setCompanyNameId(user.getCompanyId());
         applyVo.setCName(user.getCname());
         applyVo.setCompanyTel(user.getTel());
      }

      Company company = baseMapper.queryCompanyById(applyVo.getCompanyNameId());

      if (company == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "您的公司还未注册.");
      }

      //装配实体类
      Apply apply = new Apply();

      Long id = idGenerator.getNumberId();

      if (applyVo.getUpOrInsert() == 1) {
         //修改
         apply.setApplyNo(applyVo.getApplyNo());
         apply.setCreateUserId(applyVo.getCreateUserId());
         apply.setId(applyVo.getApplyId());

      } else {
         apply.setApplyNo(orderCode);
         apply.setCreateUserId(user.getId());
         apply.setId(id);
      }

      if ("运营人员".equals(user.getRoleName()) || "管理员".equals(user.getRoleName()) || "超级管理员".equals(user.getRoleName()))  {

         //运营人员代录
         apply.setAdultNumber(applyVo.getAdultNumber());
         apply.setBabyNumber(applyVo.getBabyNumber());
         apply.setChildNumber(applyVo.getChildNumber());
         apply.setOldNumber(applyVo.getOldNumber());
         apply.setAllNumber(AllNumber);
         apply.setAllPrice(AllPrice);
         apply.setCompanyName(company.getCompanyName());
         apply.setCompanyUser(applyVo.getCompanyUser());
         apply.setCompanyTel(applyVo.getCompanyTel());
         apply.setContactName(applyVo.getContactName());
         apply.setContactTel(applyVo.getContactTel());
         apply.setPlace(applyVo.getPlace());
         apply.setRegion(line.getRegion());
         apply.setLineId(line.getId());
         apply.setCity(line.getCity());
         apply.setOutDate(localDate);
         apply.setUpdateUserId(applyVo.getUpdateUserId());
         apply.setCName(applyVo.getCName());
         apply.setMarketAllPrice(applyVo.getMarketAllPrice());
         apply.setMsPrice(applyVo.getMsPrice());
         apply.setApplyRemark(applyVo.getApplyRemark());

         apply.setCompanyId(applyVo.getCompanyNameId());
         apply.setType(applyVo.getType());

      } else {

         apply.setAdultNumber(applyVo.getAdultNumber());
         apply.setBabyNumber(applyVo.getBabyNumber());
         apply.setChildNumber(applyVo.getChildNumber());
         apply.setOldNumber(applyVo.getOldNumber());
         apply.setAllNumber(AllNumber);
         apply.setAllPrice(AllPrice);
         apply.setCompanyName(company.getCompanyName());
         apply.setCompanyTel(applyVo.getCompanyTel());
         apply.setCompanyUser(user.getCname());
         apply.setContactName(applyVo.getContactName());
         apply.setContactTel(applyVo.getContactTel());
         apply.setPlace(applyVo.getPlace());
         apply.setRegion(line.getRegion());
         apply.setLineId(line.getId());
         apply.setCity(line.getCity());
         apply.setOutDate(localDate);
         apply.setUpdateUserId(user.getId());
         apply.setCName(applyVo.getCName());
         apply.setMarketAllPrice(applyVo.getMarketAllPrice());
         apply.setMsPrice(applyVo.getMsPrice());
         apply.setApplyRemark(applyVo.getApplyRemark());

         apply.setCompanyId(applyVo.getCompanyNameId());
         apply.setCompanyTel(user.getTel());
         apply.setType(applyVo.getType());
      }


      //设置过期时间
      apply.setExpreDate(30);

      if (MONTH_PAY.equals(applyVo.getPayType())) {
         apply.setPayType(1);
         apply.setIsPayment(true);

         //如果是月结，判断月结金额是否达标

         //查询当前月使用的金额
         List<Apply> applies = baseMapper.queryAllPriceToApply(applyVo.getCreateUserId());

         //算出本月已使用的金额
         Double sxAllPrice = 0.0;
         for (Apply apply1 : applies) {
            if (apply1.getAllPrice() != null) {
               sxAllPrice += apply1.getAllPrice();
            }
         }
         //已使用的月结金额与额度比较
         if (applyVo.getSxPrice() == null) {
            applyVo.setSxPrice(0.0);
         }

         Double faPrice = applyVo.getSxPrice() - (sxAllPrice + AllPrice);

         Double syPrice = applyVo.getSxPrice() - sxAllPrice;

         if (faPrice < 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "您的额度不足，剩余额度:" + syPrice);
         }
      }
      if (NOW_PAY.equals(applyVo.getPayType())) {
         apply.setPayType(0);
      }
      if (AGENT_PAY.equals(applyVo.getPayType())) {

         //如果是面收   则判断面收金额是否为空
         if (applyVo.getMsPrice() == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "请输入面收金额");
         }

         apply.setPayType(2);
         apply.setIsPayment(true);

         //
         //获取当前月份

         //查询当前月使用的金额
         List<Apply> applies = baseMapper.queryAllPriceToApply(applyVo.getCreateUserId());

         //算出本月已使用的金额
         Double sxAllPrice = 0.0;
         for (Apply apply1 : applies) {
            if (apply1.getAllPrice() != null) {
               sxAllPrice += apply1.getAllPrice();
            }
         }
         //已使用的月结金额与额度比较
         if (applyVo.getSxPrice() == null) {
            applyVo.setSxPrice(0.0);
         }

         Double faPrice = applyVo.getSxPrice() - (sxAllPrice + AllPrice);

         Double syPrice = applyVo.getSxPrice() - sxAllPrice;

         if (faPrice < 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "您的额度不足，剩余额度:" + syPrice);
         }
      }

      if (!MONTH_PAY.equals(applyVo.getPayType()) && !NOW_PAY.equals(applyVo.getPayType()) && !AGENT_PAY.equals(applyVo.getPayType())) {
         throw new ApplicationException(CodeType.SERVICE_ERROR,"您输入支付类型有误，请重新输入");
      }

      //单价
      apply.setAdultPrice(applyVo.getAdultPrice());
      apply.setBabyPrice(applyVo.getBabyPrice());
      apply.setChildPrice(applyVo.getChildPrice());
      apply.setOldPrice(applyVo.getOldPrice());

      int insert = baseMapper.insert(apply);

      ExamineAddVo vo = new ExamineAddVo();
      vo.setExamineType("0");
      vo.setApplyId(id);

      examineService.insertExamine(vo);

      if (insert <= 0) {
         log.error("insert apply fail.");
         throw new ApplicationException(CodeType.SERVICE_ERROR,"报名失败");
      }

      //查询所有管理员的id集合

//      List<Long> longList = new ArrayList<>();
//      List<UserAdminBo> idList = (List<UserAdminBo>) httpUtils.queryAllAdminInfo();
//
//      if (idList.size() == 0) {
//         throw new ApplicationException(CodeType.SERVICE_ERROR, "请将管理员的角色名设置为运营人员");
//      }
//
//      for (int i = 0; i <= idList.size()-1; i++) {
//         UserAdminBo bo = JSONObject.parseObject(String.valueOf(idList.get(i)),UserAdminBo.class);
//         longList.add(bo.getAdminId());
//      }
//
//      //批量增加所有运营审核的报名消息提醒
//      MsgAddVo msgVo = new MsgAddVo();
//      msgVo.setCreateId(user.getId());
//      msgVo.setMsgType(3);
//      msgVo.setMsg(NettyType.MSG_EXA.getMsg());
//      msgVo.setToId(longList);
////
//      messageService.addAllMsg(msgVo);

      //组装ApplyPayResultQuery 返回数据
      ApplyPayResultQuery query = new ApplyPayResultQuery();

      //返回报名单号
      query.setApplyNo(orderCode);

      //装配auto
      //去数据库查询是否有该用户的openId
      ApplyOpenIdVo idVo = baseMapper.queryPayInfo(user.getId());
      if (idVo == null) {
         query.setAuto(false);
      } else {
         query.setAuto(true);
      }
      return query;
   }

   /**
    * 分页展示报名记录
    * <a>如果只有出发日期，只对出发日期进行条件查询</a>
    * <b>如果只有线路名，区域任意一种，对线路名，或区域进行模糊查询</b>
    * <c>默认参数都为空，对所有分页</c>
    * <d>对出发日期，线路名或者区域查询</d>
    * @param page
    * @param OutDate
    * @param LineNameOrRegion
    * @return
    */
   @Override
   public Page<ApplyQuery> listPage2Apply(Page<ApplyQuery> page, String OutDate, String LineNameOrRegion, String lineName) {

      LocalDate outTime = null;

      if (StringUtils.isNotBlank(OutDate)) {
         outTime = DateUtil.parseDate(OutDate);
      }

      if (StringUtils.isBlank(LineNameOrRegion)) {
         LineNameOrRegion = null;
      }

      if (StringUtils.isBlank(lineName)) {
         lineName = null;
      }


      return baseMapper.queryApplyInfo (page,LineNameOrRegion,outTime,lineName);
   }

   /**
    * 删除报名表
    * @param id
    */
   @Override
   public void deleteApply(Long id) {
      int i = baseMapper.deleteById(id);

      if (i <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "删除失败");
      }
   }

   /**
    * 分页查询已报名未审核的列表,可以根据出发时间模糊查询
    * 优化
    * @param page
    * @param outDate
    * @param lineName
    * @param type
    * @return
    */
   @Override
   public Page<ApplyDoExaQuery> listPageDo2Apply(Page<ApplyDoExaQuery> page, String outDate, Long lineName, Integer type, String applyDate, Integer exaStatus) {

      UserLoginQuery user = localUser.getUser("用户信息");

      LocalDate outTime = null;

      if (StringUtils.isNotBlank(outDate)) {
         outTime = DateUtil.parseDate(outDate);
      }


      LocalDateTime start = null;
      LocalDateTime end = null;
      if (StringUtils.isNotBlank(applyDate)) {
         String startTime = applyDate + " 00:00:00";
         String endTime = applyDate + " 23:59:59";
         start = DateUtil.parseDateTime(startTime);
         end = DateUtil.parseDateTime(endTime);
      }


      //加城市   好奔溃
      List<String> list = new ArrayList<>();
      if (user.getCity().size() > 0) {
         for (CityJwtBo bo : user.getCity()) {
            list.add(bo.getCity());
         }
      }

      return baseMapper.queryAllExaInfo2 (page,outTime,lineName,type,start,end,exaStatus,list);

   }

    /**
     * 查询同一公司所有报名记录
     * 对时间进行条件查询
     * @param page
     * @param OutTime
     * @param CompanyName
     * @return
     */
    @Override
    public Page<ApplyCompanyQuery> listPageDoApply2Company(Page<ApplyCompanyQuery> page, String OutTime, String CompanyName) {
        if (StringUtils.isBlank(OutTime)) {
            //时间为空   查询全部
            return baseMapper.listPageDoApply2Company(page,CompanyName);
        }
        LocalDate localDate = DateUtil.parseDate(OutTime);
        return baseMapper.listPageDoApply2CompanyByTime(page,localDate,CompanyName);
    }

    /**
     * 取消报名表
     * @param Id
     */
    @Override
    public void cancelApply(Long Id) {
        Apply apply = new Apply();
        apply.setId(Id);
        apply.setIsCancel(true);
        apply.setStatu(3);
        LocalDateTime now = LocalDateTime.now();
        apply.setRemark(now + "取消");
        int result = baseMapper.updateById(apply);

        if (result <= 0) {
            log.error("cancel apply fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"取消报名表失败");
        }
    }

    /**
     * 修改审核状态
     * @param Id
     * @param Status
     */
    @Override
    public void updateExamineStatus(Long Id, Integer Status, Integer type) {
        Apply apply = new Apply();
        apply.setId(Id);
        apply.setStatu(Status);
        apply.setCancelInfo(type);
        int i = baseMapper.updateById(apply);

        if (i <= 0) {
            log.error("update examine status fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"报名表修改审核状态失败");
        }
    }


    /**
     * 查询报名表
     * @param list
     * @return
     */
    @Override
    public List<Apply> listApply(List<Long> list) {
        return baseMapper.selectBatchIds(list);
    }

    /**
     * 查询公司信息
     * @param Id
     * @return
     */
    @Override
    public Company queryCompany(Long Id) {
        return baseMapper.queryCompanyById(Id);
    }

    /**
     * 查询一条报名记录
     * @param Id
     * @return
     */
    @Override
    public ApplySeacherVo queryById(Long Id) {
        Apply apply = baseMapper.selectById(Id);

        //装配vo
        ApplySeacherVo vo = new ApplySeacherVo();
        vo.setAdultNumber(apply.getAdultNumber());
        vo.setAllNumber(apply.getAllNumber());
        vo.setAllPrice(apply.getAllPrice());
        vo.setApplyNo(apply.getApplyNo());
        vo.setBabyNumber(apply.getBabyNumber());
        vo.setChildNumber(apply.getChildNumber());
        vo.setCity(apply.getCity());
        vo.setCompanyName(apply.getCompanyName());
        vo.setCompanyUser(apply.getCompanyUser());
        vo.setContactName(apply.getContactName());
        vo.setContactTel(apply.getContactTel());
        vo.setCreateUserId(apply.getCreateUserId());
        vo.setId(apply.getId());
        vo.setIsCancel(apply.getIsCancel());
        vo.setIsPayment(apply.getIsPayment());
        vo.setLineId(apply.getLineId());
        vo.setOldNumber(apply.getOldNumber());
        vo.setPayType(apply.getPayType());
        vo.setPlace(apply.getPlace());
        vo.setProvince(apply.getProvince());
        vo.setRegion(apply.getRegion());
        vo.setRemark(apply.getRemark());
        vo.setStatu(apply.getStatu());
        vo.setUpdateUserId(apply.getUpdateUserId());
        vo.setIsSelect(apply.getIsSelect());

        //单价
         vo.setAdultPrice(apply.getAdultPrice());
         vo.setBabyPrice(apply.getBabyPrice());
         vo.setChildPrice(apply.getChildPrice());
         vo.setOldPrice(apply.getOldPrice());
         vo.setCompanyId(apply.getCompanyId());

        //出发日期
        vo.setOutDate(DateUtil.formatDate(apply.getOutDate()));

        vo.setCreateDate(DateUtil.formatDateTime(apply.getCreateDate()));
        vo.setUpdateDate(DateUtil.formatDateTime(apply.getUpdateDate()));

        //支付类型 0--微信支付  1--支付宝支付
       vo.setPayInfo(apply.getPayInfo());

       Line line = lineService.queryOneLine(apply.getLineId());
       vo.setLineName(line.getLineName());
       vo.setApplyRemark(apply.getApplyRemark());

       vo.setMsPrice(apply.getMsPrice());

       //包团详情文件
       vo.setApplyPic(apply.getApplyPic());

       return vo;
    }

    /**
     * 优化
     * 我的报名记录(同行)
     * @param page
     * @param lineName
     * @param outDate
     * @param applyTime
     * @param type
     * @param todayType
     * @return
     */
    @Override
    public Page<ApplyCompanyQuery> pageMeApply(Page<ApplyCompanyQuery> page, String lineName, String outDate, String applyTime, Integer type, Integer todayType,String roadName) {

        UserLoginQuery user = localUser.getUser("用户信息");

        if (StringUtils.isNotBlank(applyTime) && StringUtils.isNotBlank(outDate)) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "请勿同时选择两个时间");
        }

        LocalDate outTime = null;
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        if (StringUtils.isNotBlank(outDate)) {
            outTime = DateUtil.parseDate(outDate);
        }

        if (StringUtils.isNotBlank(applyTime)) {
            String start = applyTime + " 00:00:00";
            String end = applyTime + " 23:59:59";
            startDate = DateUtil.parseDateTime(start);
            endDate = DateUtil.parseDateTime(end);
        }

        if (StringUtils.isBlank(lineName)) {
           lineName = null;
        }

       if (StringUtils.isBlank(roadName)) {
          roadName = null;
       }

       //如果是子用户，则查询自己的报名记录
       //如果是用户，说明是同行代表(同行的管理人员),则查询公司全部的报名记录
       Long id = null;
       if (user.getStatus() == 1) {
          //等于1说明是子用户
          id = user.getId();
       }

       Long companyId = null;
       if (user.getStatus() == 0) {
          //等于0说明是同行代表
          companyId = user.getCompanyId();
       }

        if (todayType == null) {
           return baseMapper.allCompanyList (page,lineName,outTime,startDate,endDate,id,companyId,type,roadName);
        }

        //查询今天已报名
        if (todayType == 1) {
            LocalDate now = LocalDate.now();
            String nowTime = DateUtil.formatDate(now);
            //加上分秒
            String start = nowTime + " 00:00:00";
            String end = nowTime + " 23:59:59";
            LocalDateTime sartTime = DateUtil.parseDateTime(start);
            LocalDateTime endTime = DateUtil.parseDateTime(end);

            return baseMapper.listToDayApply(page,sartTime,endTime,user.getId());
        }

        //查询今天出行
        if (todayType == 2) {
            LocalDate now = LocalDate.now();
            return baseMapper.listToDayOut(page,now,user.getId());
        }else {
           throw new ApplicationException(CodeType.SERVICE_ERROR, "参数type或todayType有误");
        }
    }

   /**
    * 我的报名记录(运营人员)
    * 优化
    * @param page
    * @param lineName
    * @param outDate
    * @param applyTime
    * @param type
    * @param companyUserId
    * @param todayType
    * @return
    */
   @Override
   public Page<ApplyCompanyQuery> pageAdmin2Apply(Page<ApplyCompanyQuery> page, String lineName, String outDate, String applyTime, Integer type, Long companyUserId, Integer todayType, String roadName) {

      if (StringUtils.isNotBlank(applyTime) && StringUtils.isNotBlank(outDate)) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "请勿同时选择两个时间");
      }

      LocalDate outTime = null;
      LocalDateTime startDate = null;
      LocalDateTime endDate = null;

      if (StringUtils.isNotBlank(outDate)) {
         outTime = DateUtil.parseDate(outDate);
      }

      if (StringUtils.isNotBlank(applyTime)) {
         String start = applyTime + " 00:00:00";
         String end = applyTime + " 23:59:59";
         startDate = DateUtil.parseDateTime(start);
         endDate = DateUtil.parseDateTime(end);
      }

      if (StringUtils.isBlank(lineName)) {
         lineName = null;
      }

      if (StringUtils.isBlank(roadName)) {
         roadName = null;
      }

      if (todayType == null) {
         return baseMapper.allCompanyAdminList (page,lineName,outTime,startDate,endDate,companyUserId,type,roadName);
      }


      //查询今天已报名
      if (todayType == 1) {
         LocalDate now = LocalDate.now();
         String nowTime = DateUtil.formatDate(now);
         //加上分秒
         String start = nowTime + " 00:00:00";
         String end = nowTime + " 23:59:59";
         LocalDateTime sartTime = DateUtil.parseDateTime(start);
         LocalDateTime endTime = DateUtil.parseDateTime(end);

         return baseMapper.listToDayApply(page,sartTime,endTime,companyUserId);
      }

      //查询今天出行
      if (todayType == 2) {
         LocalDate now = LocalDate.now();
         return baseMapper.listToDayOut(page,now,companyUserId);
      } else {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "参数type有误");
      }
   }

    /**
     * 修改导游选人状态
     * @param id
     */
    @Override
    public void updateGuestStatus(Long id) {

        Apply apply = new Apply();
        apply.setId(id);
        apply.setIsSelect(true);
        int byId = baseMapper.updateById(apply);

        if (byId <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"提交失败");
        }

    }

    /**
     * 批量修改
     * @param list
     */
    @Override
    public void updateAllGuestStatus(List<ApplyGuideBo> list) {

        Integer integer = baseMapper.updateAllApply(list);

        if (integer <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改客人的状态失败");
        }
    }

   /**
    * 分页查询月结的信息
    * type==0 默认
    * type==1 未付款
    * type==2 已付款
    * @param page
    * @param type
    * @param outDate
    * @return
    */
   @Override
   public Page<ApplyMonthQuery> queryMonth2Apply(Page<ApplyMonthQuery> page, Integer type, String outDate) {

      LocalDate outTime = null;

      if (StringUtils.isNotBlank(outDate)) {
         outTime = DateUtil.parseDate(outDate);
      }
      return baseMapper.queryAllMonth (page,outTime,type);
   }

   /**
     * 修改付款状态
     * @param id
     */
    @Override
    public void updateMonthType(Long id) {
        Apply apply = new Apply();
        apply.setId(id);
        apply.setIsPayment(true);

        int byId = baseMapper.updateById(apply);

        if (byId <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改失败");
        }
    }

    /**
     * 根据订单号回收报名表
     * @param applyNo
     */
    @Override
    public void dopApply(String applyNo) {
        LambdaQueryWrapper<Apply> wrapper = new LambdaQueryWrapper<Apply>()
                .eq(Apply::getApplyNo,applyNo);

        Apply apply = new Apply();
        apply.setIsCancel(true);

        int update = baseMapper.update(apply, wrapper);

        if (update <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "回收订单失败");
        }


    }

    /**
     * 回收所有订单
     */
    @Override
    public void dopAllApply() {
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        //获取两小时之前的时间
        LocalDateTime time = now.minusMinutes(120);

        //查询需要回收的报名记录(未付款的最近半小时的订单)
        LambdaQueryWrapper<Apply> wrapper = new LambdaQueryWrapper<Apply>()
              .eq(Apply::getIsPayment,0)
              .le(Apply::getCreateDate,now)
              .ge(Apply::getCreateDate,time)
              .eq(Apply::getIsCancel,0);

        List<Apply> applies = baseMapper.selectList(wrapper);

        if (applies.size() == 0) {
            return;
        }

        //创建一个集合装需要回收的订单号
        List<ApplyScheQuery> list = new ArrayList<>();
        for (Apply apply : applies) {
            //获取过期时间和下单时间
            LocalDateTime createDate = apply.getCreateDate();
            Duration duration = Duration.between(createDate,now);
            //获取分钟数
            long minutes = duration.toMinutes();

            //如果获取的分钟数大于或等于过期时间，则回收
            if (minutes >= apply.getExpreDate()) {
                ApplyScheQuery query = new ApplyScheQuery();
                query.setApplyNo(apply.getApplyNo());
                list.add(query);
            }
        }

        if (list.size() == 0) {
            return;
        }

        //批量回收订单
        Integer integer = baseMapper.updateAllApplyStatus(list);

        if (integer <= 0) {
            log.error("定时任务回收订单失败");
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改失败");
        }
    }

   /**
    * 同行月结现结统计
    * @param page
    * @param time
    * @param type
    * @return
    */
   @Override
   public Page<ApplyResultCountQuery> pageResult2CountList(Page<ApplyResultCountQuery> page, String time, Integer type, Integer caiType) {

      UserLoginQuery user = localUser.getUser("用户信息");

      LocalDate start = null;
      LocalDate end = null;

      if (StringUtils.isNotBlank(time)) {
         String date = time + "-01-01";
         start = DateUtil.parseDate(date);

         //结束时间
         end = start.plusYears(1).minusDays(1);
      }

      Long id = null;
      if (user.getStatus() == 1) {
         id = user.getId();
      }

      Long companyId = null;
      if (user.getStatus() == 0) {
         companyId = user.getCompanyId();
      }

      return baseMapper.queryCompanyResultCount (page,start,end,type,caiType,id,companyId);
   }

   /**
    * 管理员统计现结月结金额
    * @param page
    * @param time
    * @param type
    * @param companyUserId
    * @return
    */
   @Override
   public Page<ApplyResultCountQuery> pageResultAdminCountList(Page<ApplyResultCountQuery> page, String time, Integer type, Integer caiType, Long companyUserId) {

      LocalDate start = null;
      LocalDate end = null;

      if (StringUtils.isNotBlank(time)) {
         String date = time + "-01-01";
         start = DateUtil.parseDate(date);

         //结束时间
         end = start.plusYears(1).minusDays(1);
      }


      return baseMapper.queryCompanyAdminResultCount (page,start,end,type,caiType,companyUserId);
   }

   /**
     * 查询待付款支付信息
     * @param applyNo
     * @return
     */
    @Override
    public ApplyPayResultQuery queryPayInfo(String applyNo) {
        //获取当前用户信息
        UserLoginQuery user = localUser.getUser("用户信息");

        LambdaQueryWrapper<Apply> wrapper = new LambdaQueryWrapper<Apply>()
              .eq(Apply::getApplyNo,applyNo);
        Apply apply = baseMapper.selectOne(wrapper);

        ApplyPayResultQuery query = new ApplyPayResultQuery();

        query.setApplyNo(applyNo);
        query.setExpreDate(apply.getExpreDate());

        //查询线路
        Line line = lineService.queryOneLine(apply.getLineId());

        String product = apply.getOutDate() + "在"+line.getLineName() +"报名消费";

        query.setProductName(product);
        String applyDate = DateUtil.formatDateTime(apply.getCreateDate());
        query.setApplyDate(applyDate);

        //装配auto
        //去数据库查询是否有该用户的openId
        ApplyOpenIdVo idVo = baseMapper.queryPayInfo(user.getId());
        if (idVo == null) {
            query.setAuto(false);
        } else {
            query.setAuto(true);
        }

        query.setAllPrice(apply.getAllPrice());
        return query;
    }


   /**
    * 统计查询
    * @return
    */
   @Override
   public ApplyCountBo queryApplyCount() {
      UserLoginQuery user = localUser.getUser("用户信息");

      ApplyCompanyBo bo = baseMapper.queryApplyCompanyInfo(user.getCompanyId());
      ApplyCountBo result = new ApplyCountBo();

      result.setCompanyName(bo.getCompanyName());
      result.setCompanyNo(bo.getCompanyNo());
      result.setEndTime(bo.getEndTime());
      result.setPicture(bo.getPicture());
      result.setSxPrice(bo.getSxPrice());

      //查询总金额
      ApplyBo applyBo = baseMapper.queryApplyCountInfo(user.getId());

      if (applyBo != null) {
         result.setAllNumber(applyBo.getAllNumber());
         result.setAllPrice(applyBo.getAllPrice());
         result.setMsPrice(applyBo.getMsPrice());
      } else {
         result.setAllNumber(0);
         result.setAllPrice(0.0);
         result.setMsPrice(0.0);
      }


      LambdaQueryWrapper<Apply> wrapper = new LambdaQueryWrapper<Apply>()
            .eq(Apply::getCreateUserId,user.getId())
            .eq(Apply::getSxType,0);

      List<Apply> applyList = baseMapper.selectList(wrapper);

      LocalDateTime startDate = LocalDateTime.now();
      for (Apply apply : applyList) {

         long until = startDate.until(apply.getCreateDate(), ChronoUnit.DAYS);

         if (until < 0) {
            startDate = apply.getCreateDate();
         }


      }

      result.setStartDate(DateUtil.formatDate(startDate));

      return result;
   }

   /**
    * 跟剧线路ID查询报名表
    * @param lineId
    * @return
    */
   @Override
   public List<Apply> queryApplyByLineId(Long lineId) {
      LambdaQueryWrapper<Apply> wrapper = new LambdaQueryWrapper<Apply>()
            .eq(Apply::getLineId,lineId);
      return baseMapper.selectList(wrapper);
   }


   /**
    * 修改取消状态
    * @param type
    * @param id
    */
   @Override
   public void updateApplyCancel(Integer type, Long id) {

      Apply apply = new Apply();
      apply.setCancelInfo(type);
      apply.setId(id);

      int updateById = baseMapper.updateById(apply);

      if (updateById <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "取消错误");
      }
   }

   /**
    * 查询支付的一些信息
    * @param applyNo
    * @return
    */
   @Override
   public ApplyDoExaInfo queryApplyDoExaInfo(String applyNo) {
      return baseMapper.queryApplyDoExaInfo (applyNo);
   }

   /**
    * 续月结的金额
    */
   @Override
   public void updateSxPriceStatus(String s) {
      //日期
      String startDate = s.substring(0,10);
      //公司名
      String  companyName = s.substring(10);

      //每月开始的第一天
      LocalDate start = DateUtil.parseDate(startDate);
      LocalDate date = start.plusMonths(1);
      //当前月的最后一天
      LocalDate endTime = date.minusDays(1);


      LambdaQueryWrapper<Apply> wrapper = new LambdaQueryWrapper<Apply>()
            .eq(Apply::getIsCancel,0)
            .eq(Apply::getSxType,0)
            .eq(Apply::getIsSelect,1)
            .eq(Apply::getCompanyName,companyName)
            .ge(Apply::getOutDate,start)
            .le(Apply::getOutDate,endTime);

      Apply apply = new Apply();
      apply.setSxType(1);

      int update = baseMapper.update(apply, wrapper);

      if (update <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "修改状态失败");
      }

   }


   /**
    * 查询auto
    * @param id
    * @return
    */
   @Override
   public ApplyOpenIdVo queryAuto(Long id) {
      UserLoginQuery user = localUser.getUser("用户信息");
      return baseMapper.queryPayInfo(user.getId());
   }


   /**
    *  查询统计的详情
    * @param page
    * @param outDate
    * @param companyName
    * @param lineName
    * @return
    */
   @Override
   public Page<ApplyCountVo> queryCountInfo(Page<ApplyCountVo> page, String outDate, String companyName, String lineName) {

      LocalDate start = null;
      LocalDate end = null;

      if (StringUtils.isNotBlank(outDate)) {
         start = DateUtil.parseDate(outDate);

         end = start.plusMonths(1).minusDays(1);
      }

      if (StringUtils.isBlank(lineName)) {
         lineName = null;
      }

      return baseMapper.queryCountInfo (page,start,end,companyName,lineName);
   }

   @Override
   public Page<ApplyCountCaiBo> queryCountInfo2(Page<ApplyCountVo> page, String outDate, String companyName, String lineName) {
      LocalDate start = null;
      LocalDate end = null;

      if (StringUtils.isNotBlank(outDate)) {
         start = DateUtil.parseDate(outDate);

         end = start.plusMonths(1).minusDays(1);
      }

      if (StringUtils.isBlank(lineName)) {
         lineName = null;
      }
      return baseMapper.queryCountInfo2 (page,start,end,companyName,lineName);
   }

   /**
    * 查询信息
    * @param id
    * @return
    */
   @Override
   public ApplyCompanyInfo queryInfo(Long id) {
      Apply apply = baseMapper.selectById(id);
      ApplyCompanyInfo bo = new ApplyCompanyInfo();
      bo.setCompanyName(apply.getCompanyName());
      bo.setSxType(apply.getSxType());
      bo.setCaiType(apply.getCaiType());
      return bo;
   }


   /**
    *   财务确认
    * @param outDate
    * @param companyName
    */
   @Override
   public void upCaiMonthCount(String outDate, String companyName) {

      LocalDate start = null;
      LocalDate end = null;

      if (StringUtils.isNotBlank(outDate)) {
         start = DateUtil.parseDate(outDate);

         end = start.plusMonths(1).minusDays(1);
      }

      LambdaQueryWrapper<Apply> wrapper = new LambdaQueryWrapper<Apply>()
            .eq(Apply::getIsSelect,1)
            .eq(Apply::getCompanyName,companyName)
            .ge(Apply::getOutDate,start)
            .le(Apply::getOutDate,end);

      Apply apply = new Apply();
      apply.setCaiType(1);

      int update = baseMapper.update(apply, wrapper);

      if (update <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "确认失败");
      }
   }


   /**
    * 查询待审核条数
    * @return
    */
   @Override
   public ApplyExaCountQuery queryCount() {

      Long id = 634338640817815552L;
      ApplyExaCountQuery result = new ApplyExaCountQuery();
      UserLoginQuery user = localUser.getUser("用户信息");

     List<String> list = new ArrayList<>();

      for (CityJwtBo bo : user.getCity()) {
         list.add(bo.getCity());
      }

      for (PrivilegeMenuQuery query : user.getMenuList()) {
         if (id.equals(query.getMenuId())) {

            LambdaQueryWrapper<Apply> wrapper = new LambdaQueryWrapper<Apply>()
                  .eq(Apply::getStatu,0)
                  .eq(Apply::getIsPayment,1)
                  .in(Apply::getCity,list)
                  .eq(Apply::getIsCancel,0);
            Integer integer = baseMapper.selectCount(wrapper);

            result.setCount(integer);
            return result;

         }
      }

      //
      result.setCount(99999);
      return result;


   }

    @Override
    public ApplyExaCountQuery guiderCount() {
        Long id = 634338100776009728L;
        ApplyExaCountQuery result = new ApplyExaCountQuery();
        UserLoginQuery user = localUser.getUser("用户信息");
        List<String> cityList = new ArrayList<>();

        if (user.getCity().size() > 0) {
            for (CityJwtBo bo : user.getCity()) {
                cityList.add(bo.getCity());
            }
        } else {
            cityList = null;
        }

        for (PrivilegeMenuQuery query : user.getMenuList()) {
            if (id.equals(query.getMenuId())) {

                LambdaQueryWrapper<Apply> wrapper = new LambdaQueryWrapper<Apply>()
                        .eq(Apply::getIsCancel,0)
                        .eq(Apply::getStatu,1)
                        .eq(Apply::getIsPayment,1)
                        .eq(Apply::getIsSelect,0)
                        .le(Apply::getOutDate, new Date())
                        .in(Apply::getCity, cityList);
                Integer integer = baseMapper.selectCount(wrapper);
                result.setCount(integer);
                return result;

            }
        }

        result.setCount(99999);
        return result;
    }

    @Override
    public int queryExamineCount() {
        Long id = 634338640817815552L;
        int result = 99999;
        UserLoginQuery user = localUser.getUser("用户信息");
        
        for (PrivilegeMenuQuery query : user.getMenuList()) {
            if (id.equals(query.getMenuId())) {
                result = baseMapper.queryExamineCount();
                return result;
            }
        }
        return result;
//       return applyMapper.queryExamineCount();
    }

    public ApplyExaCountQuery queryExamineCount2() {
        ApplyExaCountQuery result = new ApplyExaCountQuery();
        Integer integer = queryExamineCount();
        result.setCount(integer);
        return result;
//       return applyMapper.queryExamineCount();
    }
}
