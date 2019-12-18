package eqlee.ctm.apply.orders.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.line.entity.vo.ResultVo;
import eqlee.ctm.apply.orders.entity.OrderDetailed;
import eqlee.ctm.apply.orders.entity.bo.IdBo;
import eqlee.ctm.apply.orders.entity.bo.OrderBo;
import eqlee.ctm.apply.orders.entity.bo.OrderDetailedBo;
import eqlee.ctm.apply.orders.entity.query.OrderDetailedQuery;
import eqlee.ctm.apply.orders.entity.query.OrdersNoCountInfoQuery;
import eqlee.ctm.apply.orders.entity.query.OrdersNoCountQuery;

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
     * @param id
     * @param payType
     * @return
     */
    List<OrderDetailedQuery> pageOrderDetailed2Type (Long id, Integer payType);

    /**
     * 管理分配首页
     * 查询导游的主页以及历史记录
     * @param page
     * @param startDate
     * @param endDate
     * @param lineName
     * @param region
     * @return
     */
    Page<OrderBo> pageOrder (Page<OrderBo> page,String startDate, String endDate, String lineName, String region);

}
