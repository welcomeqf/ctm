package eqlee.ctm.user.entity.query;

import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/12
 * @Version 1.0
 */
@Data
public class PrivilegeQuery {

    private String roleName;

    private List<String> menuList;

    private String appId;
}
