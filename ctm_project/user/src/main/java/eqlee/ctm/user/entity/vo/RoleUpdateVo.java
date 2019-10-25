package eqlee.ctm.user.entity.vo;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/16
 * @Version 1.0
 */
@Data
public class RoleUpdateVo {

    private String appId;

    private Long id;

    /**
     * 是否停用(0--正常（正在使用）false  1--停用)
     */
    private Boolean stopped;

    private String roleName;
}
