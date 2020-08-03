package com.yq.entity.send;

import lombok.Data;

/**
 * @Author xub
 * @Date 2020年7月28日11:44:52
 * @Version 1.0
 */
@Data
public class SendResultVo {

    /*
    * 发送短信是否有错false没有true 有
    */
    private Boolean Error;

    /*
     * 0 成功
     */
    private Integer Code;

    private String Msg;

    private String Href;

    private Object Data;
}
