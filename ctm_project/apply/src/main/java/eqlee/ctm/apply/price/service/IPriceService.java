package eqlee.ctm.apply.price.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.price.entity.Price;
import eqlee.ctm.apply.price.entity.query.PriceQuery;
import eqlee.ctm.apply.price.entity.vo.PriceVo;

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

    /**
     * 价格设定
     * @param priceVo
     */
    void insertPrice(PriceVo priceVo);

    /**
     * 价格修改(..)
     * @param price
     */
    void updatePrice(Price price);


    /**
     * 分页查询所有价格
     * @param priceQuery
     * @return
     */
    Page<Price> queryPriceToPage(PriceQuery priceQuery);
}
