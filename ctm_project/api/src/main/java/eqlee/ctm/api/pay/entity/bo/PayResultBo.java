package eqlee.ctm.api.pay.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/11/28
 * @vesion 1.0
 **/
@Data
public class PayResultBo {

   private Integer StatusCode;

   private Boolean Error;

   private ResultDataBo Result;
}
