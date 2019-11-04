package eqlee.ctm.apply.entry.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/11/4
 * @Version 1.0
 */
@Data
public class ResultRefundQuery {

    private Boolean Error;

    private Integer Code;

    private String Msg;

    private String Href;

    private Object Data;
}
