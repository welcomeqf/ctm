package eqlee.ctm.apply.orders.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.IBaseMapper.IBaseMapper;
import com.yq.utils.DateUtil;
import eqlee.ctm.apply.guider.entity.vo.ApplyVo;
import eqlee.ctm.apply.orders.entity.OrderDetailed;
import eqlee.ctm.apply.orders.entity.Orders;
import eqlee.ctm.apply.orders.entity.Vo.*;
import eqlee.ctm.apply.orders.entity.bo.CarQueryBo;
import eqlee.ctm.apply.orders.entity.bo.OrdersMonthBo;
import eqlee.ctm.apply.orders.entity.query.ChangedQuery;
import eqlee.ctm.apply.orders.entity.query.OrderContectQuery;
import eqlee.ctm.apply.price.entity.vo.PriceVo;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
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
public interface OrdersMapper extends IBaseMapper<Orders> {

    /**
     * 修改订单和订单详情的状态
     * @param orderIndexVos
     * @param Id
     * @return
     */
     int updateOrderDetailed(@Param("list") List<OrderIndexVo> orderIndexVos,
                             @Param("Id") Long Id);


    /**
     * 插入车牌号(本公司车辆)
     * @param orderId
     * @param carNo
     * @return
     */
     int updateOrdersCarNo(@Param("orderId") Long orderId,
                           @Param("carNo") String carNo);


    /**
     * 插入车牌号(非公司车辆)
     * @param orderId
     * @param carNo
     * @return
     */
    int updateOrdersOutsideCarNo(@Param("orderId") Long orderId,
                                 @Param("carNo") String carNo);


    /**
     * 查询公司的出行状态
     * @param id
     * @return
     */
    CarQueryBo queryCar (Long id);



    /**
     * 查询导游需要确认换人的列表
     * @param page
     * @param Id
     * @return
     */
    Page<ChangedVo> waiteChangeIndex(Page<ChangedVo> page,
                                     @Param("Id") Long Id);


    /**
     * 接受换人
     * @param id
     * @param orderId
     * @return
     */
    int sureChoised(@Param("id") Long id,
                    @Param("orderId") Long orderId);


    /**
     * 拒绝换人
     * @param id
     * @return
     */
    int denyChoised(Long id);


    /**
     * 我推给别人的
     * @param page
     * @param Id
     * @return
     */
    Page<ChangedQuery> denyChoisedindex(Page<ChangedQuery> page,
                                        @Param("Id") Long Id);


    /**
     * 别人推给我的~
     * @param page
     * @param Id
     * @return
     */
    Page<ChangedQuery> withChoiseIndex(Page<ChangedQuery> page,
                                        @Param("Id") Long Id);

    /**
     * 导游收入统计
     * @param orderId
     * @return
     */
    IncomeVo selectIncomeCount(Long orderId);
    /**
     * 未付款人信息
     * @param page
     * @return
     */
    Page<UnpaidInformationVo> unpaidInformation(Page<UnpaidInformationVo> page);


    /**
     * 查询面收人员名单
     * @param LineName
     * @param OutDate
     * @param Id
     * @return
     */
    List<OrderContectQuery> listContect(@Param("LineName") String LineName,
                                        @Param("OutDate") LocalDate OutDate,
                                        @Param("Id") Long Id);

    /**
     * 查询月结的总金额
     * @param lineName
     * @param outDate
     * @param id
     * @return
     */
    OrdersMonthBo queryMonthPrice (@Param("lineName") String lineName,
                                   @Param("outDate") LocalDate outDate,
                                   @Param("id") Long id);


    /**
     * 查询审核状态
     * @param orderId
     * @return
     */
    IncomeVo queryStatus (Long orderId);


    /**
     * 批量修改车辆出行状态
     * @param list
     * @return
     */
    Integer upCarStatus (List<Orders> list);

}
