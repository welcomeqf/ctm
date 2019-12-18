package eqlee.ctm.finance.settlement.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/12/2
 * @vesion 1.0
 **/
@Data
public class FinanceCompanyBo {

   /**
    * 公司编码
    */
   private String companyNo;

   /**
    * 出行日期
    */
   private String outDate;

   /**
    * 公司名
    */
   private String companyName;

   /**
    * 总人数
    */
   private Integer allNumber;

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
    * 月结金额
    */
   private Double monthPrice;

   /**
    * 面收金额
    */
   private Double msPrice;

   /**
    * 应收金额
    */
   private Double gaiPrice;

   /**
    * 同行代表人名字
    */
   private String companyUserName;

   /**
    * 同行代表人电话
    */
   private String companyUserTel;

   /**
    * 同行代表人账号(账号作为查询下一个页面的参数）
    */
   private String accountName;


}
