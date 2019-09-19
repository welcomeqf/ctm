package eqlee.ctm.user.service;

import eqlee.ctm.user.entity.UserPrivilege;
import eqlee.ctm.user.entity.query.PrivilegeMenuQuery;
import eqlee.ctm.user.entity.query.PrivilegeQuery;

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

    /**
     * 根据角色名查询所有菜单权限
     * @param roleName
     * @return
     */
    List<PrivilegeMenuQuery> queryAllMenu(String roleName);
}
