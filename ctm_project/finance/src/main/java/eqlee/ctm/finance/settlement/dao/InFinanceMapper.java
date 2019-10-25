package eqlee.ctm.finance.settlement.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.finance.settlement.entity.Income;
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
     * @return
     */
    Page<ExamineResultQuery> listExamine2Page(Page<ExamineResultQuery> page);

    /**
     * 未审核的财务数据
     * @param page
     * @return
     */
    Page<ExamineResultQuery> listExamine2No (Page<ExamineResultQuery> page);

    /**
     * 已审核的财务数据
     * @param page
     * @return
     */
    Page<ExamineResultQuery> listExamine2Do (Page<ExamineResultQuery> page);


    /**
     * 通过导游名字模糊查询未审核数据
     * @param page
     * @param guestName
     * @return
     */
    Page<ExamineResultQuery> listExamine2NoByGuestName(Page<ExamineResultQuery> page,
                                                         @Param("guestName") String guestName);

    /**
     * 通过导游名字模糊查询已审核数据
     * @param page
     * @param guestName
     * @return
     */
    Page<ExamineResultQuery> listExamine2DoByGuestName(Page<ExamineResultQuery> page,
                                                       @Param("guestName") String guestName);


    /**
     * 通过导游名字模糊查询所有审核数据
     * @param page
     * @param guestName
     * @return
     */
    Page<ExamineResultQuery> listExamine2PageByGuestName(Page<ExamineResultQuery> page,
                                                         @Param("guestName") String guestName);

    /**
     * 展示审核详情
     * @param Id
     * @return
     */
    List<ExamineResultVo> listExamineDetailed(Long Id);

    /**
     * 展示导游个人信息
     * @param page
     * @return
     */
    Page<GuestResultQuery> pageGuest2Me (Page<GuestResultQuery> page);

    /**
     * 展示导游个人信息根据审核状态
     * @param page
     * @param Status
     * @return
     */
    Page<GuestResultQuery> pageGuest2MeByStatus (Page<GuestResultQuery> page,
                                                @Param("Status") Integer Status);

    /**
     * 标记该订单已完成
     * @param lineName
     * @param outDate
     * @param id
     * @param dateTime
     */
    int updateIsFinash(@Param("lineName") String lineName,
                        @Param("outDate") LocalDate outDate,
                        @Param("id") Long id,
                        @Param("dateTime") LocalDateTime dateTime);

    /**
     * 财务通过审核
     * @param outDate
     * @param lineName
     * @param guestId
     * @param time
     * @return
     */
    int examineGuestResult(@Param("outDate") LocalDate outDate,
                            @Param("lineName") String lineName,
                            @Param("guestId") Long guestId,
                            @Param("time") LocalDateTime time);


    /**
     * 财务通过审核
     * @param outDate
     * @param lineName
     * @param guestId
     * @param time
     * @return
     */
    int examineResult(@Param("outDate") LocalDate outDate,
                           @Param("lineName") String lineName,
                           @Param("guestId") Long guestId,
                           @Param("time") LocalDateTime time);


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

}
