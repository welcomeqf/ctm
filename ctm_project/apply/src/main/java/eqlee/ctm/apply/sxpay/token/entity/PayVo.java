package eqlee.ctm.apply.sxpay.token.entity;

import eqlee.ctm.apply.entry.vilidata.ResultTokenVo;
import lombok.Data;

/**
 * @author qf
 * @date 2019/12/24
 * @vesion 1.0
 **/
@Data
public class PayVo {


   private Integer StatusCode;

   private Boolean Error;

   private ResultTokenVo Result;

}
