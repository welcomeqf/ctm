package eqlee.ctm.api.httpclient;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author qf
 * @Date 2019/9/21
 * @Version 1.0
 */
@Data
public class HttpResult implements Serializable {

    /**
     * 响应的状态码
     */
    private int code;

    /**
     * 响应的响应体
     */
    private String body;
}
