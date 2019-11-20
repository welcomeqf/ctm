package eqlee.ctm.apply.entry.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.DateUtil;
import com.yq.utils.IdGenerator;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.entry.dao.ApplyMapper;
import eqlee.ctm.apply.entry.entity.Apply;
import eqlee.ctm.apply.entry.entity.bo.UserAdminBo;
import eqlee.ctm.apply.entry.entity.query.*;
import eqlee.ctm.apply.entry.entity.vo.*;
import eqlee.ctm.apply.entry.service.IApplyService;
import eqlee.ctm.apply.entry.service.IExamineService;
import eqlee.ctm.apply.entry.vilidata.HttpUtils;
import eqlee.ctm.apply.line.entity.Line;
import eqlee.ctm.apply.line.service.ILineService;
import eqlee.ctm.apply.orders.entity.Vo.LongVo;
import eqlee.ctm.apply.price.entity.Price;
import eqlee.ctm.apply.price.service.IPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    private HttpUtils httpUtils;

    private final String MONTH_PAY = "月结";

    private final String NOW_PAY = "现结";

    private final String AGENT_PAY = "面收";

    private final String APPLY_EXA = "报名审核";

    /**
     * 消息中的msg
     */
    private final String APPLY_DO = "报名申请审核";

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
        Price price = priceService.queryPrice(localDate,applyVo.getLineName());

        if (price == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该天还未开放");
        }

        if (!price.getLineId().equals(line.getId())) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该线路该天还未设置价格");
        }

        //算出总价格
        Double AdultPrice = price.getAdultPrice()*applyVo.getAdultNumber();
        Double BabyPrice = price.getBabyPrice()*applyVo.getBabyNumber();
        Double OldPrice = price.getOldPrice()*applyVo.getOldNumber();
        Double ChildPrice = price.getChildPrice()*applyVo.getChildNumber();
        Double AllPrice = AdultPrice + BabyPrice + OldPrice + ChildPrice;
        //算出总人数
        Integer AllNumber = applyVo.getAdultNumber() + applyVo.getChildNumber() + applyVo.getOldNumber() + applyVo.getBabyNumber();
        //生成报名单号
        String orderCode = idGenerator.getOrderCode();

        UserLoginQuery user = localUser.getUser("用户信息");

        if ("同行".equals(user.getRoleName())) {
            applyVo.setCompanyNameId(user.getCompanyId());
            applyVo.setCName(user.getCname());
        } else {
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

            applyVo.setCompanyNameId(userById.getCompanyId());
            applyVo.setCompanyUser(userById.getAccount());
            applyVo.setCreateUserId(userById.getId());
            applyVo.setUpdateUserId(userById.getId());
            applyVo.setCName(userById.getCname());
        }

        Company company = baseMapper.queryCompanyById(applyVo.getCompanyNameId());

        if (company == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "您的公司还未注册.");
        }

        //装配实体类
        Apply apply = new Apply();

        Long id = idGenerator.getNumberId();
        if ("同行".equals(user.getRoleName())) {
            apply.setId(id);
            apply.setAdultNumber(applyVo.getAdultNumber());
            apply.setBabyNumber(applyVo.getBabyNumber());
            apply.setChildNumber(applyVo.getChildNumber());
            apply.setOldNumber(applyVo.getOldNumber());
            apply.setAllNumber(AllNumber);
            apply.setAllPrice(AllPrice);
            apply.setApplyNo(orderCode);
            apply.setCompanyName(company.getCompanyName());
            apply.setCompanyUser(user.getAccount());
            apply.setContactName(applyVo.getContactName());
            apply.setContactTel(applyVo.getContactTel());
            apply.setPlace(applyVo.getPlace());
            apply.setRegion(line.getRegion());
            apply.setLineId(line.getId());
            apply.setOutDate(localDate);
            apply.setCreateUserId(user.getId());
            apply.setUpdateUserId(user.getId());
            apply.setCName(applyVo.getCName());
            apply.setMarketAllPrice(applyVo.getMarketAllPrice());

        } else {
            //运营人员代录
            apply.setId(id);
            apply.setAdultNumber(applyVo.getAdultNumber());
            apply.setBabyNumber(applyVo.getBabyNumber());
            apply.setChildNumber(applyVo.getChildNumber());
            apply.setOldNumber(applyVo.getOldNumber());
            apply.setAllNumber(AllNumber);
            apply.setAllPrice(AllPrice);
            apply.setApplyNo(orderCode);
            apply.setCompanyName(company.getCompanyName());
            apply.setCompanyUser(applyVo.getCompanyUser());
            apply.setContactName(applyVo.getContactName());
            apply.setContactTel(applyVo.getContactTel());
            apply.setPlace(applyVo.getPlace());
            apply.setRegion(line.getRegion());
            apply.setLineId(line.getId());
            apply.setOutDate(localDate);
            apply.setCreateUserId(applyVo.getCreateUserId());
            apply.setUpdateUserId(applyVo.getUpdateUserId());
            apply.setCName(applyVo.getCName());
            apply.setMarketAllPrice(applyVo.getMarketAllPrice());

        }


        //设置过期时间
        apply.setExpreDate(30);

        if (MONTH_PAY.equals(applyVo.getPayType())) {
            apply.setPayType(1);
            apply.setIsPayment(true);
        }
        if (NOW_PAY.equals(applyVo.getPayType())) {
            apply.setPayType(0);
        }
        if (AGENT_PAY.equals(applyVo.getPayType())) {
            apply.setPayType(2);
            apply.setIsPayment(true);
        }

        if (!MONTH_PAY.equals(applyVo.getPayType()) && !NOW_PAY.equals(applyVo.getPayType()) && !AGENT_PAY.equals(applyVo.getPayType())) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"您输入支付类型有误，请重新输入");
        }

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
//        for (UserAdminBo bo : idList) {
//            longList.add(bo.getAdminId());
//        }
//
//        //批量增加所有运营审核的报名消息提醒
//        MsgAddVo msgVo = new MsgAddVo();
//        msgVo.setCreateId(user.getId());
//        msgVo.setMsgType(3);
//        msgVo.setMsg(APPLY_DO);
//        msgVo.setToId(longList);
//
//        httpUtils.addAllMsg(msgVo);

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
    public Page<ApplyQuery> listPageApply(Page<ApplyQuery> page,String OutDate, String LineNameOrRegion) {
        if (StringUtils.isBlank(OutDate) && StringUtils.isBlank(LineNameOrRegion) ) {
            //分页查询所有报名记录
            return baseMapper.listPageApply(page);
        }

        if (StringUtils.isBlank(LineNameOrRegion) && StringUtils.isNotBlank(OutDate)) {
            //分页及时间条件查询报名记录
            LocalDate localDate = DateUtil.parseDate(OutDate);
            return baseMapper.listPageApplyByDate(page,localDate);
        }

        if (StringUtils.isBlank(OutDate) && StringUtils.isNotBlank(LineNameOrRegion)) {
            //分页及线路名或区域模糊查询报名记录
            return baseMapper.listPageApplyByLine(page,LineNameOrRegion);
        }

        //分页及时间条件查询及线路名或区域模糊查询报名记录
        LocalDate localDate = DateUtil.parseDate(OutDate);
        return baseMapper.listPageApplyByAll(page,LineNameOrRegion,localDate);
    }

    /**
     * 修改报名表
     * @param updateInfo
     */
    @Override
    public void updateApply(ApplyUpdateInfo updateInfo) {
        Apply apply = new Apply();
        apply.setId(updateInfo.getId());
        apply.setContactName(updateInfo.getContactName());
        apply.setContactTel(updateInfo.getContactTel());
        apply.setPlace(updateInfo.getPlace());
        LocalDateTime now = LocalDateTime.now();
        apply.setRemark(now + "修改");
        int result = baseMapper.updateById(apply);

        if (result <= 0) {
            log.error("update apply fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改报名表失败");
        }
    }

    /**
     * 分页查询已报名未审核的列表,可以根据出发时间模糊查询
     *（默认出来本公司所有的数据）
     * @param page
     * @param OutDate
     * @param LineName
     * @return
     */
    @Override
    public Page<ApplyDoExaQuery> listPageDoApply(Page<ApplyDoExaQuery> page, String OutDate, String LineName , String ApplyType) {

        if (StringUtils.isBlank(ApplyType) || APPLY_EXA.equals(ApplyType)) {
            //若审核为空或是报名审核都默认报名审核记录

            if (StringUtils.isBlank(OutDate) && StringUtils.isBlank(LineName)) {
                //当出发时间 和线路名 都为空时
                return baseMapper.listPageDoApply(page);
            }

            if (StringUtils.isBlank(LineName) && StringUtils.isNotBlank(OutDate)) {
                //当线路名为空
                LocalDate outDate = DateUtil.parseDate(OutDate);
                return baseMapper.listPageDoApplyByNo(page,outDate);
            }

            if (StringUtils.isBlank(OutDate) && StringUtils.isNotBlank(LineName)) {
                //当出发时间为空
                return baseMapper.listPageDoApplyByLineName(page,LineName);
            }

            if (StringUtils.isNotBlank(OutDate) && StringUtils.isNotBlank(LineName)) {
                //根据订单号和线路名模糊查询分页数据
                LocalDate outDate = DateUtil.parseDate(OutDate);
                return baseMapper.listPageDoApplyByNoWithLine(page,LineName,outDate);
            }

        }

        //取消审核的记录--
        if (StringUtils.isBlank(LineName) && StringUtils.isNotBlank(OutDate)) {
            //当线路名为空
            LocalDate outDate = DateUtil.parseDate(OutDate);
            return baseMapper.listPageNotCancelByTime(page,outDate);
        }

        if (StringUtils.isBlank(OutDate) && StringUtils.isBlank(LineName)) {
            //当出发时间 和线路名 都为空时
            return baseMapper.listPageNotCancel(page);
        }

        if (StringUtils.isBlank(OutDate) && StringUtils.isNotBlank(LineName)) {
            //当出发时间为空
            return baseMapper.listPageNotCancelByLineName(page,LineName);

        }

        //三者都不为空并且是取消审核的记录
        LocalDate outDate = DateUtil.parseDate(OutDate);
        return baseMapper.listPageNotCancelByTimeWithLine(page,LineName,outDate);
    }

    /**
     * 分页查询已报名已审核的列表,可以根据出发时间模糊查询
     *（默认出来本公司所有的数据）
     * @param page
     * @param OutDate
     * @param LineName
     * @return
     */
    @Override
    public Page<ApplyDoExaQuery> toListPageDoApply(Page<ApplyDoExaQuery> page, String OutDate, String LineName, String ApplyType) {

        if (StringUtils.isBlank(ApplyType)) {
            //类型为空
            if (StringUtils.isBlank(OutDate) && StringUtils.isBlank(LineName)) {
                //当出发时间 和线路名 都为空时
                return baseMapper.toListPageDoApply(page);
            }

            if (StringUtils.isBlank(LineName) && StringUtils.isNotBlank(OutDate)) {
                //当线路名为空
                LocalDate outDate = DateUtil.parseDate(OutDate);
                return baseMapper.toListPageDoApplyByNo(page,outDate);
            }

            if (StringUtils.isBlank(OutDate) && StringUtils.isNotBlank(LineName)) {
                //当出发时间为空
                return baseMapper.toListPageDoApplyByLineName(page,LineName);
            }

            //根据订单号和线路名模糊查询分页数据
            LocalDate outDate = DateUtil.parseDate(OutDate);
            return baseMapper.toListPageDoApplyByNoWithLine(page,LineName,outDate);
        }

        //类型不为空
        String type = null;
        if (APPLY_EXA.equals(ApplyType)) {
            type = "0";
        } else {
            type = "1";
        }

        if (StringUtils.isBlank(OutDate) && StringUtils.isBlank(LineName)) {
            //当出发时间 和线路名 都为空时
            return baseMapper.toListPageDoExa(page,type);
        }

        if (StringUtils.isBlank(LineName) && StringUtils.isNotBlank(OutDate)) {
            //当线路名为空
            LocalDate outDate = DateUtil.parseDate(OutDate);
            return baseMapper.toListPageDoExaByNo(page,outDate,type);
        }

        if (StringUtils.isBlank(OutDate) && StringUtils.isNotBlank(LineName)) {
            //当出发时间为空
            return baseMapper.toListPageDoExaByLineName(page,LineName,type);
        }

        //根据订单号和线路名模糊查询分页数据
        LocalDate outDate = DateUtil.parseDate(OutDate);
        return baseMapper.toListPageDoExaByNoWithLine(page,LineName,outDate,type);

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
    public void updateExamineStatus(Long Id, Integer Status) {
        Apply apply = new Apply();
        apply.setId(Id);
        apply.setStatu(Status);
        int i = baseMapper.updateById(apply);

        if (i <= 0) {
            log.error("update examine status fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"报名表修改审核状态失败");
        }
    }

    /**
     * 查询所有报名表
     * @return
     */
    @Override
    public List<Apply> selectAllApply() {
        return baseMapper.selectList(null);
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

        //出发日期
        vo.setOutDate(DateUtil.formatDate(apply.getOutDate()));

        vo.setCreateDate(DateUtil.formatDateTime(apply.getCreateDate()));
        vo.setUpdateDate(DateUtil.formatDateTime(apply.getUpdateDate()));

        return vo;
    }

    /**
     * 我的报名记录
     * @param page
     * @param lineName
     * @param outDate
     * @param applyTime
     * @param type
     * @return
     */
    @Override
    public Page<ApplyCompanyQuery> page2MeApply(Page<ApplyCompanyQuery> page, String lineName, String outDate, String applyTime, Integer type) {

        UserLoginQuery user = localUser.getUser("用户信息");

        if (StringUtils.isNotBlank(applyTime) && StringUtils.isNotBlank(outDate)) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "请勿同时选择两个时间");
        }

        //报名时间为空
        if (StringUtils.isBlank(applyTime)) {

            if (StringUtils.isBlank(lineName) && StringUtils.isBlank(outDate) && type == null) {
                //都为空
                return baseMapper.listPageDoApply2Me(page,user.getId());
            }

            if (StringUtils.isBlank(lineName) && StringUtils.isNotBlank(outDate) && type == null) {
                //根据出行日期查询
                LocalDate localDate = DateUtil.parseDate(outDate);
                return baseMapper.listPageDoApply2MeByTime(page,localDate,user.getId());
            }

            if (StringUtils.isNotBlank(lineName) && StringUtils.isBlank(outDate) && type == null) {
                //根据线路名模糊查询
                return baseMapper.listPageDoApply2MeByName(page,lineName,user.getId());
            }

            //都不为空
            if (StringUtils.isNotBlank(lineName) && StringUtils.isNotBlank(outDate) && type == null) {
                LocalDate localDate = DateUtil.parseDate(outDate);
                return baseMapper.listPageDoApply2MeByNameAndTime(page,lineName,localDate,user.getId());
            }

            if (StringUtils.isBlank(lineName) && StringUtils.isBlank(outDate) && type == 0) {
                //都为空
                return baseMapper.listPageDoApply2MeCancel(page,user.getId());
            }

            if (StringUtils.isBlank(lineName) && StringUtils.isNotBlank(outDate) && type == 0) {
                //根据出行日期查询
                LocalDate localDate = DateUtil.parseDate(outDate);
                return baseMapper.listPageDoApply2MeByTimeCancel(page,localDate,user.getId());
            }

            if (StringUtils.isNotBlank(lineName) && StringUtils.isBlank(outDate) && type == 0) {
                //根据线路名模糊查询
                return baseMapper.listPageDoApply2MeByNameCancel(page,lineName,user.getId());
            }

            //都不为空
            if (StringUtils.isNotBlank(lineName) && StringUtils.isNotBlank(outDate) && type == 0) {
                LocalDate localDate = DateUtil.parseDate(outDate);
                return baseMapper.listPageDoApply2MeByNameAndTimeCancel(page,lineName,localDate,user.getId());
            }
        }

        //报名日期不为空
        if (StringUtils.isNotBlank(applyTime)) {

            String start = applyTime + " 00:00:00";
            String end = applyTime + " 23:59:59";
            LocalDateTime startDate = DateUtil.parseDateTime(start);
            LocalDateTime endDate = DateUtil.parseDateTime(end);

            if (StringUtils.isBlank(lineName) && type == null) {
                //根据报名日期查询

                return baseMapper.listPageDoApply2MeByTimeWithAT(page,startDate,endDate,user.getId());
            }

            //都不为空
            if (StringUtils.isNotBlank(lineName) && type == null) {
                return baseMapper.listPageDoApply2MeByNameAndTimeWithAT(page,lineName,startDate,endDate,user.getId());
            }

            if (StringUtils.isBlank(lineName) && type == 0) {
                //根据报名日期查询
                return baseMapper.listPageDoApply2MeByTimeCancelWithAT(page,startDate,endDate,user.getId());
            }

            //都不为空
            if (StringUtils.isNotBlank(lineName)  && type == 0) {
                return baseMapper.listPageDoApply2MeByNameAndTimeCancelWithAT(page,lineName,startDate,endDate,user.getId());
            }
        }

        //查询今天已报名
        if (type == 1) {
            LocalDate now = LocalDate.now();
            String nowTime = DateUtil.formatDate(now);
            //加上分秒
            String start = nowTime + " 00:00:00";
            String end = nowTime + " 23:59:59";
            LocalDateTime sartDate = DateUtil.parseDateTime(start);
            LocalDateTime endDate = DateUtil.parseDateTime(end);

            return baseMapper.listToDayApply(page,sartDate,endDate,user.getId());
        }

        //查询今天出行
        if (type == 2) {
            LocalDate now = LocalDate.now();
            return baseMapper.listToDayOut(page,now,user.getId());
        }

        if (type != 0 && type != 1 && type != 2 && type != null) {
            //传入参数有误
            throw new ApplicationException(CodeType.SERVICE_ERROR, "参数type有误");
        }
        return null;
    }

    /**
     * 我的报名记录（运营人员）
     * @param page
     * @param lineName
     * @param outDate
     * @param applyTime
     * @param type
     * @param companyUserId
     * @return
     */
    @Override
    public Page<ApplyCompanyQuery> pageAdminApply(Page<ApplyCompanyQuery> page, String lineName, String outDate, String applyTime, Integer type, Long companyUserId) {

        if (StringUtils.isNotBlank(applyTime) && StringUtils.isNotBlank(outDate)) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "请勿同时选择两个时间");
        }

        if (companyUserId == null) {
            //报名时间为空
            if (StringUtils.isBlank(applyTime)) {

                if (StringUtils.isBlank(lineName) && StringUtils.isBlank(outDate) && type == null) {
                    //都为空
                    return baseMapper.listAdminDoApply2Me(page);
                }

                if (StringUtils.isBlank(lineName) && StringUtils.isNotBlank(outDate) && type == null) {
                    //根据出行日期查询
                    LocalDate localDate = DateUtil.parseDate(outDate);
                    return baseMapper.listAdminDoApply2MeByTime(page,localDate);
                }

                if (StringUtils.isNotBlank(lineName) && StringUtils.isBlank(outDate) && type == null) {
                    //根据线路名模糊查询
                    return baseMapper.listAdminDoApply2MeByName(page,lineName);
                }

                //都不为空
                if (StringUtils.isNotBlank(lineName) && StringUtils.isNotBlank(outDate) && type == null) {
                    LocalDate localDate = DateUtil.parseDate(outDate);
                    return baseMapper.listAdminDoApply2MeByNameAndTime(page,lineName,localDate);
                }

                if (StringUtils.isBlank(lineName) && StringUtils.isBlank(outDate) && type == 0) {
                    //都为空
                    return baseMapper.listAdminDoApply2MeCancel(page);
                }

                if (StringUtils.isBlank(lineName) && StringUtils.isNotBlank(outDate) && type == 0) {
                    //根据出行日期查询
                    LocalDate localDate = DateUtil.parseDate(outDate);
                    return baseMapper.listAdminDoApply2MeByTimeCancel(page,localDate);
                }

                if (StringUtils.isNotBlank(lineName) && StringUtils.isBlank(outDate) && type == 0) {
                    //根据线路名模糊查询
                    return baseMapper.listAdminDoApply2MeByNameCancel(page,lineName);
                }

                //都不为空
                if (StringUtils.isNotBlank(lineName) && StringUtils.isNotBlank(outDate) && type == 0) {
                    LocalDate localDate = DateUtil.parseDate(outDate);
                    return baseMapper.listAdminDoApply2MeByNameAndTimeCancel(page,lineName,localDate);
                }
            }

            //报名日期不为空
            if (StringUtils.isNotBlank(applyTime)) {

                String start = applyTime + " 00:00:00";
                String end = applyTime + " 23:59:59";
                LocalDateTime startDate = DateUtil.parseDateTime(start);
                LocalDateTime endDate = DateUtil.parseDateTime(end);

                if (StringUtils.isBlank(lineName) && type == null) {
                    //根据报名日期查询

                    return baseMapper.listAdminDoApply2MeByTimeWithAT(page,startDate,endDate);
                }

                //都不为空
                if (StringUtils.isNotBlank(lineName) && type == null) {
                    return baseMapper.listAdminDoApply2MeByNameAndTimeWithAT(page,lineName,startDate,endDate);
                }

                if (StringUtils.isBlank(lineName) && type == 0) {
                    //根据报名日期查询
                    return baseMapper.listAdminDoApply2MeByTimeCancelWithAT(page,startDate,endDate);
                }

                //都不为空
                if (StringUtils.isNotBlank(lineName)  && type == 0) {
                    return baseMapper.listAdminDoApply2MeByNameAndTimeCancelWithAT(page,lineName,startDate,endDate);
                }
            }
        }


        //报名时间为空
        if (StringUtils.isBlank(applyTime)) {

            if (StringUtils.isBlank(lineName) && StringUtils.isBlank(outDate) && type == null) {
                //都为空
                return baseMapper.listPageDoApply2Me(page,companyUserId);
            }

            if (StringUtils.isBlank(lineName) && StringUtils.isNotBlank(outDate) && type == null) {
                //根据出行日期查询
                LocalDate localDate = DateUtil.parseDate(outDate);
                return baseMapper.listPageDoApply2MeByTime(page,localDate,companyUserId);
            }

            if (StringUtils.isNotBlank(lineName) && StringUtils.isBlank(outDate) && type == null) {
                //根据线路名模糊查询
                return baseMapper.listPageDoApply2MeByName(page,lineName,companyUserId);
            }

            //都不为空
            if (StringUtils.isNotBlank(lineName) && StringUtils.isNotBlank(outDate) && type == null) {
                LocalDate localDate = DateUtil.parseDate(outDate);
                return baseMapper.listPageDoApply2MeByNameAndTime(page,lineName,localDate,companyUserId);
            }

            if (StringUtils.isBlank(lineName) && StringUtils.isBlank(outDate) && type == 0) {
                //都为空
                return baseMapper.listPageDoApply2MeCancel(page,companyUserId);
            }

            if (StringUtils.isBlank(lineName) && StringUtils.isNotBlank(outDate) && type == 0) {
                //根据出行日期查询
                LocalDate localDate = DateUtil.parseDate(outDate);
                return baseMapper.listPageDoApply2MeByTimeCancel(page,localDate,companyUserId);
            }

            if (StringUtils.isNotBlank(lineName) && StringUtils.isBlank(outDate) && type == 0) {
                //根据线路名模糊查询
                return baseMapper.listPageDoApply2MeByNameCancel(page,lineName,companyUserId);
            }

            //都不为空
            if (StringUtils.isNotBlank(lineName) && StringUtils.isNotBlank(outDate) && type == 0) {
                LocalDate localDate = DateUtil.parseDate(outDate);
                return baseMapper.listPageDoApply2MeByNameAndTimeCancel(page,lineName,localDate,companyUserId);
            }
        }

        //报名日期不为空
        if (StringUtils.isNotBlank(applyTime)) {

            String start = applyTime + " 00:00:00";
            String end = applyTime + " 23:59:59";
            LocalDateTime startDate = DateUtil.parseDateTime(start);
            LocalDateTime endDate = DateUtil.parseDateTime(end);

            if (StringUtils.isBlank(lineName) && type == null) {
                //根据报名日期查询

                return baseMapper.listPageDoApply2MeByTimeWithAT(page,startDate,endDate,companyUserId);
            }

            //都不为空
            if (StringUtils.isNotBlank(lineName) && type == null) {
                return baseMapper.listPageDoApply2MeByNameAndTimeWithAT(page,lineName,startDate,endDate,companyUserId);
            }

            if (StringUtils.isBlank(lineName) && type == 0) {
                //根据报名日期查询
                return baseMapper.listPageDoApply2MeByTimeCancelWithAT(page,startDate,endDate,companyUserId);
            }

            //都不为空
            if (StringUtils.isNotBlank(lineName)  && type == 0) {
                return baseMapper.listPageDoApply2MeByNameAndTimeCancelWithAT(page,lineName,startDate,endDate,companyUserId);
            }
        }

        //查询今天已报名
        if (type == 1) {
            LocalDate now = LocalDate.now();
            String nowTime = DateUtil.formatDate(now);
            //加上分秒
            String start = nowTime + " 00:00:00";
            String end = nowTime + " 23:59:59";
            LocalDateTime sartDate = DateUtil.parseDateTime(start);
            LocalDateTime endDate = DateUtil.parseDateTime(end);

            return baseMapper.listToDayApply(page,sartDate,endDate,companyUserId);
        }

        //查询今天出行
        if (type == 2) {
            LocalDate now = LocalDate.now();
            return baseMapper.listToDayOut(page,now,companyUserId);
        }

        if (type != 0 && type != 1 && type != 2 && type != null) {
            //传入参数有误
            throw new ApplicationException(CodeType.SERVICE_ERROR, "参数type有误");
        }


        return null;
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
    public void updateAllGuestStatus(List<LongVo> list) {

        Integer integer = baseMapper.updateAllApply(list);

        if (integer <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改客人的状态失败");
        }
    }

    /**
     * 查询报名表
     * @param outDate
     * @param lineName
     * @return
     */
    @Override
    public List<Apply> queryApplyByTime(LocalDate outDate, String lineName) {
        //查询线路id
        Line line = lineService.queryLineByName(lineName);

        LambdaQueryWrapper<Apply> lambdaQueryWrapper = new LambdaQueryWrapper<Apply>()
                .eq(Apply::getOutDate,outDate)
                .eq(Apply::getLineId,line.getId());

        return baseMapper.selectList(lambdaQueryWrapper);
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
    public Page<ApplyMonthQuery> queryMonthApply(Page<ApplyMonthQuery> page, Integer type,String outDate) {

        if (type == 1 && StringUtils.isBlank(outDate)) {
            //未付款信息
               return baseMapper.queryMonthWeiApply(page);
        }

        if (type == 1 && StringUtils.isNotBlank(outDate)) {
            //根据出行日期查询未付款信息
            LocalDate localDate = DateUtil.parseDate(outDate);
            return baseMapper.queryMonthWeiApplyByTime(page,localDate);
        }

        if (type == 2 && StringUtils.isBlank(outDate)) {
            //已付款信息
            return baseMapper.queryMonthYiApply(page);
        }

        if (type == 2 && StringUtils.isNotBlank(outDate)) {
            //根据出行日期查询已付款信息
            LocalDate localDate = DateUtil.parseDate(outDate);
            return baseMapper.queryMonthYiApplyByTime(page,localDate);
        }

        if (StringUtils.isNotBlank(outDate)) {
            //根据出行日期查询全部
            LocalDate localDate = DateUtil.parseDate(outDate);
            return baseMapper.queryMonthApplyByTime(page,localDate);
        }

        //查询全部
        return baseMapper.queryMonthApply(page);
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
              .ne(Apply::getIsCancel,1);

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
     * @param payType
     * @param outDate
     * @param lineName
     * @return
     */
    @Override
    public Page<ApplyResultCountQuery> pageResultCountList(Page<ApplyResultCountQuery> page, Integer payType, String outDate, String lineName) {
        if (StringUtils.isBlank(outDate) && StringUtils.isBlank(lineName)) {
            //线路名和出发时间为空时
            return baseMapper.pageResult(page,payType);
        }

        if (StringUtils.isBlank(outDate) && StringUtils.isNotBlank(lineName)) {
            //线路名不为空
            return baseMapper.pageResultByLineName(page,payType,lineName);
        }

        if (StringUtils.isNotBlank(outDate) && StringUtils.isBlank(lineName)) {
            //出发时间不为空
            LocalDate date = DateUtil.parseDate(outDate);
            return baseMapper.pageResultByOutDate(page,payType,date);
        }

        //都不为空
        LocalDate date = DateUtil.parseDate(outDate);
        return baseMapper.pageResultByTimeAndName(page,payType,date,lineName);
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
     * 查询所有管理员id
     * @return
     */
    @Override
    public List<Long> queryAdminIds() {
        return baseMapper.queryAllAdmin("运营人员");
    }


}
