package eqlee.ctm.apply.orders.service;

import eqlee.ctm.apply.orders.entity.OrderDetailed;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/24 0024
 * @Version 1.0
 */
public interface IOrdersDetailedService {
    void batchInsertorderDetailed(List<OrderDetailed> orderDetailedList);
}
