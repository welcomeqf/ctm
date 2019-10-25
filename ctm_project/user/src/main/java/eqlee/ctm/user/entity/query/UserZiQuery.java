package eqlee.ctm.user.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/24
 * @Version 1.0
 */
@Data
public class UserZiQuery {

    private String userName;

    private String password;

    private String name;

    private String phone;

    private Long companyId;

    private String appId;

    private String roleName;
}
