package eqlee.ctm.apply.price.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.apply.price.entity.Price;
import eqlee.ctm.apply.price.entity.query.PriceQuery;
import eqlee.ctm.apply.price.entity.vo.PriceAllUpdateVo;
import eqlee.ctm.apply.price.entity.vo.PriceSeacherVo;
import eqlee.ctm.apply.price.entity.vo.PriceSelectVo;
import eqlee.ctm.apply.price.entity.vo.PriceVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/17
 * @Version 1.0
 */
@Component
public interface PriceMapper extends IBaseMapper<Price> {

    /**
     * 批量插入
     * @param prices
     * @return
     */
    Integer allInsertPrice(List<Price> prices);


    /**
     * 批量修改
     * @param list
     * @return
     */
    Integer batchUpdatePrice(List<PriceAllUpdateVo> list);


    /**
     * 由出行时间和线路名获取Price列表
     * @param page
     * @param OutDate
     * @param LineName
     * @return
     */
    Page<PriceSelectVo> selectPriceByFilter(Page<PriceSelectVo> page,
                                            @Param("OutDate") LocalDate OutDate,
                                            @Param("LineName") String LineName);


    /**
     * 由出行时间获取Price列表
     * @param page
     * @param OutDate
     * @return
     */
    Page<PriceSelectVo> selectPriceByOutDate(Page<PriceSelectVo> page,
                                            @Param("OutDate") LocalDate OutDate);

    /**
     * 由线路名获取Price列表
     * @param page
     * @param LineName
     * @return
     */
    Page<PriceSelectVo> selectPriceByLineName(Page<PriceSelectVo> page,
                                            @Param("LineName") String LineName);

    /**
     * 获取Price列表
     * @param page
     * @return
     */
    Page<PriceSelectVo> selectPrice(Page<PriceSelectVo> page);

    /**
     * 查询一条价格记录
     * @param Id
     * @return
     */
    PriceSeacherVo queryOne (Long Id);
}
