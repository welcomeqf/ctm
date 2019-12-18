package com.yq.entity.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author qf
 * @date 2019/12/13
 * @vesion 1.0
 **/
@Data
@Component
public class WebSokcetVo {

   /**
    * 发送给谁
    */
   private Long toId;

   /**
    * 发送的内容
    */
   private String msg;

   /**
    * 通信的类型
    *  通信协议
    * 1- 连接请求
    * 2- 心跳消息
    */
   private Integer type;
}
