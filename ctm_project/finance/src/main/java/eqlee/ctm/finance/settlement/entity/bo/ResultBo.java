package eqlee.ctm.finance.settlement.entity.bo;

import eqlee.ctm.finance.settlement.entity.Outcome2;
import lombok.Data;

import java.util.List;

/**
 * @author qf
 * @date 2019/12/30
 * @vesion 1.0
 **/
@Data
public class ResultBo {

   /**
    * 其他收入
    */
   private Double otherInPrice;

   /**
    * 支出消费信息
    */
   private List<Outcome2> outList;


   private String caiName;

   private String remark;
}
