package eqlee.ctm.api.pay.entity.vo;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/28
 * @Version 1.0
 */
@Data
public class TokenVo {

    private Integer StatusCode;

    private String Error;

    private ResultTokenVo Result;
}
