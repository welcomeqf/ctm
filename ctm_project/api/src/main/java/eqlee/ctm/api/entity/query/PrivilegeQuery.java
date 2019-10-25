package eqlee.ctm.api.entity.query;

import eqlee.ctm.api.entity.UserMenu;
import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/12
 * @Version 1.0
 */
@Data
public class PrivilegeQuery {

    private Long roleId;

    private List<UserMenu> menuList;


}
