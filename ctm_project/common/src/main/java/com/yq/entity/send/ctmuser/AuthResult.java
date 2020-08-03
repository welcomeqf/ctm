package com.yq.entity.send.ctmuser;

import lombok.Data;

/**
 * @author qf
 * @date 2019/11/15
 * @vesion 1.0
 **/
@Data
public class AuthResult {

   private String token;

   private Long expTime;
}
