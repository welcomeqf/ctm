package eqlee.ctm.api.pay.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/12/24
 * @vesion 1.0
 **/
@Data
public class PayInfoBo {

   private String thirdPayOrderId;

   private String payOrderSerialNumber;

   private String message;

   private Integer payStatus;

   private Integer payType;
}
