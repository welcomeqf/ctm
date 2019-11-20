package eqlee.ctm.api.entity.query;

import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/10/22
 * @Version 1.0
 */
@Data
public class PrivilegeDetailedQuery {

    private Long roleId;

    private List<PrivilegeWithQuery> menuList;
}
