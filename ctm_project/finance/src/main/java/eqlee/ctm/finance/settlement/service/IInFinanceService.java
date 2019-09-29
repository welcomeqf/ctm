package eqlee.ctm.finance.settlement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.finance.settlement.entity.query.ExamineDetailedQuery;
import eqlee.ctm.finance.settlement.entity.query.ExamineResultQuery;
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
     * @return
     */
    Page<ExamineResultQuery> listExamine2Page(Page<ExamineResultQuery> page);


    /**
     * 展示该导游的审核详情
     * @param Id
     * @return
     */
    ExamineDetailedQuery queryExamineDetailed(Long Id);


}
