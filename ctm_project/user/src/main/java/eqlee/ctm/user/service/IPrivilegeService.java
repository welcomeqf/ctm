package eqlee.ctm.user.service;

import com.yq.jwt.entity.PrivilegeMenuQuery;
import eqlee.ctm.user.entity.UserPrivilege;
import eqlee.ctm.user.entity.query.PrivilegeQuery;
import eqlee.ctm.user.entity.query.PrivilegeWithQuery;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
public interface IPrivilegeService {

    /**
     * 查询所有
     * @return
     */
    List<UserPrivilege> queryAll();

    /**
     * 一次性增加一个角色的所有权限
     * @param roleId
     * @param menuList
     * @param AppId
     */
    void insertAllPrivilege(Long roleId, List<PrivilegeWithQuery> menuList, String AppId);

    /**
     * 根据角色名查询所有菜单权限
     * @param roleId
     * @return
     */
    List<PrivilegeMenuQuery> queryAllMenu(Long roleId);




}
