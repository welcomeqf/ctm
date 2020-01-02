package eqlee.ctm.finance.settlement.service;

import eqlee.ctm.finance.settlement.entity.Number;

/**
 * @Author qf
 * @Date 2019/9/27
 * @Version 1.0
 */
public interface INumberService {

    /**
     * 增加未付款代收的人数信息
     * @param number
     */
    void insertNumber (Number number);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Number queryById (Long id);

    /**
     * 修改
     * @param number
     */
    void updateNumber (Number number);
}
