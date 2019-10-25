package eqlee.ctm.api.entity.query;

import com.yq.jwt.entity.PrivilegeMenuQuery;
import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/10/25
 * @Version 1.0
 */
@Data
public class UserPrivilegeQuery {

    private String menuName;

    private String action;

    private String iconClass;

    private String iconColor;

    private Boolean start;
}
