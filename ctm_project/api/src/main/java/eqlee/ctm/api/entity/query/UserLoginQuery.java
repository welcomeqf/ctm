package eqlee.ctm.api.entity.query;

import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/19
 * @Version 1.0
 */
@Data
public class UserLoginQuery {

    /**
     * 用户名
     */
    private String Account;

    /**
     * 密码
     */
    private String Password;

    /**
     * 中文名
     */
    private String CName;

    /**
     * 电话
     */
    private String Tel;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 状态（0--默认 正常  1--冻结）
     */
    private String Status;

    /**
     * 公司ID
     */
    private Long CompanyId;

    /**
     * 权限列表
     */
    private List<PrivilegeMenuQuery> menuList;
}
