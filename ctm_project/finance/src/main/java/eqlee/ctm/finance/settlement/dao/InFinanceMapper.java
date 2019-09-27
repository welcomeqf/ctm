package eqlee.ctm.finance.settlement.dao;

import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.finance.settlement.entity.Income;
import eqlee.ctm.finance.settlement.entity.vo.ContectUserNumberVo;
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

}
