package eqlee.ctm.api.pay.entity.vo;

import eqlee.ctm.api.entity.query.AuthResult;
import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/28
 * @Version 1.0
 */
@Data
public class TokenVo {

    private Integer code;

    private String msg;

    private AuthResult data;
}
