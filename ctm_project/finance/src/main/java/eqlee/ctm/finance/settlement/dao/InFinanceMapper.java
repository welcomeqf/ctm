package eqlee.ctm.finance.settlement.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.finance.settlement.entity.Income;
import eqlee.ctm.finance.settlement.entity.query.ExamineResultQuery;
import eqlee.ctm.finance.settlement.entity.vo.ContectUserNumberVo;
import eqlee.ctm.finance.settlement.entity.vo.ExamineResultVo;
import eqlee.ctm.finance.settlement.entity.vo.OrderDetailedVo;
import org.springframework.stereotype.Component;

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
     * 展示审核详情
     * @param Id
     * @return
     */
    List<ExamineResultVo> listExamineDetailed(Long Id);

}
