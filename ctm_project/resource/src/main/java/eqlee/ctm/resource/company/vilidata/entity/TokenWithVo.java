package eqlee.ctm.resource.company.vilidata.entity;

import lombok.Data;

/**
 * @author qf
 * @date 2019/11/16
 * @vesion 1.0
 **/
@Data
public class TokenWithVo {

   private Integer code;

   private String msg;

   private AuthResult data;
}
