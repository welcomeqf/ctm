package eqlee.ctm.api.entity.vo;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/16
 * @Version 1.0
 */
@Data
public class RoleUpdateVo {

    private Long id;

    private String appId;

    private Boolean stopped;

    private String roleName;
}
