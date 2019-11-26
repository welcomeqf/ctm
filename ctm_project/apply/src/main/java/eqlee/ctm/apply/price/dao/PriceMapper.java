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
     * 查询当前月的所有价格
     * @param start
     * @param end
     * @param lineId
     * @return
     */
    List<PriceSelectVo> selectPriceByFilter(@Param("start") LocalDate start,
                                            @Param("end") LocalDate end,
                                            @Param("lineId") Long lineId);




    /**
     * 查询一条价格记录
     * @param Id
     * @return
     */
    PriceSeacherVo queryOne (Long Id);

    /**
     * 查询时间段的价格
     * @param lineId
     * @param startDate
     * @param endDate
     * @return
     */
    List<Price> queryAllPriceByTime (@Param("lineId") Long lineId,
                               @Param("startDate") LocalDate startDate,
                               @Param("endDate") LocalDate endDate);
}
