package eqlee.ctm.finance.settlement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.utils.DateUtil;
import com.yq.utils.IdGenerator;
import eqlee.ctm.finance.exception.ApplicationException;
import eqlee.ctm.finance.jwt.contain.LocalUser;
import eqlee.ctm.finance.jwt.entity.UserLoginQuery;
import eqlee.ctm.finance.settlement.dao.InFinanceMapper;
import eqlee.ctm.finance.settlement.entity.Income;
import eqlee.ctm.finance.settlement.entity.Number;
import eqlee.ctm.finance.settlement.entity.NumberDetailed;
import eqlee.ctm.finance.settlement.entity.Outcome;
import eqlee.ctm.finance.settlement.entity.vo.ContectUserNumberVo;
import eqlee.ctm.finance.settlement.entity.vo.ContectUserVo;
import eqlee.ctm.finance.settlement.entity.vo.FinanceVo;
import eqlee.ctm.finance.settlement.service.IInFinanceService;
import eqlee.ctm.finance.settlement.service.INumberDetailedService;
import eqlee.ctm.finance.settlement.service.INumberService;
import eqlee.ctm.finance.settlement.service.IOutFinanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        income.setGuideName(user.getCName());
        income.setLineName(vo.getLineName());
        //加入到人数表以及人数明细表
        Number number = new Number();
        Long id = idGenerator.getNumberId();
        number.setId(id);
        number.setCreateUserId(user.getId());
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
    }
}
