package eqlee.ctm.api.pay.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/11/28
 * @vesion 1.0
 **/
@Data
public class ResultDataBo {

   private String Msg;

   private Boolean Error;

   private String Href;

   private Object Data;

   private Integer Code;
}
