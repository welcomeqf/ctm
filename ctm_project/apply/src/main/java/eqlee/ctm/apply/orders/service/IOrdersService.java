package eqlee.ctm.apply.orders.service;

import eqlee.ctm.apply.guider.entity.vo.ApplyVo;
import eqlee.ctm.apply.orders.entity.Vo.OrdersVo;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/24 0024
 * @Version 1.0
 */
public interface IOrdersService {
    void saveApply(List<OrdersVo> applyVoList);
}
