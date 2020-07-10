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

   private Long companyId;

   /**
    * 报名单号
    */
   private String applyNo;

   /**
    * 年
    */
   private String year;

   /**
    * 月
    */
   private String month;

   /**
    * 月结总额
    */
   private Double allPrice;

   /**
    * 总人数
    */
   private Integer allNumber;

   private Double msprice;

   private Integer adultNumber;

   private Integer babyNumber;

   private Integer oldNumber;

   private Integer childNumber;

   /**
    * 同行
    */
   private String companyName;

   /**
    * 0--未支付  1--已支付
    */
   private Integer sxType;

   /**
    * 0-财务未确认  1--财务已确认
    */
   private Integer caiType;
}
