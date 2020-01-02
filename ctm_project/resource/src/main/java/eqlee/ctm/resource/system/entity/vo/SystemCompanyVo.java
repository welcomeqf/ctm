package eqlee.ctm.resource.system.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/12/27
 * @vesion 1.0
 **/
@Data
public class SystemCompanyVo {

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
    * 是否开放给同行(0-不开放 1-开放)
    */
   private Boolean isPublic;

   /**
    * 备注
    */
   private String remark;
}
