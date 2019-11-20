package eqlee.ctm.apply.entry.entity.query;


import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/9/10
 * @Version 1.0
 */
@Data
public class UserQuery {

    private Long id;

    /**
     * 用户名
     */
    private String account;


    /**
     * 中文名
     */
    private String cname;


    /**
     * 性别（默认0--男   1--女  true）
     */
    private boolean sex;

    /**
     * 电话
     */
    private String tel;

    /**
     * 是否为超级管理员（0--是false  1--不是）
     */
    private Boolean isSuper;

    /**
     * 用户角色名字
     */
    private String roleName;

    /**
     * 是否停用（0--停用  1--正在使用  默认0 false）
     */
    private Boolean stopped;

    /**
     * 是否删除（默认0  0--false不删除  1--删除）
     */
    private Boolean deleted;

    /**
     * 状态（0--默认 正常  1--冻结）
     */
    private Integer status;

    /**
     * 公司ID
     */
    private Long companyId;
}
