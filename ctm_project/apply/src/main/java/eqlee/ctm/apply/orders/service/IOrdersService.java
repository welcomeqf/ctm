package eqlee.ctm.apply.orders.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.guider.entity.vo.ApplyVo;
import eqlee.ctm.apply.orders.entity.Orders;
import eqlee.ctm.apply.orders.entity.Vo.*;
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
     * 查看导游已选人情况
     *
     * @param page
     * @param LineName
     * @param OutDate
     * @return
     */
    Page<OrderIndexVo> ChoisedIndex(Page<OrderIndexVo> page, String LineName, String OutDate);


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
     *
     * @param OutDate
     * @param CarNumber
     */
    void save(String OutDate, String CarNumber);


    /**
     * 修改公司状态
     * @param carNo
     */
    void updateCarStatus (String carNo);


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
     * 导游收入统计
     * @param outDate
     * @return
     */
    IncomeVo IncomeCount(String outDate);



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
}
