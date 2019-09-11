package com.yq.user.service;

import com.yq.user.entity.UserPrivilege;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
public interface IPrivilegeService {

    void insertPrivilege(String RoleName, String MenuName);

    List<UserPrivilege> queryAll();
}
