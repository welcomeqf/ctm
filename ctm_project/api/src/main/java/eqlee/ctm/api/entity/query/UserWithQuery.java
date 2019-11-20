package eqlee.ctm.api.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/21
 * @Version 1.0
 */
@Data
public class UserWithQuery {

    private String userName;

    private String password;

    private String name;

    private String phone;

    private Long companyId;

    private String roleName;
}
