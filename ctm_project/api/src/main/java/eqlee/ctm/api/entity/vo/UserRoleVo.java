package eqlee.ctm.api.entity.vo;

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

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 是否停用(0--停用false  1--正常（正在使用）  默认停用)
     */
    private Boolean stopped;

    /**
     * 0--默认  1--子用户设置
     */
    private Integer statu;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否已删除(0--未删除  1--已删除  默认0)
     */
    private Boolean deleted;

    private String appId;

}
