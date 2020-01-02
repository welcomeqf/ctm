package eqlee.ctm.apply.entry.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/11/26
 * @vesion 1.0
 **/
@Data
public class ApplyCountBo {

   private String companyName;

   private String companyNo;

   /**
    * 合同结束时间
    */
   private String endTime;

   /**
    * 授信金额
    */
   private Double sxPrice;

   /**
    * 合同图片
    */
   private String picture;

   private Double msPrice;

   private Integer allNumber;

   private Double allPrice;

   private String startDate;
}
