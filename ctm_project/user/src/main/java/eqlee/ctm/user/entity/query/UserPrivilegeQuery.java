package eqlee.ctm.user.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/25
 * @Version 1.0
 */
@Data
public class UserPrivilegeQuery {

    private Long menuId;

    private String menuName;

    private Long parent;

    private String action;

    private String iconClass;

    private String iconColor;

    private Boolean start;
}
