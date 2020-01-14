package eqlee.ctm.finance.settlement.dao;

import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.finance.settlement.entity.Outcome2;
import eqlee.ctm.finance.settlement.entity.bo.OutComeInfoBo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qf
 * @date 2019/12/26
 * @vesion 1.0
 **/
@Component
public interface Out2FinanceMapper extends IBaseMapper<Outcome2> {


   /**
    * 批量增加支出信息
    * @param list
    */
   void insertOut2Info(List<OutComeInfoBo> list);

   /**
    * 批量修改支出信息
    * @param list
    */
   void upOut2Info (List<OutComeInfoBo> list);
}
