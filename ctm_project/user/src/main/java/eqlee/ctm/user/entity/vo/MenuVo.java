package eqlee.ctm.user.entity.vo;

import lombok.Data;



/**
 * @Author qf
 * @Date 2019/9/23
 * @Version 1.0
 */
@Data
public class MenuVo {

    private String MenuName;

    private String AppId;

    private Long Parent;

    private String Action;

    private String IconClass;
}
