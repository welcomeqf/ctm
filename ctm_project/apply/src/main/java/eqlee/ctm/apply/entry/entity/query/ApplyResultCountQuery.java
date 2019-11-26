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

   /**
    * 报名单号
    */
   private String applyNo;

   /**
    * 出发时间
    */
   private String outDate;

   /**
    * 总价格
    */
   private Double allPrice;

   /**
    * 线路名
    */
   private String lineName;

   /**
    * 联系人
    */
   private String contactName;

   /**
    * 联系人电话
    */
   private String contactTel;

   /**
    * 总人数
    */
   private Integer allNumber;

   private Double MarketAllPrice;

   private Integer AdultNumber;

   private Integer BabyNumber;

   private Integer OldNumber;

   private Integer ChildNumber;
}
