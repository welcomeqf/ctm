package eqlee.ctm.finance.settlement.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/12/3
 * @vesion 1.0
 **/
@Data
public class FinanceCompanyInfoBo {

   /**
    * 订单主表Id
    */
   private Long id;

   /**
    * 公司编号
    */
   private String companyNo;

   /**
    * 出行日期
    */
   private String outDate;

   /**
    * 线路名
    */
   private String lineName;

   /**
    * 公司名
    */
   private String companyName;

   /**
    * 总人数
    */
   private Integer allNumber;

   /**
    * 月结金额
    */
   private Double monthPrice;

   /**
    * 面收金额
    */
   private Double msPrice;

   /**
    * 总金额
    */
   private Double price;

   /**
    * 成人总人数
    */
   private Integer adultNumber;

   /**
    * 幼儿总人数
    */
   private Integer babyNumber;

   /**
    * 老人总人数
    */
   private Integer oldNumber;

   /**
    * 小孩总人数
    */
   private Integer childNumber;

   /**
    * 同行代表人名字
    */
   private String companyUserName;
}
