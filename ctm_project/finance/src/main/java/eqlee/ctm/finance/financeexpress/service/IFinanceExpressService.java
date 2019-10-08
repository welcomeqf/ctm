package eqlee.ctm.finance.financeexpress.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.finance.financeexpress.entity.AllCountVo;
import eqlee.ctm.finance.financeexpress.entity.CountVo;
import eqlee.ctm.finance.financeexpress.entity.FinanceExpressCountVo;
import eqlee.ctm.finance.financeexpress.entity.CashCountVo;

/**
 * @Author Claire
 * @Date 2019/10/8 0008
 * @Version 1.0
 */
public interface IFinanceExpressService {
    /**
     * 查询现结财务结算列表
     * @param current
     * @param size
     * @param time
     * @param companyName
     * @return
     */
    Page<CashCountVo> queryfinancecount(Integer current, Integer size, String time, String companyName);


    /**
     * 查询财务代收列表
     * @param current
     * @param size
     * @param time
     * @param guideName
     * @return
     */
    Page<FinanceExpressCountVo> queryfinancecollection(Integer current, Integer size, String time, String guideName);



    /**
     * 查询财务结算列表
     * @param current
     * @param size
     * @param time
     * @param companyName
     * @return
     */
    Page<CountVo> getfinancecount(Integer current, Integer size, String time, String companyName);



    /**
     * 查询财务总账单列表
     * @param current
     * @param size
     * @param time
     * @param companyName
     * @return
     */
    Page<AllCountVo> getAllfinancecount(Integer current, Integer size, String time, String companyName);
}
