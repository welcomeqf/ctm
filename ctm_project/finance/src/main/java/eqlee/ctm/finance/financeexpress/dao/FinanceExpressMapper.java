package eqlee.ctm.finance.financeexpress.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.finance.financeexpress.entity.AllCountVo;
import eqlee.ctm.finance.financeexpress.entity.CountVo;
import eqlee.ctm.finance.financeexpress.entity.FinanceExpressCountVo;
import eqlee.ctm.finance.financeexpress.entity.CashCountVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @Author Claire
 * @Date 2019/10/8 0008
 * @Version 1.0
 */
@Component
public interface FinanceExpressMapper {

    /**
     * 查询现结财务结算列表
     * @param page
     * @param year
     * @param month
     * @return
     */
    Page<CashCountVo> queryfinancecount(Page<CashCountVo> page,
                                        @Param("year") String year,
                                        @Param("month") String month,
                                        @Param("companyName") String companyName);


    /**
     * 查询财务代收列表
     * @param page
     * @param year
     * @param month
     * @param guideName
     * @return
     */
    Page<FinanceExpressCountVo>  queryfinancecollection(Page<FinanceExpressCountVo> page,
                                                        @Param("year") String year,
                                                        @Param("month") String month,
                                                        @Param("guideName") String guideName);




    /**
     * 查询财务结算列表
     * @param page
     * @param year
     * @param month
     * @return
     */
    Page<CountVo> getfinancecount(Page<CountVo> page,
                                  @Param("year") String year,
                                  @Param("month") String month,
                                  @Param("companyName") String companyName);



    /**
     * 查询财务总结算列表
     * @param page
     * @param year
     * @param month
     * @param companyName
     * @return
     */
    Page<AllCountVo> getAllfinancecount(Page<AllCountVo> page,
                                        @Param("year") String year,
                                        @Param("month") String month,
                                        @Param("companyName") String companyName);
}
