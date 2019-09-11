package com.yq.user.service;

import com.yq.user.entity.UserRole;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/10
 * @Version 1.0
 */
public interface IRoleService {

    void addRole(UserRole role);

    void deleteRole(Long id);

    void updateRole(UserRole role);

    List<UserRole> queryAllRole();

    UserRole queryOne(String RoleName);
}
