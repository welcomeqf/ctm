package eqlee.ctm.finance.settlement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.finance.settlement.entity.bo.FinanceCompanyBo;
import eqlee.ctm.finance.settlement.entity.bo.FinanceCompanyInfoBo;
import eqlee.ctm.finance.settlement.entity.bo.ResultBo;
import eqlee.ctm.finance.settlement.entity.query.*;
import eqlee.ctm.finance.settlement.entity.vo.FinanceVo;

import java.util.Map;


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
     * 查询提交信息
     * @param orderId
     * @return
     */
    ResultBo queryResult (Long orderId);


    /**
     * 分页查询所有财务审核
     * @param page
     * @param guideName
     * @param type
     * @param outDate
     * @return
     */
    Page<ExamineResultQuery> listExamine2Page(Page<ExamineResultQuery> page, String guideName, Integer type, String outDate);


    /**
     * 展示该导游的审核详情
     * @param Id
     * @return
     */
    Map<String,Object> queryExamineDetailed(Long Id);


    /**
     * 展示导游个人记录
     * @param page
     * @param exaType
     * @param outDate
     * @param lineName
     * @return
     */
    Page<GuestResultQuery> GuestPage2Me (Page<GuestResultQuery> page, String exaType, String outDate, String lineName);

    /**
     * 财务同意或拒绝审核
     * @param id
     * @param type
     * @return
     */
    ExaResultQuery examineGuestResult (Long id, Integer type, String caiName, String remark);

    /**
     * 展示所有财务审核的结果
     * @param page
     * @param guideId
     * @return
     */
    Page<ExamineInfoQuery> pageExamine (Page<ExamineInfoQuery> page, String outDate, String lineName, Long guideId);

    /**
     * 按月份 公司名  统计财务的月结统计
     * @param outDate
     * @param companyName
     * @return
     */
    Page<FinanceCompanyBo> pageCompanyCount (Page<FinanceCompanyBo> page,String outDate, String companyName);

    /**
     * 月结统计详情
     * @param page
     * @param lineName
     * @param outDate
     * @param accountName
     * @return
     */
    Map<String,Object> queryCompanyInfoCount (Page<FinanceCompanyInfoBo> page, String lineName, String outDate, String accountName);

    IncomeCount incomeCount();
}
