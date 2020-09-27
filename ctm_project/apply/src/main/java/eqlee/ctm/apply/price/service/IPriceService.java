package eqlee.ctm.apply.price.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.entry.entity.bo.PriceBo;
import eqlee.ctm.apply.price.entity.Price;
import eqlee.ctm.apply.price.entity.query.PriceQuery;
import eqlee.ctm.apply.price.entity.vo.PriceSeacherVo;
import eqlee.ctm.apply.price.entity.vo.PriceSelectVo;
import eqlee.ctm.apply.price.entity.vo.PriceUpdateVo;
import eqlee.ctm.apply.price.entity.vo.PriceVo;

import java.time.LocalDateTime;
import java.util.List;

import java.time.LocalDate;
import java.util.Map;

/**
 * @Author qf
 * @Date 2019/9/17
 * @Version 1.0
 */
public interface IPriceService {

    /**
     * 根据出行日期,线路名查询价格
     * @param OutDate
     * @param LineName
     * @return
     */
    Price queryPrice(LocalDate OutDate, String LineName);

    /**
     * 价格设定
     * 对一段时间增加或修改
     * （不加星期约束）
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
     * @param priceBo
     */
    void batchUpdatePrice(PriceBo priceBo);


    /**
     * 根据出行时间或者线路名查看价格序列(模糊查询)
     * @param numberType
     * @param outDate
     * @param lineId
     * @return
     */
    Map<String,Object> queryPricePageByFilter(Integer numberType, String outDate, Long lineId);


    /**
     * 根据日期和线路名查询价格
     * @param Outdate
     * @param LineName
     * @return
     */
    Price queryPriceByTimeAndLineName(LocalDate Outdate,String LineName);


    /**
     * 查询一条数据
     * @param id
     * @return
     */
    Price queryPriceById (Long id);

    /**
     * 删除价格
     * @param id
     */
    void deletePriceById (Long id);

    /**
     * 跟剧线路ID查询价格
     * @param lineId
     * @return
     */
    List<Price> queryPriceByLineNameId (Long lineId);


    /**
     * 删除价格 根据线路和出行时间
     * @param lineId
     * @param OutDate
     * @param OutDateEnd
     */
    void deletePriceByDay (Long lineId,String OutDate,String OutDateEnd);

}
