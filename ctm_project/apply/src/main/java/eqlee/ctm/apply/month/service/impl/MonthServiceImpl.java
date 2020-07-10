package eqlee.ctm.apply.month.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.DateUtil;
import com.yq.utils.IdGenerator;
import eqlee.ctm.apply.entry.entity.vo.ApplyOpenIdVo;
import eqlee.ctm.apply.entry.service.IApplyService;
import eqlee.ctm.apply.month.dao.MonthMapper;
import eqlee.ctm.apply.month.entity.MonthPay;
import eqlee.ctm.apply.month.entity.vo.MonthParamVo;
import eqlee.ctm.apply.month.entity.vo.MonthVo;
import eqlee.ctm.apply.month.service.IMonthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2019/12/27
 * @vesion 1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MonthServiceImpl extends ServiceImpl<MonthMapper, MonthPay> implements IMonthService {

   @Autowired
   private LocalUser localUser;

   @Autowired
   private IApplyService applyService;

   /**
    * 增加月结账单
    * @param vo
    * @return
    */
   @Override
   public MonthVo insertMonth(MonthParamVo vo) {

      UserLoginQuery user = localUser.getUser("用户信息");

      LambdaQueryWrapper<MonthPay> wrapper = new LambdaQueryWrapper<MonthPay>()
            .eq(MonthPay::getStartDate,DateUtil.parseDate(vo.getStartDate()))
            .eq(MonthPay::getCompanyId,vo.getCompanyId());
            //.eq(MonthPay::getCreateUserId,user.getId());

      MonthPay pay = baseMapper.selectOne(wrapper);


      MonthVo result = new MonthVo();
      if (pay != null) {
         result.setId(pay.getId());
         return result;
      }


      IdGenerator idGenerator = new IdGenerator();
      long id = idGenerator.getNumberId();
      String monthNo = idGenerator.getShortNo();

      LocalDate start = DateUtil.parseDate(vo.getStartDate());

      LocalDate endTime = start.plusMonths(1);
      LocalDate end = endTime.minusDays(1);

      String year = vo.getStartDate().substring(0,4);
      String month = vo.getStartDate().substring(5,7);

      String monthInfo = year + "年" + month + "月的月结账单";

      MonthPay monthPay = new MonthPay();
      monthPay.setId(id);
      monthPay.setMonthNo(monthNo);
      monthPay.setKeyId(vo.getKeyId());
      monthPay.setCreateUserId(user.getId());
      monthPay.setUpdateUserId(user.getId());
      monthPay.setStartDate(start);
      monthPay.setEndDate(end);
      monthPay.setCompanyName(vo.getCompanyName());
      monthPay.setMonthInfo(monthInfo);
      monthPay.setPayPrice(vo.getMonthPrice());
      monthPay.setCompanyId(vo.getCompanyId());

      //增加月结账单
      int insert = baseMapper.insert(monthPay);

      if (insert <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "增加失败");
      }

      //装配返回数据
      result.setId(id);
      return result;
   }


   /**
    * 续费授信金额
    * @param monthPrice
    * @return
    */
   @Override
   public MonthVo xuMonth(Double monthPrice) {

      UserLoginQuery user = localUser.getUser("用户信息");
      IdGenerator idGenerator = new IdGenerator();
      long id = idGenerator.getNumberId();
      String monthNo = idGenerator.getShortNo();

      LocalDateTime now = LocalDateTime.now();

      String s = DateUtil.formatDate(now);

      String substring = s.substring(8,9);

      //获取这个月的10号
      String s1 = s.substring(0,8);
      String start = s1 + "10";
      LocalDate dateTime = DateUtil.parseDate(start);
      LocalDate startDate;
      if ("0".equals(substring)) {
         //从上个月的10号到今天
         startDate = dateTime.minusMonths(1);

      } else {
         //从这个月10号到今天
         //这个月10号
         startDate = dateTime;
      }

      //获取结束时间
      LocalDate end = LocalDate.now();

      String monthInfo = "从" + startDate + "到" + end + "的月结账单";


      MonthPay monthPay = new MonthPay();
      monthPay.setId(id);
      monthPay.setMonthNo(monthNo);
      monthPay.setCreateUserId(user.getId());
      monthPay.setUpdateUserId(user.getId());
      monthPay.setStartDate(startDate);
      monthPay.setEndDate(end);
      monthPay.setMonthInfo(monthInfo);
      monthPay.setPayPrice(monthPrice);

      //增加月结账单
      int insert = baseMapper.insert(monthPay);

      if (insert <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "增加失败");
      }

      //装配返回数据
      MonthVo result = new MonthVo();
      result.setId(id);
      result.setMonthInfo(monthInfo);
      result.setMonthNo(monthNo);
      result.setMonthPrice(monthPrice);

      return result;
   }


   /**
    * 修改月结账单
    * @param monthPay
    */
   @Override
   public void updateMonth(MonthPay monthPay) {

      int update = baseMapper.updateById(monthPay);

      if (update <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "修改失败");
      }
   }


   /**
    *  查询
    * @param id
    * @return
    */
   @Override
   public MonthVo queryInfo(Long id) {

      UserLoginQuery user = localUser.getUser("用户信息");

      MonthPay monthPay = baseMapper.selectById(id);

      MonthVo vo = new MonthVo();
      vo.setId(monthPay.getId());
      vo.setMonthPrice(monthPay.getPayPrice());
      vo.setMonthNo(monthPay.getMonthNo());
      vo.setMonthInfo(monthPay.getMonthInfo());
      LocalDateTime now = LocalDateTime.now();
      vo.setTime(DateUtil.formatDateTime(now));

      ApplyOpenIdVo idVo = applyService.queryAuto(user.getId());
      if (idVo == null) {
         vo.setAuto(false);
      } else {
         vo.setAuto(true);
      }
      return vo;
   }


   /**
    * 修改月结支付状态
    * @param monthNo
    */
   @Override
   public void updateMonthStatus(String monthNo) {

      LambdaQueryWrapper<MonthPay> wrapper = new LambdaQueryWrapper<MonthPay>()
            .eq(MonthPay::getMonthNo,monthNo);

      MonthPay pay = new MonthPay();
      pay.setIsPay(true);
      int update = baseMapper.update(pay, wrapper);

      if (update <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR);
      }
   }


   /**
    * 查询支付的单号
    * @param keyId
    * @return
    */
   @Override
   public MonthVo queryInfoByTime(Long keyId) {

      LambdaQueryWrapper<MonthPay> wrapper = new LambdaQueryWrapper<MonthPay>()
            .eq(MonthPay::getKeyId,keyId);

      MonthPay pay = baseMapper.selectOne(wrapper);

      MonthVo vo = new MonthVo();
      vo.setMonthNo(pay.getMonthNo());
      return vo;
   }
}
