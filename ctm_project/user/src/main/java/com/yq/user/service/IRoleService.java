package com.yq.user.service;

import com.yq.user.entity.UserRole;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/10
 * @Version 1.0
 */
public interface IRoleService {

    /**
     * 增加角色
     * @param role
     */
    void addRole(UserRole role);

    /**
     * 根据ID删除角色
     * @param id
     */
    void deleteRole(Long id);

    /**
     * 修改角色
     * @param role
     */
    void updateRole(UserRole role);

    /**
     * 查询所有角色
     * @return
     */
    List<UserRole> queryAllRole();

    /**
     * 根据角色名查询角色信息
     * @param RoleName
     * @return
     */
    UserRole queryOne(String RoleName);
}
