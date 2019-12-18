package eqlee.ctm.api.pay.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/11/15
 * @vesion 1.0
 **/
@Data
public class PayTokenVo {

   private Integer StatusCode;

   private Boolean Error;

   private ResultTokenVo Result;
}
