package com.yq.entity.send;

import lombok.Data;

/**
 * @Author xub
 * @Date 2020年7月28日11:44:52
 * @Version 1.0
 */
@Data
public class WechatToken {

    /*
    * 接口访问凭证
    */
    private  String access_token;

    /*
     * 有效期限
     */
    private Integer expires_in;

    /*
     * 错误编码
     */
    private Long errcode;

    /*
     * 错误说明
     */
    private String errmsg;
}
