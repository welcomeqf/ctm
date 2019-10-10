package eqlee.ctm.apply.price.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.apply.price.entity.Price;
import eqlee.ctm.apply.price.entity.query.PriceQuery;
import eqlee.ctm.apply.price.entity.vo.PriceSeacherVo;
import eqlee.ctm.apply.price.entity.vo.PriceSelectVo;
import eqlee.ctm.apply.price.entity.vo.PriceVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

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
     */
    Integer batchupdatePrice(List<Price> prices);


    /**
     * 由出行时间和线路名获取Price列表
     * @param priceQuery
     * @return
     */
    Page<PriceSelectVo> selectPriceByFilter(Page<PriceVo> page,
                                            @Param("priceQuery") PriceQuery priceQuery);



    PriceSeacherVo queryOne (Long Id);
}
