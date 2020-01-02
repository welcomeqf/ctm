package eqlee.ctm.resource.company.entity.query;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2019/12/20
 * @vesion 1.0
 **/
@Data
public class CompanyAdminQuery {

   private Long id;

   /**
    * 用户名
    */
   private String account;

   /**
    * 中文名
    */
   private String cName;

   /**
    * 电话
    */
   private String tel;

   /**
    * 角色名
    */
   private String roleName;

   /**
    * 公司名称
    */
   private String companyName;

   /**
    * 合同开始时间
    */
   private String startDate;

   /**
    * 合同结束时间
    */
   private String endDate;

   /**
    * 同行编号
    */
   private String companyNo;

   /**
    * 同行合同图片
    */
   private String companyPic;

   /**
    * 授信月结金额
    */
   private Double sxPrice;

   /**
    * 支付方式
    */
   private Integer payMethod;
}
