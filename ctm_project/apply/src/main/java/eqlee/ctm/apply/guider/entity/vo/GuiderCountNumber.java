package eqlee.ctm.apply.guider.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/12/26
 * @vesion 1.0
 **/
@Data
public class GuiderCountNumber {

   /**
    * 未选人数
    */
   private Integer numberCount;

   private Integer adultNumber;

   private Integer babyNumber;

   private Integer oldNumber;

   private Integer childNumber;

   private Integer totalNumber;

}
