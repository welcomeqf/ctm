package eqlee.ctm.apply.orders.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.apply.orders.entity.OrderDetailed;
import eqlee.ctm.apply.orders.entity.Vo.OrderIndexVo;
import eqlee.ctm.apply.orders.entity.bo.OrderBo;
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
     * @param payType
     * @param id
     * @return
     */
    List<OrderDetailedQuery> pageOrderDetailed(@Param("payType") Integer payType,
                                               @Param("id") Long id);

    /**
     * 查询导游主页
     * @param page
     * @param start
     * @param end
     * @param lineName
     * @param region
     * @param id
     * @return
     */
    Page<OrderBo> pageOrder(Page<OrderBo> page,
                            @Param("start") LocalDate start,
                            @Param("end") LocalDate end,
                            @Param("lineName") String lineName,
                            @Param("region") String region,
                            @Param("id") Long id);
}
