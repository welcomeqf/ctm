package eqlee.ctm.apply.sxpay.entity;

import lombok.Data;

/**
 * @author qf
 * @date 2019/12/24
 * @vesion 1.0
 **/
@Data
public class PayInfo {

   private String thirdPayOrderId;

   private String payOrderSerialNumber;

   private String message;

   private Integer payStatus;

   private Integer payType;
}
