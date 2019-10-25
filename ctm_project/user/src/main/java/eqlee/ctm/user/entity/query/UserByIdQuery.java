package eqlee.ctm.user.entity.query;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/10/17
 * @Version 1.0
 */
@Data
public class UserByIdQuery {

    private Long Id;

    /**
     * 用户名
     */
    private String Account;


    /**
     * 中文名
     */
    private String CName;


    /**
     * 性别（默认0--男   1--女  true）
     */
    private boolean sex;

    /**
     * 电话
     */
    private String Tel;

    /**
     * 是否为超级管理员（0--是false  1--不是）
     */
    private Boolean IsSuper;

    /**
     * 用户角色名字
     */
    private String RoleName;

    /**
     * 是否停用（0--停用  1--正在使用  默认0 false）
     */
    private Boolean Stopped;

    /**
     * 是否删除（默认0  0--false不删除  1--删除）
     */
    private Boolean Deleted;

    /**
     * 状态（0--默认 正常  1--冻结）
     */
    private Integer Status;

    /**
     * 公司ID
     */
    private Long CompanyId;
}
