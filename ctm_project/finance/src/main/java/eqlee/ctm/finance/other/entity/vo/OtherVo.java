package eqlee.ctm.finance.other.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2019/12/4
 * @vesion 1.0
 **/
@Data
public class OtherVo {

   /**
    * ID
    */
   private Long id;

   /**
    * 紧急通知标题
    */
   private String otherName;

   /**
    * 紧急通知内容
    */
   private String otherContent;

   /**
    * 0-不展示 1-展示
    */
   private Integer types;

   /**
    * 创建时间
    */
   private String createDate;
}
