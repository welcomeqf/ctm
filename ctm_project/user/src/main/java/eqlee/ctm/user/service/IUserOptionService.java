package eqlee.ctm.user.service;

import eqlee.ctm.user.entity.UserOption;

/**
 * @Author qf
 * @Date 2019/10/23
 * @Version 1.0
 */
public interface IUserOptionService {

    /**
     * 增加权限相关
     * @param userOption
     */
    void insertOption (UserOption userOption);

    /**
     * 修改状态
     * @param roleId
     * @param menuId
     * @param start
     */
    void updateStart (Long roleId, Long menuId, Boolean start);
}
