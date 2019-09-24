package eqlee.ctm.api.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/9/19
 * @Version 1.0
 */
@Data
public class UserMenuQuery {

    private Long Id;

    private String MenuName;

    private String Action;

    private String IconClass;
}
