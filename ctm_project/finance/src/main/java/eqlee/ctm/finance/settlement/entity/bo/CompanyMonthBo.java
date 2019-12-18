package eqlee.ctm.finance.settlement.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/12/5
 * @vesion 1.0
 **/
@Data
public class CompanyMonthBo {

   /**
    * 公司编号
    */
   private String companyNo;

   /**
    * 公司名
    */
   private String companyName;

   /**
    * 同行代表人名字
    */
   private String companyUserName;
}
