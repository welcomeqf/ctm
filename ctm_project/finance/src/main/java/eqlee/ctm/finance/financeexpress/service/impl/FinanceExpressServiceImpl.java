package eqlee.ctm.finance.financeexpress.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.utils.StringUtils;
import eqlee.ctm.finance.financeexpress.dao.FinanceExpressMapper;
import eqlee.ctm.finance.financeexpress.entity.AllCountVo;
import eqlee.ctm.finance.financeexpress.entity.CountVo;
import eqlee.ctm.finance.financeexpress.entity.FinanceExpressCountVo;
import eqlee.ctm.finance.financeexpress.entity.CashCountVo;
import eqlee.ctm.finance.financeexpress.service.IFinanceExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * @Author Claire
 * @Date 2019/10/8 0008
 * @Version 1.0
 */
@Service
public class FinanceExpressServiceImpl implements IFinanceExpressService {
    @Autowired
    private FinanceExpressMapper financeExpressMapper;



    /**
     * 查询现结财务结算列表
     * @param current
     * @param size
     * @param time
     * @param companyName
     * @return
     */
    @Override
    public Page<CashCountVo> queryfinancecount(Integer current, Integer size, String time, String companyName) {
        Page<CashCountVo> page = new Page<CashCountVo>();
        page.setSize(size);
        page.setCurrent(current);
        LocalDate today = LocalDate.now();
        String year = String.valueOf(today.getYear());
        String month = String.valueOf(today.minusMonths(1).getMonthValue());
        if(StringUtils.isNotBlank(time)) {
            year = time.substring(0, 4);
            month = time.substring(5, 6);
        }
       return financeExpressMapper.queryfinancecount(page,year,month,companyName);
    }



    /**
     * 查询财务代收列表
     * @param current
     * @param size
     * @param time
     * @param guideName
     * @return
     */
    @Override
    public Page<FinanceExpressCountVo> queryfinancecollection(Integer current, Integer size, String time, String guideName) {
        Page<FinanceExpressCountVo> page = new Page<FinanceExpressCountVo>();
        page.setSize(size);
        page.setCurrent(current);
        LocalDate today = LocalDate.now();
        String year = String.valueOf(today.getYear());
        String month = String.valueOf(today.minusMonths(1).getMonthValue());
        if(StringUtils.isNotBlank(time)) {
            year = time.substring(0, 4);
            month = time.substring(5, 6);
        }
        return financeExpressMapper.queryfinancecollection(page,year,month,guideName);
    }


    /**
     * 查询财务结算列表
     * @param current
     * @param size
     * @param time
     * @param companyName
     * @return
     */
    @Override
    public Page<CountVo> getfinancecount(Integer current, Integer size, String time, String companyName) {
        Page<CountVo> page = new Page<CountVo>();
        page.setSize(size);
        page.setCurrent(current);
        LocalDate today = LocalDate.now();
        String year = String.valueOf(today.getYear());
        String month = String.valueOf(today.minusMonths(1).getMonthValue());
        if(StringUtils.isNotBlank(time)) {
            year = time.substring(0, 4);
            month = time.substring(5, 6);
        }
        return financeExpressMapper.getfinancecount(page,year,month,companyName);
    }


    /**
     * 查询财务总账单列表
     * @param current
     * @param size
     * @param time
     * @param companyName
     * @return
     */
    @Override
    public Page<AllCountVo> getAllfinancecount(Integer current, Integer size, String time, String companyName) {
        Page<AllCountVo> page = new Page<AllCountVo>();
        page.setSize(size);
        page.setCurrent(current);
        LocalDate today = LocalDate.now();
        String year = String.valueOf(today.getYear());
        String month = String.valueOf(today.minusMonths(1).getMonthValue());
        if(StringUtils.isNotBlank(time)) {
            year = time.substring(0, 4);
            month = time.substring(5, 6);
        }
        return financeExpressMapper.getAllfinancecount(page,year,month,companyName);
    }
}
