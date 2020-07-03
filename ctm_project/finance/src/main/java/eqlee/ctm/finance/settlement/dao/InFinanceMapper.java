package eqlee.ctm.finance.settlement.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.finance.settlement.entity.Income;
import eqlee.ctm.finance.settlement.entity.bo.FinanceCompanyBo;
import eqlee.ctm.finance.settlement.entity.bo.FinanceCompanyInfoBo;
import eqlee.ctm.finance.settlement.entity.bo.MsgCaiBo;
import eqlee.ctm.finance.settlement.entity.order.OrderQuery;
import eqlee.ctm.finance.settlement.entity.query.ExamineInfoQuery;
import eqlee.ctm.finance.settlement.entity.query.ExamineResultQuery;
import eqlee.ctm.finance.settlement.entity.query.GuestResultQuery;
import eqlee.ctm.finance.settlement.entity.vo.ContectUserNumberVo;
import eqlee.ctm.finance.settlement.entity.vo.ExamineResultVo;
import eqlee.ctm.finance.settlement.entity.vo.OrderDetailedVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/27
 * @Version 1.0
 */
@Component
public interface InFinanceMapper extends IBaseMapper<Income> {

    /**
     * 查询所有代付的订单详情
     * @param Id
     * @return
     */
    List<OrderDetailedVo> queryOrderNumber (Long Id);

    /**
     * 分页查询所有审核数据
     * @param page
     * @param guideName
     * @param type
     * @param outTime
     * @param orderNo
     * @return
     */
    Page<ExamineResultQuery> listPageExa (Page<ExamineResultQuery> page,
                                          @Param("guideName") String guideName,
                                          @Param("type") Integer type,
                                          @Param("outTime") LocalDate outTime,
                                          @Param("orderNo") String orderNo,
                                          @Param("outTimeEnd") LocalDate outTimeEnd);

    /**
     * 展示审核详情
     * @param Id
     * @return
     */
    ExamineResultVo listExamineDetailed(Long Id);

    /**
     * 展示导游个人信息
     * @param page
     * @param Status
     * @param outTime
     * @param lineName
     * @param id
     * @param list
     * @return
     */
    Page<GuestResultQuery> pageGuest2Me (Page<GuestResultQuery> page,
                                         @Param("Status") Integer Status,
                                         @Param("outTime") LocalDate outTime,
                                         @Param("lineName") String lineName,
                                         @Param("id") Long id,
                                         @Param("list") List<String> list);



    /**
     * 标记该订单已完成
     * @param outDate
     * @param id
     * @param dateTime
     */
    int updateIsFinash(@Param("outDate") LocalDate outDate,
                        @Param("id") Long id,
                        @Param("dateTime") LocalDateTime dateTime);

    /**
     * 财务通过审核
     * @param id
     * @param time
     * @param caiName
     * @param remark
     * @return
     */
    int examineGuestResult(@Param("id") Long id,
                            @Param("time") LocalDateTime time,
                           @Param("caiName") String caiName,
                           @Param("remark") String remark);


    /**
     * 财务拒绝审核
     * @param id
     * @param time
     * @return
     */
    int examineResult(@Param("id") Long id,
                      @Param("time") LocalDateTime time,
                      @Param("caiName") String caiName,
                      @Param("remark") String remark);

    /**
     * 修改订单表状态
     * @param id
     * @return
     */
    Integer updateOrder (Long id);


    /**
     * 修改订单表状态为已完成
     * @param id
     * @return
     */
    Integer updateOrderOver (Long id);


    /**
     * 展示所有财务审核的结果
     * @param page
     * @param outDate
     * @param lineName
     * @param guideId
     * @return
     */
    Page<ExamineInfoQuery> pageExamine(Page<ExamineInfoQuery> page,
                                       @Param("outDate") LocalDate outDate,
                                       @Param("lineName") String lineName,
                                       @Param("guideId") Long guideId);

    /**
     * 修改车辆状态
     * @param carNo
     * @return
     */
    Integer updateCarStatus (String carNo);

    /**
     * 月结统计
     * @param page
     * @param start
     * @param end
     * @param companyName
     * @return
     */
    Page<FinanceCompanyBo> pageFinanceCompany (Page<FinanceCompanyBo> page,
                                               @Param("start") LocalDate start,
                                               @Param("end") LocalDate end,
                                               @Param("companyName") String companyName);

    /**
     *    月结统计详情
     * @param page
     * @param lineName
     * @param start
     * @param end
     * @param accountName
     * @return
     */
    Page<FinanceCompanyInfoBo> queryCompanyInfoCount (Page<FinanceCompanyInfoBo> page,
                                                      @Param("lineName") String lineName,
                                                      @Param("start") LocalDate start,
                                                      @Param("end") LocalDate end,
                                                      @Param("accountName") String accountName);

    /**
     * 增加要传递的消息
     * @param toId
     * @param createId
     * @param msg
     * @param msgType
     * @return
     */
    Integer insertMsg (@Param("toId") Long toId,
                       @Param("createId")Long createId,
                       @Param("msg")String msg,
                       @Param("msgType")Integer msgType);

    /**
     * 查询城市
     * @param orderId
     * @return
     */
    OrderQuery queryOrderCity (Long orderId);
}
