package eqlee.ctm.user.entity.query;

import eqlee.ctm.user.entity.UserMenu;
import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/10/22
 * @Version 1.0
 */
@Data
public class AllMenuQuery {

    private Long id;

    private String menuName;

    private List<WithQuery> userMenu;
}
