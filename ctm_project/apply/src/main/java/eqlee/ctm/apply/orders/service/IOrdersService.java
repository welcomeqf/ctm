package eqlee.ctm.apply.orders.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.guider.entity.vo.ApplyVo;
import eqlee.ctm.apply.orders.entity.Orders;
import eqlee.ctm.apply.orders.entity.Vo.*;
import eqlee.ctm.apply.orders.entity.query.ChangedQuery;
import eqlee.ctm.apply.orders.entity.query.OrderQuery;
import org.apache.ibatis.annotations.Param;

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
    void saveApply(List<LongVo> applyVoList);


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
     * @param LineName
     * @param OutDate
     * @return
     */
    String getCarNumber(String LineName, String OutDate);


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
     * @param LineName
     * @param OutDate
     * @param CarNumber
     */
    void save(String LineName, String OutDate, String CarNumber);


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
     * @param LineName
     * @param OutDate
     * @return
     */
    IncomeVo IncomeCount(String LineName,String OutDate);



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
}
