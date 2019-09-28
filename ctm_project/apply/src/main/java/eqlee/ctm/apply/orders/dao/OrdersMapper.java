package eqlee.ctm.apply.orders.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.IBaseMapper.IBaseMapper;
import com.yq.utils.DateUtil;
import eqlee.ctm.apply.orders.entity.OrderDetailed;
import eqlee.ctm.apply.orders.entity.Orders;
import eqlee.ctm.apply.orders.entity.Vo.*;
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
                              @Param("Id") String Id);


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



    Page<VisitorInformation> selectVisitor(@Param("page") Page<VisitorInformation> page,
                                            @Param("OutDate") LocalDate OutDate,
                                            @Param("ContactName") String ContactName,
                                            @Param("LineName") String LineName);


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
     * @return
     */
    List<ChangedVo> waiteChangeIndex(String Id);


    /**
     * 接受换人
     * @param choisedList
     * @return
     */
    int sureChoised(List<ChoisedVo> choisedList);



    /**
     * 拒绝换人
     * @param choisedList
     * @return
     */
    int denyChoised(List<ChoisedVo> choisedList);


    /**
     * 换人拒绝列表查询
     * @param LineName
     * @param OutDate
     * @return
     */
    List<ChangedVo> denyChoisedindex(@Param("LineName") String LineName,
                                     @Param("OutDate") LocalDate OutDate,
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
     * @param ContactTel
     * @return
     */
    UnpaidInformationVo unpaidInformation(@Param("ContactTel") String ContactTel,
                                          @Param("LineName") String LineName,
                                          @Param("OutDate") LocalDate OutDate);
}
