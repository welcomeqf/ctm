package eqlee.ctm.api.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/25
 * @Version 1.0
 */
@Data
public class UserRoleZiQuery {

    private String appId;

    /**
     * 角色名字
     */
    private String roleName;

    private Long companyId;
}
