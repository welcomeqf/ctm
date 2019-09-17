package eqlee.ctm.apply.price.service;

import eqlee.ctm.apply.price.entity.Price;

import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/9/17
 * @Version 1.0
 */
public interface IPriceService {

    /**
     * 根据出行日期查询价格
     * @param OutDate
     * @return
     */
    Price queryPrice(LocalDateTime OutDate);
}
