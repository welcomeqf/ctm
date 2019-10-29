package eqlee.ctm.user.entity.query;

import com.yq.jwt.entity.PrivilegeMenuQuery;
import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/10/26
 * @Version 1.0
 */
@Data
public class LoginQuery {

    private Long id;

    /**
     * 用户名
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 中文名
     */
    private String cname;

    /**
     * 电话
     */
    private String tel;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 权限列表
     */
    private List<PrivilegeMenuQuery> menuList;
}
