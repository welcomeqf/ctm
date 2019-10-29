package eqlee.ctm.finance.settlement.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.DateUtil;
import com.yq.utils.IdGenerator;
import com.yq.utils.StringUtils;
import eqlee.ctm.finance.settlement.dao.InFinanceMapper;
import eqlee.ctm.finance.settlement.entity.Income;
import eqlee.ctm.finance.settlement.entity.Number;
import eqlee.ctm.finance.settlement.entity.Outcome;
import eqlee.ctm.finance.settlement.entity.query.*;
import eqlee.ctm.finance.settlement.entity.vo.ContectUserNumberVo;
import eqlee.ctm.finance.settlement.entity.vo.ContectUserVo;
import eqlee.ctm.finance.settlement.entity.vo.ExamineResultVo;
import eqlee.ctm.finance.settlement.entity.vo.FinanceVo;
import eqlee.ctm.finance.settlement.service.IInFinanceService;
import eqlee.ctm.finance.settlement.service.INumberDetailedService;
import eqlee.ctm.finance.settlement.service.INumberService;
import eqlee.ctm.finance.settlement.service.IOutFinanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        number.setUnpaidNumber(vo.getUnpaidNumber());
        number.setUpdateUserId(user.getId());
        //增加人数表
        numberService.insertNumber(number);

        //将未付款代付信息批量插入数据库

        if (vo.getUnpaidList().size() != 0) {
            List<ContectUserNumberVo> list = new ArrayList<>();

            for (ContectUserVo contectUserVo : vo.getUnpaidList()) {
                ContectUserNumberVo vo1 = new ContectUserNumberVo();
                vo1.setId(idGenerator.getNumberId());
                vo1.setNumberId(id);
                vo1.setAdultNumber(contectUserVo.getAdultNumber());
                vo1.setAllNumber(contectUserVo.getAllNumber());
                vo1.setAllPrice(contectUserVo.getAllPrice());
                vo1.setBabyNumber(contectUserVo.getBabyNumber());
                vo1.setChildNumber(contectUserVo.getChildNumber());
                vo1.setContectUserName(contectUserVo.getContectUserName());
                vo1.setContectUserTel(contectUserVo.getContectUserTel());
                vo1.setOldNumber(contectUserVo.getOldNumber());
                vo1.setCreateUserId(user.getId());
                vo1.setUpdateUserId(user.getId());
                list.add(vo1);
            }
            numberDetailedService.insertAllNumberDetailed(list);
        }

        //

        income.setOutDate(DateUtil.parseDate(vo.getOutDate()));
        //结算价(应收金额)
        income.setSettlementPrice(vo.getGaiMoney());
        //代收费用（实收金额）
        income.setSubstitutionPrice(vo.getTrueMoney());
        income.setUpdateUserId(user.getId());
        income.setNumberId(id);
        int insert = baseMapper.insert(income);

        if (insert<= 0) {
            log.error("insert income fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"提交收入表失败");
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

    }

    /**
     * 分页查询所有财务审核
     * @param page
     * @param guestName
     * @param
     * @return
     */
    @Override
    public Page<ExamineResultQuery> listExamine2Page(Page<ExamineResultQuery> page, String guestName, String type) {

        if (StringUtils.isBlank(guestName) && StringUtils.isBlank(type)) {
            //查询全部
            return baseMapper.listExamine2Page(page);
        }

        if (StringUtils.isBlank(guestName) && StringUtils.isNotBlank(type)) {
            //未审核  已审核
            if (!EXA_NO.equals(type) && !EXA_DO.equals(type)) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "type传入有误");
            }

            if (EXA_NO.equals(type)) {
                //未审核的记录
                return baseMapper.listExamine2No(page);
            }

            //已审核的记录
            return baseMapper.listExamine2Do(page);
        }

        if (StringUtils.isNotBlank(guestName) && StringUtils.isBlank(type)) {
            return baseMapper.listExamine2PageByGuestName(page,guestName);
        }

        //都不为空
        if (!EXA_NO.equals(type) && !EXA_DO.equals(type)) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "type传入有误");
        }

        if (EXA_NO.equals(type)) {
            //未审核的记录
            return baseMapper.listExamine2NoByGuestName(page,guestName);
        }

        //已审核的记录
        return baseMapper.listExamine2DoByGuestName(page,guestName);
    }

    /**
     * 展示该导游的审核详情信息
     * @param Id
     * @return
     */
    @Override
    public ExamineDetailedQuery queryExamineDetailed(Long Id) {
        List<ExamineResultVo> voList = baseMapper.listExamineDetailed(Id);
        //重新装配数据返回
        ExamineDetailedQuery query = new ExamineDetailedQuery();
        List<ExamineContectQuery> queryList = new ArrayList<>();
        Integer allNoNumber = 0;
        Double allNoPrice = 0.0;
        for (ExamineResultVo vo : voList) {
            ExamineContectQuery contectQuery = new ExamineContectQuery();
            //装配联系人
            if ((StringUtils.isBlank(vo.getContectUserName()) && StringUtils.isBlank(vo.getContectUserTel()))) {
                //没有未付款人员
                vo.setAdultNumber(0);
                vo.setAllNumber(0);
                vo.setAllPrice(0.0);
                vo.setBabyNumber(0);
                vo.setChildNumber(0);
                vo.setContectUserName("无");
                vo.setContectUserTel("无");
                vo.setOldNumber(0);
            }

            contectQuery.setAdultNumber(vo.getAdultNumber());
            contectQuery.setAllNumber(vo.getAllNumber());
            contectQuery.setAllPrice(vo.getAllPrice());
            contectQuery.setBabyNumber(vo.getBabyNumber());
            contectQuery.setChildNumber(vo.getChildNumber());
            contectQuery.setContectUserName(vo.getContectUserName());
            contectQuery.setContectUserTel(vo.getContectUserTel());
            contectQuery.setOldNumber(vo.getOldNumber());
            queryList.add(contectQuery);



            //装配审核详情结果类
            query.setAllDoNumber(vo.getAllDoNumber());
            query.setAllOutPrice(vo.getAllOutPrice());
            query.setCarNo(vo.getCarNo());
            query.setDriverSubsidy(vo.getDriverSubsidy());
            query.setFinallyPrice(vo.getFinallyPrice());
            query.setGuideName(vo.getGuideName());
            query.setGuideSubsidy(vo.getGuideSubsidy());
            query.setInPrice(vo.getInPrice());
            query.setLineName(vo.getLineName());
            query.setLunchPrice(vo.getLunchPrice());
            query.setOutDate(vo.getOutDate());
            query.setParkingRatePrice(vo.getParkingRatePrice());
            query.setRentCarPrice(vo.getRentCarPrice());
            query.setSettlementPrice(vo.getSettlementPrice());
            query.setSubstitutionPrice(vo.getSubstitutionPrice());
            query.setTicketName(vo.getTicketName());
            query.setTicketPrice(vo.getTicketPrice());
            query.setTreeAdultNumber(vo.getTreeAdultNumber());
            query.setTreeBabyNumber(vo.getTreeBabyNumber());
            query.setTreeChildNumber(vo.getTreeChildNumber());
            query.setTreeOldNumber(vo.getTreeOldNumber());
            query.setTrueAllNumber(vo.getTrueAllNumber());
            query.setUnpaidNumber(vo.getUnpaidNumber());
            query.setGuideId(vo.getGuestId());

            allNoNumber =allNoNumber + vo.getAllNumber();
            allNoPrice = allNoPrice + vo.getAllPrice();
        }

        query.setQueryList(queryList);
        query.setAllNoNumber(allNoNumber);
        query.setAllNoPrice(allNoPrice);

        return query;
    }

    /**
     * 展示导游的个人记录
     * @param page
     * @param exaType
     * @return
     */
    @Override
    public Page<GuestResultQuery> GuestPage2Me(Page<GuestResultQuery> page, String exaType) {
        if (StringUtils.isBlank(exaType)) {
            //展示所有记录
            return baseMapper.pageGuest2Me(page);
        }

        if (!EXA_DAI.equals(exaType) && !EXA_WEI.equals(exaType) && !EXA_YI.equals(exaType)) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"审核类型输入有误");
        }

        int exa = 0;
        if (EXA_DAI.equals(exaType)) {
            exa = 0;
        }

        if (EXA_YI.equals(exaType)) {
            exa = 1;
        }

        if (EXA_WEI.equals(exaType)) {
            exa = 2;
        }

        return baseMapper.pageGuest2MeByStatus(page,exa);
    }

    /**
     * 财务同意或拒绝审核
     * @param outDate
     * @param lineName
     * @param guestId
     * @param type
     * @return
     */
    @Override
    public ExaResultQuery examineGuestResult(String outDate, String lineName, Long guestId, Integer type) {

        if (type != 1 && type != 2) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"type参数只能是1或2");
        }

        LocalDateTime time = LocalDateTime.now();
        LocalDate date = DateUtil.parseDate(outDate);

        ExaResultQuery query = new ExaResultQuery();
        if (type == 1) {
            //同意
            int result = baseMapper.examineGuestResult(date, lineName, guestId, time);


            if (result <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"操作失败");
            }
            query.setExaResult(1);
            return query;
        }

        //拒绝
        int result = baseMapper.examineResult(date, lineName, guestId, time);


        if (result <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"操作失败");
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
}
