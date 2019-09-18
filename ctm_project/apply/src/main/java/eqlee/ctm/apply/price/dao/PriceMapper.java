package eqlee.ctm.apply.price.dao;

import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.apply.price.entity.Price;
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
}
