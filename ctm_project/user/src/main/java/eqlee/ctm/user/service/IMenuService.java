package eqlee.ctm.user.service;

import eqlee.ctm.user.entity.UserMenu;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
public interface IMenuService {

    /**
     * 增加菜单
     * @param userMenu
     */
    void addMenu(UserMenu userMenu);

    /**
     * 查询所有菜单
     * @return
     */
    List<UserMenu> queryAllMenu();

    /**
     * 根据菜单名查询菜单信息
     * @param MenuName
     * @return
     */
    UserMenu queryOne(String MenuName);
}