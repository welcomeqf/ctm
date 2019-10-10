package eqlee.ctm.apply.price.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.price.entity.Price;
import eqlee.ctm.apply.price.entity.query.PriceQuery;
import eqlee.ctm.apply.price.entity.vo.PriceSeacherVo;
import eqlee.ctm.apply.price.entity.vo.PriceSelectVo;
import eqlee.ctm.apply.price.entity.vo.PriceUpdateVo;
import eqlee.ctm.apply.price.entity.vo.PriceVo;

import java.time.LocalDateTime;
import java.util.List;

import java.time.LocalDate;
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
    Price queryPrice(LocalDate OutDate);

    /**
     * 价格设定
     * @param priceVo
     */
    void insertPrice(PriceVo priceVo);

    /**
     * 单条价格修改
     * @param price
     */
    void updatePrice(PriceUpdateVo price);


    /**
     * 批量修改价格
     * @param priceVo
     */
    void batchUpdatePrice(PriceVo priceVo);


    /**
     * 根据出行时间或者线路名查看价格序列(模糊查询)
     *
     * @param priceQuery
     * @return
     */
    Page<PriceSelectVo> queryPricePageByFilter(PriceQuery priceQuery);


    /**
     * 根据日期和线路名查询价格
     * @param Outdate
     * @param LineName
     * @return
     */
    Price queryPriceByTimeAndLineName(LocalDate Outdate,String LineName);


    /**
     * 查询一条数据
     * @param Id
     * @return
     */
    PriceSeacherVo queryPriceById (Long Id);

}
