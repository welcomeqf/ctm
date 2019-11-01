package eqlee.ctm.apply.orders.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.apply.orders.entity.OrderDetailed;
import eqlee.ctm.apply.orders.entity.Vo.OrderIndexVo;
import eqlee.ctm.apply.orders.entity.bo.OrderDetailedBo;
import eqlee.ctm.apply.orders.entity.query.OrderDetailedQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
     * @param list
     */
    void batchInsertorderDetailed(List<OrderDetailedBo> list);

    /**
     * 查询所有导游人员
     * @param page
     * @param lineName
     * @param outDate
     * @param id
     * @return
     */
    Page<OrderDetailedQuery> pageOrderDetailed(Page<OrderDetailedQuery> page,
                                               @Param("lineName") String lineName,
                                               @Param("outDate") LocalDate outDate,
                                               @Param("id") Long id);


    /**
     * 根据支付类型查询所有导游人员
     * @param page
     * @param lineName
     * @param outDate
     * @param type
     * @param id
     * @return
     */
    Page<OrderDetailedQuery> pageOrderDetailedByType(Page<OrderDetailedQuery> page,
                                               @Param("lineName") String lineName,
                                               @Param("outDate") LocalDate outDate,
                                               @Param("type") Integer type,
                                                     @Param("id") Long id);
}
