package eqlee.ctm.user.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/21
 * @Version 1.0
 */
@Data
public class User2Query {

    private Long Id;

    private String Account;

    private String Tel;

    private String RoleName;

    private Boolean Stopped;
}
