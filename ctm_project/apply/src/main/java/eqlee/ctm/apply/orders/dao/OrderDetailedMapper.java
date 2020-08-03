package eqlee.ctm.apply.orders.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.apply.entry.entity.query.ApplyUpdateInfo;
import eqlee.ctm.apply.orders.entity.OrderDetailed;
import eqlee.ctm.apply.orders.entity.Vo.OrderIndexVo;
import eqlee.ctm.apply.orders.entity.bo.OrderBo;
import eqlee.ctm.apply.orders.entity.bo.OrderDetailedBo;
import eqlee.ctm.apply.orders.entity.query.OrderDetailedQuery;
import eqlee.ctm.apply.orders.entity.query.OrderFinanceQuery;
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


    /**
     * 交账结果
     * @param page
     * @param start
     * @param end
     * @param lineName
     * @param guideName
     * @param id
     * @return
     */
    Page<OrderBo> pageOrder2(Page<OrderBo> page,
                            @Param("start") LocalDate start,
                            @Param("end") LocalDate end,
                            @Param("lineName") String lineName,
                            @Param("guideName") String guideName,
                            @Param("id") Long id,
                             @Param("inStatus") Integer inStatus);


    /**
     * 查询财务信息
     * @param orderId
     * @return
     */
    List<OrderFinanceQuery>  queryInOutInfo(Long orderId);

    /**
     * 待交账账单取消
     * @param deptId
     * @return
     */
    void CancelWaitSubmitOrder(@Param("orderId") Long deptId);

    /**
     * 批量更新报名表短信通知状态
     * @param list
     * @return
     */
    void updateApplySendStatu(List<ApplyUpdateInfo> list);
}
