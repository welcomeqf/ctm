package eqlee.ctm.api.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/11/12
 * @vesion 1.0
 **/
@Data
public class UserPasswordUpVo {

   private Long id;

   private String userName;

   private String password;

   private String oldPassword;
}
