package com.yq.entity.send;

import lombok.Data;

/**
 * @author qf
 * @date 2019/11/15
 * @vesion 1.0
 **/
@Data
public class SendTokenVo {

   private Integer StatusCode;

   private Boolean Error;

   private ResultTokenVo Result;
}
