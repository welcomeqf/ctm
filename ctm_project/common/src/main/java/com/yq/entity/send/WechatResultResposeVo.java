package com.yq.entity.send;

import lombok.Data;

/**
 * @Author xub
 * @Date 2020年7月28日11:44:52
 * @Version 1.0
 */
@Data
public class WechatResultResposeVo {

    /*
    * 响应状态 0 表示成功
    */
    private Integer errcode;

    private String errmsg;

    /*
     * 推送成功返回
     */
    private Long msgid;

    /*
     * 推送失败返回
     */
    private Long rid;
}
