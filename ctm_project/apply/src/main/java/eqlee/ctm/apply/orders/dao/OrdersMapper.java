package eqlee.ctm.apply.orders.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.IBaseMapper.IBaseMapper;
import com.yq.utils.DateUtil;
import eqlee.ctm.apply.guider.entity.vo.ApplyVo;
import eqlee.ctm.apply.orders.entity.OrderDetailed;
import eqlee.ctm.apply.orders.entity.Orders;
import eqlee.ctm.apply.orders.entity.Vo.*;
import eqlee.ctm.apply.orders.entity.bo.CarQueryBo;
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
     * 批量插入
     * @param ordersList
     */
    void batchInsertOrders(List<Orders> ordersList);


    /**
     * 通过CreateUserId选择订单
     * @param pager
     * @param CreateUserId
     * @param LineName
     * @param OutDate
     * @return
     */
    Page<OrderIndexVo> selectOrdersByCreateUserId(@Param("page") Page<OrderIndexVo> pager,
                                                  @Param("CreateUserId") Long CreateUserId,
                                                  @Param("LineName")  String LineName,
                                                  @Param("OutDate") LocalDate OutDate);



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
     * @param LineName
     * @param OutDate
     * @param CarNumber
     * @return
     */
     int updateOrdersCarNo(@Param("LineName") String LineName,
                           @Param("OutDate") LocalDate OutDate,
                           @Param("CarNumber") String CarNumber,
                           @Param("Id") Long Id);


    /**
     * 插入车牌号(非公司车辆)
     * @param LineName
     * @param OutDate
     * @param CarNumber
     * @return
     */
    int updateOrdersOutsideCarNo(@Param("LineName") String LineName,
                                 @Param("OutDate") LocalDate OutDate,
                                 @Param("CarNumber") String CarNumber,
                                 @Param("Id") Long Id);


    /**
     * 查询公司的出行状态
     * @param CarNo
     * @return
     */
    CarQueryBo queryCar (String CarNo);

    /**
     * 修改车辆出行状态
     * @param CarNo
     * @return
     */
    int updateCarStatus (@Param("CarNo") String CarNo);


    /**
     * 判断是否是本公司车辆
     * @param CarNo
     * @return
     */
    CarVo isCompanyCar(String CarNo);


    /**
     * 插入车辆
     * @param Id
     * @param CarNumber
     * @param UserId
     */
    void insertCar(@Param("Id") Long Id,
                   @Param("CarNumber") String CarNumber,
                   @Param("UserId") Long UserId);




    /**
     *更新Orders表
     * @param LineName
     * @param OutDate
     * @return
     */
    int updateOrdersInformation(@Param("LineName") String LineName,
                                @Param("OutDate") LocalDate OutDate,
                                @Param("Id") Long Id);


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
     * @param list
     * @return
     */
    int sureChoised(List<ChoisedVo> list);

    /**
     *  接收换人
     * @param Id
     * @param lineName
     * @param outDate
     * @return
     */
    int sureOrders(@Param("Id") Long Id,
                    @Param("lineName") String lineName,
                    @Param("outDate") LocalDate outDate);



    /**
     * 拒绝换人
     * @param choisedList
     * @return
     */
    int denyChoised(List<ChoisedVo> choisedList);


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
     * @param LineName
     * @param OutDate
     * @return
     */
    List<InComeRemerberVo> selectIncomeCount(@Param("LineName") String LineName,
                                             @Param("OutDate") LocalDate OutDate,
                                             @Param("Id") Long Id);
    /**
     * 未付款人信息
     * @param page
     * @return
     */
    Page<UnpaidInformationVo> unpaidInformation(Page<UnpaidInformationVo> page);

    /**
     * 根据报名表的Id查出ApplyVo列表
     * @param id
     * @return
     */
    OrdersVo selectApplyVoList(Long id);


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
}
