package eqlee.ctm.user.entity.vo;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/9/23
 * @Version 1.0
 */
@Data
public class UserRoleVo {

    private Long id;

    /**
     * 角色名字
     */
    private String roleName;

    private Integer statu;
}
