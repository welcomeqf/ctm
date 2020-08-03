package com.yq.entity.send;

import lombok.Data;

/**
 * @Author xub
 * @Date 2020年7月28日11:44:52
 * @Version 1.0
 */
@Data
public class ResultResposeVo {

    /*
    * 响应状态 200 表示成功
    */
    private Integer StatusCode;

    private SendResultVo Result;

    private Boolean Error;
}
