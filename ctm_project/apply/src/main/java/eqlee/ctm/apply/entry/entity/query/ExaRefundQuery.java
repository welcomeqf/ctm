package eqlee.ctm.apply.entry.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/11/4
 * @Version 1.0
 */
@Data
public class ExaRefundQuery {

    private Integer StatusCode;

    private ResultRefundQuery Result;

    private String Error;
}
