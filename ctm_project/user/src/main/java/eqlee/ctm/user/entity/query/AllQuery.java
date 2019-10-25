package eqlee.ctm.user.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/22
 * @Version 1.0
 */
@Data
public class AllQuery {


    private Long id;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 是否启用(0--不启用  1--启用)
     */
    private Boolean started;
}
