package com.yq.entity.send;

import lombok.Data;

/**
 * @Author xub
 * @Date 2020年7月28日11:44:52
 * @Version 1.0
 */
@Data
public class SendModel {

    /*
    * 发送短信的接收手机号，支持多个以逗号隔开
    */
    private  String Mobile;

    private String Content;
}
