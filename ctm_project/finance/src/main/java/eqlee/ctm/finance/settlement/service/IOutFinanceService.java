package eqlee.ctm.finance.settlement.service;

import eqlee.ctm.finance.settlement.entity.Outcome;

/**
 * @Author qf
 * @Date 2019/9/27
 * @Version 1.0
 */
public interface IOutFinanceService {


    /**
     * 提交导游消费支出表
     * @param outcome
     */
    void insertOutFinance (Outcome outcome);
}
