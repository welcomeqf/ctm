package eqlee.ctm.api.entity.vo;


import eqlee.ctm.api.entity.query.UserPrivilegeQuery;
import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/10/25
 * @Version 1.0
 */
@Data
public class MenuZiVo {

    private Long roleId;

    private List<UserPrivilegeQuery> list;
}
