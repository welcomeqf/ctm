package eqlee.ctm.apply.entry.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/1/7
 * @vesion 1.0
 **/
@Data
public class ApplyCountCaiBo {

   private String applyNo;

   private String outDate;

   private String lineName;

   private String contactName;

   private String contactTel;

   private Integer allNumber;

   private Integer adultNumber;

   private Integer babyNumber;

   private Integer oldNumber;

   private Integer childNumber;

   private Double allPrice;

   private Double msPrice;

   private String cname;

   private String companyDbTel;
}
