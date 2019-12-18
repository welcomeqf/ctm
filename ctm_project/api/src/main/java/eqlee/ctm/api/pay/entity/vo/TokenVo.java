package eqlee.ctm.api.pay.entity.vo;

import eqlee.ctm.api.entity.bo.AuthTokenBo;
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

    private AuthTokenBo data;
}
