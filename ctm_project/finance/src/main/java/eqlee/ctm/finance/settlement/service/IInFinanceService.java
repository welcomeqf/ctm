package eqlee.ctm.finance.settlement.service;

import eqlee.ctm.finance.settlement.entity.vo.FinanceVo;

/**
 * @Author qf
 * @Date 2019/9/27
 * @Version 1.0
 */
public interface IInFinanceService {

    /**
     * 提交导游消费情况
     * @param vo
     */
    void insertFinance (FinanceVo vo);
}
