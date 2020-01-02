package eqlee.ctm.resource.system.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/12/27
 * @vesion 1.0
 **/
@Data
public class SystemResultCompanyVo {

   private Long id;

   /**
    * 0--文本  1--图片
    */
   private Integer type;

   /**
    * 描述
    */
   private String description;

   /**
    * 键(全英文）
    */
   private String no;

   /**
    * 值
    */
   private String value;

   /**
    * 备注
    */
   private String remark;
}
