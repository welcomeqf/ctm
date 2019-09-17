package com.yq.user.service;

import com.yq.user.entity.UserPrivilege;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
public interface IPrivilegeService {

    /**
     * 根据角色名，菜单名增加（无用）
     * @param RoleName
     * @param MenuName
     */
    void insertPrivilege(String RoleName, String MenuName);

    /**
     * 查询所有
     * @return
     */
    List<UserPrivilege> queryAll();

    /**
     * 一次性增加一个角色的所有权限
     * @param roleName
     * @param menuList
     */
    void insertAllPrivilege(String roleName, List<String> menuList);
}
