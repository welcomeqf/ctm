package eqlee.ctm.apply.entry.entity.query;

import lombok.Data;

/**
 * @author qf
 * @date 2019/11/13
 * @vesion 1.0
 **/
@Data
public class ApplyReadCountQuery {

   /**
    * 报名审核条数
    */
   private Integer exaCount;

   /**
    * 取消审核条数
    */
   private Integer cancelCount;
}
