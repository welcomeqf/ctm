package eqlee.ctm.apply.orders.dao;

import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.apply.orders.entity.OrderDetailed;
import eqlee.ctm.apply.orders.entity.Orders;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/24 0024
 * @Version 1.0
 */
@Component
public interface OrdersMapper extends IBaseMapper<Orders> {
    void batchInsertOrders(List<Orders> ordersList);
}
