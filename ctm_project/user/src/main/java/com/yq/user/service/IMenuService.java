package com.yq.user.service;

import com.yq.user.entity.UserMenu;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
public interface IMenuService {

    void addMenu(UserMenu userMenu);

    List<UserMenu> queryAllMenu();

    UserMenu queryOne(String MenuName);
}
