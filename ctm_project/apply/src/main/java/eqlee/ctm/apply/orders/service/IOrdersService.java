package eqlee.ctm.apply.orders.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.guider.entity.vo.ApplyVo;
import eqlee.ctm.apply.orders.entity.Orders;
import eqlee.ctm.apply.orders.entity.Vo.*;
import eqlee.ctm.apply.orders.entity.bo.CancelBo;
import eqlee.ctm.apply.orders.entity.bo.IdBo;
import eqlee.ctm.apply.orders.entity.query.ChangedQuery;
import eqlee.ctm.apply.orders.entity.query.OrderDetailedFainllyQuery;
import eqlee.ctm.apply.orders.entity.query.OrderQuery;
import eqlee.ctm.apply.orders.entity.query.OrdersNoCountInfoQuery;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @Author Claire
 * @Date 2019/9/24 0024
 * @Version 1.0
 */
public interface IOrdersService {
    /**
     * 导游选人后保存
     *
     * @param applyVoList
     */
    Long saveApply(List<LongVo> applyVoList);


    /**
     * 由线路名，出发时间，导游ID得到车牌号
     * @param id
     * @return
     */
    OrderDetailedFainllyQuery getCarNumber(Long id);


    /**
     * 导游换人
     * @param orderIndexVos
     * @param Id
     * @param lineName
     * @param outDate
     */
    void updateApply(List<OrderIndexVo> orderIndexVos, Long Id, String lineName, String outDate);


    /**
     * 保存车辆信息
     * @param carId
     * @param orderId
     * @param carNo
     */
    void save(Long carId, Long orderId, String carNo);


    /**
     * 导游换人消息展示
     * @param page
     * @return
     */
    Page<ChangedVo> waiteChangeIndex(Page<ChangedVo> page);


    /**
     * 接受换人
     * @param id
     * @param outDate
     * @param lineName
     */
    void sureChoised(Long id, String outDate, String lineName);


    /**
     * 拒绝换人
     * @param id
     * @param outDate
     * @param lineName
     */
    void denyChoised(Long id, String outDate, String lineName);


    /**
     * 换人列表查询
     * @param page
     * @param type
     * @return
     */
    Page<ChangedQuery> denyChoisedindex(Page<ChangedQuery> page, Integer type);



    /**
     *  导游收入统计
     * @param orderId
     * @return
     */
    IncomeVo incomeCount(Long orderId);



    /**
     * 未付款人列表
     * @param page
     * @return
     */
    Page<UnpaidInformationVo> unpaidInformation(Page<UnpaidInformationVo> page);

    /**
     * 查询日期和线路
     * @return
     */
    List<OrderQuery> queryLineAndTime ();

    /**
     * 查询所有未结算的条数
     * @return
     */
    Integer queryAllNoCount ();

    /**
     * 查看自己的数据
     * 查询所有未结算的具体的信息
     * @return
     */
    List<OrdersNoCountInfoQuery>  queryAllNoCountInfo ();

    /**
     * 根据线路和时间查询id
     * @param outDate
     * @param lineName
     * @return
     */
    IdBo queryOrderId (String outDate, String lineName);

    /**
     * 查询一条记录
     * @param id
     * @return
     */
    Orders queryOne (Long id);

    /**
     * 修改车辆出行状态
     */
    void upCarStatus ();

    /**
     * 查询ID
     * @param outDate
     * @return
     */
    IdBo queryId (String outDate);

    /**
     * 修改订单主表的信息
     * @param bo
     */
    void updateOrderCancel (CancelBo bo);

    /**
     * 查询账单状态 3待交账 状态(0--已提交待审核1--审核成功2--审核失败)
     * @param id
     * @return
     */
    IncomeVo queryOrdersStatus(Long id);
}
