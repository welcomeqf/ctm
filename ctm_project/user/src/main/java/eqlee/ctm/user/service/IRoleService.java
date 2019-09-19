package eqlee.ctm.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.user.entity.UserRole;
import eqlee.ctm.user.entity.vo.RoleVo;

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

    /**
     * 分页查询所有角色
     * @param roleVo
     * @return
     */
    Page<UserRole> queryPageRole(RoleVo roleVo);

    /**
     * 根据ID查询角色
     * @param Id
     * @return
     */
    UserRole queryRoleById(Long Id);

}
