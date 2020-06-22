package eqlee.ctm.apply.orders.entity.query;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author qf
 * @date 2020/1/14
 * @vesion 1.0
 **/
@Data
public class OrderFinanceOutComeQuery {
   /**
    * 其他收入
    */
   private Double otherInPrice;

   /**
    * 支出消费信息
    */
   private List<Outcome2Vm> outList;


   private String caiName;

   private String remark;

}
