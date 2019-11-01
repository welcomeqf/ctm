package eqlee.ctm.apply.orders.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.orders.entity.OrderDetailed;
import eqlee.ctm.apply.orders.entity.bo.OrderDetailedBo;
import eqlee.ctm.apply.orders.entity.query.OrderDetailedQuery;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/24 0024
 * @Version 1.0
 */
public interface IOrdersDetailedService {

    /**
     * 导游选人后生成订单
     * @param list
     */
    void batchInsertorderDetailed(List<OrderDetailedBo> list);


    /**
     * 查询导游人员表
     * @param page
     * @param payType
     * @param lineName
     * @param outDate
     * @return
     */
    Page<OrderDetailedQuery> pageOrderDetailed2Type (Page<OrderDetailedQuery> page, String payType, String lineName, String outDate);
}
