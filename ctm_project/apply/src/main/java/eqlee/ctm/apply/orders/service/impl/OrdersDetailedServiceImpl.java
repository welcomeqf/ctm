package eqlee.ctm.apply.orders.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import eqlee.ctm.apply.orders.dao.OrderDetailedMapper;
import eqlee.ctm.apply.orders.entity.OrderDetailed;
import eqlee.ctm.apply.orders.service.IOrdersDetailedService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/24 0024
 * @Version 1.0
 */
@Service
public class OrdersDetailedServiceImpl extends ServiceImpl<OrderDetailedMapper, OrderDetailed> implements IOrdersDetailedService {

    @Override
    public void batchInsertorderDetailed(List<OrderDetailed> orderDetailedList) {
        baseMapper.batchInsertorderDetailed(orderDetailedList);
    }
}
