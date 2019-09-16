package com.yq.user.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/9/10
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("SystemUser")
public class User extends Model<User> {

    private Long Id;

    /**
     * 用户名
     */
    private String Account;

    /**
     * 密码
     */
    private String Password;

    /**
     * 中文名
     */
    private String CName;

    /**
     * 英文名
     */
    private String EName;

    /**
     * 性别（默认0--男   1--女）
     */
    private boolean sex;

    /**
     * 个人图像路径
     */
    private String SelfImagePath;

    /**
     * 电话
     */
    private String Tel;

    /**
     * 用户级别
     */
    private Integer UserLevel;

    /**
     * 是否为超级管理员（0--是  1--不是）
     */
    private Boolean IsSuper;

    /**
     * 用户角色ID
     */
    private Long SystemRoleId;

    /**
     * 用户角色ID（暂时不用）
     */
    private String SystemRoleIds;

    /**
     * 是否停用（0--停用  1--正在使用  默认0）
     */
    private Boolean Stopped;

    /**
     * 备注
     */
    private String Remark;

    /**
     * 部门
     */
    private String Department;

    /**
     * 最后登录时间
     */
    private LocalDateTime LastLoginTime;

    /**
     * 最后登录的IP
     */
    private String LastLoginIp;

    /**
     * 登录次数
     */
    private Integer LoginCount;

    /**
     * 是否删除（默认0  0--不删除  1--删除）
     */
    private Boolean Deleted;

    /**
     * 状态（0--默认 正常  1--冻结）
     */
    private Integer Status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime CreateDate;

    /**
     * 创建人ID
     */
    private Long CreateUserId;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime UpdateDate;

    /**
     * 修改人ID
     */
    private Long UpdateUserId;

    /**
     * 公司ID
     */
    private Long CompanyId;
}
