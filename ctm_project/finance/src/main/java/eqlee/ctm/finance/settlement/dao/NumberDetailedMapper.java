package eqlee.ctm.finance.settlement.dao;

import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.finance.settlement.entity.NumberDetailed;
import eqlee.ctm.finance.settlement.entity.vo.ContectUserNumberVo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/27
 * @Version 1.0
 */
@Component
public interface NumberDetailedMapper extends IBaseMapper<NumberDetailed> {


    /**
     * 批量插入未付款代付的人员信息
     * @param list
     * @return
     */
    Integer insertNumberDetailed (List<ContectUserNumberVo> list);
}
