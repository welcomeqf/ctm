package eqlee.ctm.user.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/23
 * @Version 1.0
 */
@Data
public class WithQuery {

    private Long id;

    private String menuName;

    private Boolean start;

    private Long parent;
}
