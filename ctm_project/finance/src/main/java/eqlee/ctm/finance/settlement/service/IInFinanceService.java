package eqlee.ctm.finance.settlement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.finance.settlement.entity.query.*;
import eqlee.ctm.finance.settlement.entity.vo.FinanceVo;


/**
 * @Author qf
 * @Date 2019/9/27
 * @Version 1.0
 */
public interface IInFinanceService {

    /**
     * 提交导游消费情况
     * @param vo
     */
    void insertFinance (FinanceVo vo);

    /**
     * 分页查询所有财务审核
     * @param page
     * @param guestName
     * @param type
     * @return
     */
    Page<ExamineResultQuery> listExamine2Page(Page<ExamineResultQuery> page, String guestName, String type);


    /**
     * 展示该导游的审核详情
     * @param Id
     * @return
     */
    ExamineDetailedQuery queryExamineDetailed(Long Id);


    /**
     * 展示导游个人记录
     * @param page
     * @param exaType
     * @return
     */
    Page<GuestResultQuery> GuestPage2Me (Page<GuestResultQuery> page, String exaType);

    /**
     * 财务同意或拒绝审核
     * @param outDate
     * @param lineName
     * @param guestId
     * @param type
     * @return
     */
    ExaResultQuery examineGuestResult (String outDate, String lineName, Long guestId, Integer type);

    /**
     * 展示所有财务审核的结果
     * @param page
     * @param guideId
     * @return
     */
    Page<ExamineInfoQuery> pageExamine (Page<ExamineInfoQuery> page, String outDate, String lineName, Long guideId);

}
