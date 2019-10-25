package eqlee.ctm.user.service;

import eqlee.ctm.user.entity.UserMenu;
import eqlee.ctm.user.entity.query.*;
import eqlee.ctm.user.entity.vo.MenuUpdateVo;
import eqlee.ctm.user.entity.vo.MenuVo;

import java.time.LocalDate;
import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
public interface IMenuService {

    /**
     * 增加菜单
     * @param vo
     */
    void addMenu(MenuVo vo);

    /**
     * 查询所有菜单
     * @param Id
     * @param AppId
     * @return
     */
    List<UserMenuQuery> queryAllMenu(Long Id, String AppId);

    /**
     * 根据菜单名查询菜单信息
     * @param MenuName
     * @return
     */
    UserMenu queryOne(String MenuName);

    /**
     * 根据ID查询菜单信息
     * @param Id
     * @return
     */
    UserMenu queryMenuById(Long Id);

    /**
     * 修改菜单 增加链接地址以及图标
     * @param vo
     */
    void updateMenu(MenuUpdateVo vo);

    /**
     *  查看所有
     * @param AppId
     * @param roleId
     * @return
     */
    List<WithQuery> queryAll (String AppId, Long roleId);


    /**
     *  查看所有子菜单
     * @param AppId
     * @param roleId
     * @return
     */
    List<WithQuery> queryZiAll (String AppId, Long roleId,List<UserPrivilegeQuery> list);

    /**
     * 修改菜单状态
     * @param id
     * @param start
     */
    void updateStart (Long id, Boolean start);

    /**
     *  查看所有
     * @param AppId
     * @return
     */
    List<WithQuery> queryAllParent (String AppId);



}
