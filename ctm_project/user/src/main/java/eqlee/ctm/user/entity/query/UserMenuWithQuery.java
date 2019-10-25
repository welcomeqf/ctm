package eqlee.ctm.user.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/23
 * @Version 1.0
 */
@Data
public class UserMenuWithQuery {

    private Long id;

    /**
     * 父级ID
     */
    private Long parent;

    private String menuName;

}
