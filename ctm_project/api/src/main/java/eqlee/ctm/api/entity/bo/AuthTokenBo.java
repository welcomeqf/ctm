package eqlee.ctm.api.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/11/27
 * @vesion 1.0
 **/
@Data
public class AuthTokenBo {

   private String token;

   private Long expTime;
}
