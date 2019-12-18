package eqlee.ctm.finance.settlement.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.entity.websocket.NettyType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.DateUtil;
import com.yq.utils.IdGenerator;
import com.yq.utils.StringUtils;
import eqlee.ctm.finance.other.entity.Other;
import eqlee.ctm.finance.other.entity.vo.OtherUpdateVo;
import eqlee.ctm.finance.other.service.IOtherService;
import eqlee.ctm.finance.settlement.dao.InFinanceMapper;
import eqlee.ctm.finance.settlement.entity.Income;
import eqlee.ctm.finance.settlement.entity.Number;
import eqlee.ctm.finance.settlement.entity.Outcome;
import eqlee.ctm.finance.settlement.entity.bo.*;
import eqlee.ctm.finance.settlement.entity.query.*;
import eqlee.ctm.finance.settlement.entity.vo.ContectUserNumberVo;
import eqlee.ctm.finance.settlement.entity.vo.ContectUserVo;
import eqlee.ctm.finance.settlement.entity.vo.ExamineResultVo;
import eqlee.ctm.finance.settlement.entity.vo.FinanceVo;
import eqlee.ctm.finance.settlement.service.IInFinanceService;
import eqlee.ctm.finance.settlement.service.INumberDetailedService;
import eqlee.ctm.finance.settlement.service.INumberService;
import eqlee.ctm.finance.settlement.service.IOutFinanceService;
import eqlee.ctm.finance.settlement.vilidata.HttpUtils;
import eqlee.ctm.finance.settlement.vilidata.entity.UserIdBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收入service
 * @Author qf
 * @Date 2019/9/27
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class InFinanceServiceImpl extends ServiceImpl<InFinanceMapper, Income> implements IInFinanceService {

    @Autowired
    private LocalUser localUser;

    @Autowired
    private IOutFinanceService outFinanceService;

    @Autowired
    private INumberDetailedService numberDetailedService;

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private IOtherService otherService;

    @Autowired
    private INumberService numberService;

    IdGenerator idGenerator = new IdGenerator();

    private final String EXA_DAI = "待审核";

    private final String EXA_YI = "已通过";

    private final String EXA_WEI = "已拒绝";

    private final String EXA_NO = "未审核";

    private final String EXA_DO = "已审核";

    /**
     * 增加导游消费情况
     * @param vo
     */
    @Override
    public void insertFinance(FinanceVo vo) {
        //获取用户信息
        UserLoginQuery user = localUser.getUser("用户信息");

        //装配收入表
        Income income = new Income();
        long numberId = idGenerator.getNumberId();
        income.setId(numberId);
        income.setCreateUserId(user.getId());
        income.setGuideTel(user.getTel());
        income.setGuideName(user.getCname());
        income.setLineName(vo.getLineName());
        //加入到人数表以及人数明细表
        Number number = new Number();
        Long id = idGenerator.getNumberId();
        number.setId(id);
        number.setCreateUserId(user.getId());
        number.setAllDoNumber(vo.getAllDoNumber());
        number.setTreeAdultNumber(vo.getTreeAdultNumber());
        number.setTreeBabyNumber(vo.getTreeBabyNumber());
        number.setTreeChildNumber(vo.getTreeChildNumber());
        number.setTreeOldNumber(vo.getTreeOldNumber());
        number.setTrueAllNumber(vo.getTrueAllNumber());
        number.setUpdateUserId(user.getId());

        //增加--
        number.setAllPrice(vo.getAllPrice());
        number.setMonthPrice(vo.getMonthPrice());
        //增加人数表
        numberService.insertNumber(number);

        //

        income.setOutDate(DateUtil.parseDate(vo.getOutDate()));
        //结算价(面收金额)
        income.setSettlementPrice(vo.getGaiMoney());
        income.setUpdateUserId(user.getId());
        income.setNumberId(id);

        Double price = 0.0;
        for (OtherBo bo : vo.getOtherInPrice()) {
            price = price + bo.getPrice();
        }
        //总收入
        Double all = price + vo.getGaiMoney();
        income.setAllInPrice(all);

        int insert = baseMapper.insert(income);

        if (insert<= 0) {
            log.error("insert income fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"提交收入表失败");
        }

        //装配其他收入表

        if (vo.getOtherInPrice().size() > 0) {
            List<OtherUpdateVo> list = new ArrayList<>();
            for (OtherBo otherBo : vo.getOtherInPrice()) {
                OtherUpdateVo updateVo = new OtherUpdateVo();
                updateVo.setId(otherBo.getId());
                updateVo.setOtherPrice(otherBo.getPrice());
                updateVo.setIncomeId(numberId);
                //updateOther
                list.add(updateVo);
            }
            otherService.updateOther(list);
        }

        //装配支出表
        Outcome outcome = new Outcome();
        outcome.setId(idGenerator.getNumberId());
        outcome.setIncomeId(numberId);
        outcome.setCarNo(vo.getCarNo());
        outcome.setAllOutPrice(vo.getAllOutPrice());
        outcome.setCreateUserId(user.getId());
        //司机补助
        outcome.setDriverSubsidy(vo.getDriverSubsidy());
        //导游补助
        outcome.setGuideSubsidy(vo.getGuideSubsidy());
        outcome.setLunchPrice(vo.getLunchPrice());
        //停车费
        outcome.setParkingRatePrice(vo.getParkingRatePrice());
        //租车费用
        outcome.setRentCarPrice(vo.getRentCarPrice());
        //门票名
        outcome.setTicketName(vo.getTicketName());
        //门票价格
        outcome.setTicketPrice(vo.getTicketPrice());
        outcome.setUpdateUserId(user.getId());
        outFinanceService.insertOutFinance(outcome);



        //标记该订单已完成
        int i = baseMapper.updateIsFinash(vo.getLineName(), DateUtil.parseDate(vo.getOutDate()), user.getId(), LocalDateTime.now());

        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该线路不存在");
        }


        //修改车辆状态
        if (!vo.getCarType()) {
            //公司内部车辆
            Integer status = baseMapper.updateCarStatus(vo.getCarNo());

            if (status <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "还车失败");
            }
        }

        List<UserIdBo> idList = (List<UserIdBo>) httpUtils.queryAllAdminInfo();

        if (idList.size() == 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "请将财务的角色名设置为财务");
        }

        for (int j = 0; j <= idList.size()-1; j++) {
            UserIdBo bo = JSONObject.parseObject(String.valueOf(idList.get(j)),UserIdBo.class);

            //消息提醒财务审核
            //增加消息提醒的记录
            Integer integer = baseMapper.insertMsg(bo.getAdminId(),user.getId(),NettyType.CAI_EXA.getMsg(),3);

            if (integer <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "提交有误");
            }
        }


    }

    /**
     * 分页查询所有财务审核
     * @param page
     * @param guestName
     * @param type
     * @param outDate
     * @param lineName
     * @return
     */
    @Override
    public Page<ExamineResultQuery> listExamine2Page(Page<ExamineResultQuery> page, String guestName, Integer type, String outDate, String lineName) {

        if (StringUtils.isBlank(guestName)) {
            guestName = null;
        }

        LocalDate outTime = null;

        if (StringUtils.isNotBlank(outDate)) {
            outTime = DateUtil.parseDate(outDate);
        }

        if (StringUtils.isBlank(lineName)) {
            lineName = null;
        }

        return baseMapper.listPageExa (page,guestName,type,outTime,lineName);

    }

    /**
     * 展示该导游的审核详情信息
     * @param Id
     * @return
     */
    @Override
    public Map<String,Object> queryExamineDetailed(Long Id) {

        List<Other> others = otherService.queryOtherByIncome(Id);

        ExamineResultVo vo = baseMapper.listExamineDetailed(Id);

        Map<String,Object> map = new HashMap<>();

        map.put("obj",vo);
        map.put("other",others);

        return map;
    }

    /**
     * 展示导游的个人记录
     * @param page
     * @param exaType
     * @param outDate
     * @param lineName
     * @return
     */
    @Override
    public Page<GuestResultQuery> GuestPage2Me(Page<GuestResultQuery> page, String exaType, String outDate, String lineName) {

        if (StringUtils.isBlank(exaType)) {
            exaType = null;
        }

        int exa = 3;
        if (StringUtils.isNotBlank(exaType)) {
            if (!EXA_DAI.equals(exaType) && !EXA_WEI.equals(exaType) && !EXA_YI.equals(exaType)) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"审核类型输入有误");
            }

            if (EXA_DAI.equals(exaType)) {
                exa = 0;
            }

            if (EXA_YI.equals(exaType)) {
                exa = 1;
            }

            if (EXA_WEI.equals(exaType)) {
                exa = 2;
            }
        }

        if (StringUtils.isBlank(lineName)) {
            lineName = null;
        }

        LocalDate outTime = null;
        if (StringUtils.isNotBlank(outDate)) {
            outTime = DateUtil.parseDate(outDate);
        }

        return baseMapper.pageGuest2Me(page,exa,outTime,lineName);
    }

    /**
     * 财务同意或拒绝审核
     * @param id
     * @param type
     * @return
     */
    @Override
    public ExaResultQuery examineGuestResult(Long id, Integer type) {

        UserLoginQuery user = localUser.getUser("用户信息");

        Income income = baseMapper.selectById(id);

        if (type != 1 && type != 2) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"type参数只能是1或2");
        }

        LocalDateTime time = LocalDateTime.now();

        ExaResultQuery query = new ExaResultQuery();
        if (type == 1) {
            //同意
            int result = baseMapper.examineGuestResult(id, time);


            if (result <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"操作失败");
            }

            //增加消息提醒的记录
            Integer integer = baseMapper.insertMsg(income.getCreateUserId(),user.getId(),NettyType.AGGRE_CAI_EXA.getMsg(),3);

            if (integer <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "增加有误");
            }

            query.setExaResult(1);
            return query;
        }

        //拒绝
        int result = baseMapper.examineResult(id, time);


        if (result <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"操作失败");
        }

        //增加消息提醒的记录
        //财务拒绝的记录
        Integer integer = baseMapper.insertMsg(income.getCreateUserId(),user.getId(),NettyType.AGGRE_CAI_NO_EXA.getMsg(),3);

        if (integer <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "增加有误");
        }

        query.setExaResult(2);
        return query;
    }

    /**
     * 展示所有财务审核的结果
     * @param page
     * @param guideId
     * @return
     */
    @Override
    public Page<ExamineInfoQuery> pageExamine(Page<ExamineInfoQuery> page,String outDate, String lineName, Long guideId) {
        LocalDate date = DateUtil.parseDate(outDate);

        return baseMapper.pageExamine(page,date,lineName,guideId);
    }

    /**
     * 财务的月结统计
     * @param outDate
     * @param companyName
     * @return
     */
    @Override
    public Page<FinanceCompanyBo> pageCompanyCount(Page<FinanceCompanyBo> page,String outDate, String companyName) {
        LocalDate start = null;
        LocalDate end = null;
        if (StringUtils.isNotBlank(outDate)) {
            start = DateUtil.parseDate(outDate);
            //获取当月最后一天
            LocalDate date = start.plusMonths(1);
            end = date.minusDays(1);
        }

        if (StringUtils.isBlank(companyName)) {
            companyName = null;
        }

        return baseMapper.pageFinanceCompany (page,start,end,companyName);
    }

    /**
     * 月结统计 详情
     * @param page
     * @param lineName
     * @param outDate
     * @param accountName
     * @return
     */
    @Override
    public Map<String,Object> queryCompanyInfoCount(Page<FinanceCompanyInfoBo> page, String lineName, String outDate, String accountName) {
        LocalDate start = null;
        LocalDate end = null;
        if (StringUtils.isNotBlank(outDate)) {
            start = DateUtil.parseDate(outDate);
            //获取当月最后一天
            LocalDate date = start.plusMonths(1);
            end = date.minusDays(1);
        }

        if (StringUtils.isBlank(lineName)) {
            lineName = null;
        }


        Page<FinanceCompanyInfoBo> pageList = baseMapper.queryCompanyInfoCount(page, lineName, start, end, accountName);


        CompanyMonthBo bo = new CompanyMonthBo();

        for (FinanceCompanyInfoBo list : pageList.getRecords()) {
            bo.setCompanyNo(list.getCompanyNo());
            bo.setCompanyName(list.getCompanyName());
            bo.setCompanyUserName(list.getCompanyUserName());
            break;
        }

        Map<String,Object> map = new HashMap<>();
        map.put("pageData",pageList);
        map.put("sameData",bo);

        return map;
    }
}
