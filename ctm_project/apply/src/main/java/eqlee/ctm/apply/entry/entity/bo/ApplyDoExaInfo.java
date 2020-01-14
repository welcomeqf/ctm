package eqlee.ctm.apply.entry.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/12/24
 * @vesion 1.0
 **/
@Data
public class ApplyDoExaInfo {

   private Long id;

   /**
    * 接送地
    */
   private String place;

   /**
    * 结算方式 0-现结 2-月结
    */
   private Integer payType;

   /**
    * 报名时间
    */
   private String createDate;

   /**
    * 面收金额
    */
   private Double msPrice;

   /**
    * 备注
    */
   private String applyRemark;

   /**
    * 第三方支付流水号
    */
   private String thirdNumber;

   /**
    * 支付时间
    */
   private String payDate;

   /**
    * 支付状态 0-未支付 1-支付成功 2-支付失败
    */
   private Integer payStatus;

   /**
    * 支付类型 0-微信 1-支付宝
    */
   private Integer type;

   /**
    * 支付金额
    */
   private Integer payPrice;

   //同行   出行日期  线路名  联系人  电话  保名人数  结算金额

   private String companyName;

   private String outDate;

   private String lineName;

   private  String contactName;

   private String contactTel;

   private Integer allNumber;

   private Integer adultNumber;

   private Integer babyNumber;

   private Integer oldNumber;

   private Integer childNumber;

   private Double allPrice;
}
