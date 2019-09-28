package eqlee.ctm.apply.orders.dao;

import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.apply.orders.entity.OrderDetailed;
import eqlee.ctm.apply.orders.entity.Vo.OrderIndexVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/24 0024
 * @Version 1.0
 */
@Component
public interface OrderDetailedMapper extends IBaseMapper<OrderDetailed> {
    /**
     * 批量插入订单详情
     * @param orderDetailedList
     */
    void batchInsertorderDetailed(List<OrderDetailed> orderDetailedList);

}
