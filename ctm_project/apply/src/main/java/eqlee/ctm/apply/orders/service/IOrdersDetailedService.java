package eqlee.ctm.apply.orders.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.entry.entity.query.ApplyUpdateInfo;
import eqlee.ctm.apply.guider.entity.vo.GuiderIdParamVo;
import eqlee.ctm.apply.guider.entity.vo.GuiderVo;
import eqlee.ctm.apply.line.entity.vo.ResultVo;
import eqlee.ctm.apply.orders.entity.OrderDetailed;
import eqlee.ctm.apply.orders.entity.bo.IdBo;
import eqlee.ctm.apply.orders.entity.bo.OrderBo;
import eqlee.ctm.apply.orders.entity.bo.OrderDetailedBo;
import eqlee.ctm.apply.orders.entity.query.*;

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



    /**
     * 交账结果
     * @param page
     * @param startDate
     * @param endDate
     * @param lineName
     * @param guideName
     * @return
     */
    Page<OrderBo> pageOrder2 (Page<OrderBo> page,String startDate, String endDate, String lineName, String guideName, Integer inStatus);


    /**
     * 根据ID查询信息
     * @param id
     * @return
     */
    OrderDetailed queryById (Long id);

    /**
     * 查询支出信息以及财务信息
     * @param orderId
     * @return
     */
    List<OrderFinanceQuery> queryInOutInfo (Long orderId);

    /**
     * 提交取消操作
     * @param applyId
     * @param cancelStatus
     */
    void updateCancelStatus (Long applyId, Integer cancelStatus);

    /**
     * 根据申请表id获取导游选人详情
     * @param ids
     * @return
     */
    List<OrderDetailed> queryByApplyId (List<Long> ids);

    /**
     * 管理员取消待交账账单
     * @param id
     * @return
     */
    void cancelOrders(Long id);

    /**
     * 批量更新报名表短信通知状态
     * @param applyIds
     * @return
     */
    void updateApplySendStatu(List<ApplyUpdateInfo> applyIds);
}
