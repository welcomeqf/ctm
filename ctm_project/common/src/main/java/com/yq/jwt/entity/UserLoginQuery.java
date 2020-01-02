package com.yq.jwt.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/19
 * @Version 1.0
 */
@Data
public class UserLoginQuery {

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
     * 电话
     */
    private String tel;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 状态（0--管理员   1--子用户）
     */
    private Integer status;

    /**
     * 所属城市
     */
    private String city;

    /**
     * 权限列表
     */
    private List<PrivilegeMenuQuery> menuList;
}
