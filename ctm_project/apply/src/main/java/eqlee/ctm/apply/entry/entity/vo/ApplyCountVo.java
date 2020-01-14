package eqlee.ctm.apply.entry.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/1/6
 * @vesion 1.0
 **/
@Data
public class ApplyCountVo {

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
}
