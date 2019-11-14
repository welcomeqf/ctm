package eqlee.ctm.apply.entry.entity.query;

import lombok.Data;

/**
 * @auther qf
 * @date 2019/11/11
 * @vesion 1.0
 **/
@Data
public class ApplyResultCountQuery {

   private Long id;

   private String applyNo;

   private String outDate;

   private Double allPrice;

   private String lineName;

   private String contactName;

   private String contactTel;

   private Integer allNumber;
}
