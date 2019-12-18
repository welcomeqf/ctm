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
    * 其他收费名称
    */
   private String otherName;

   /**
    * 创建时间
    */
   private LocalDateTime createDate;
}
