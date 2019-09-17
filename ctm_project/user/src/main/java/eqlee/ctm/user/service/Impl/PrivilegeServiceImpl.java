package eqlee.ctm.user.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import eqlee.ctm.user.dao.PrivilegeMapper;
import eqlee.ctm.user.entity.UserMenu;
import eqlee.ctm.user.entity.UserPrivilege;
import eqlee.ctm.user.entity.UserRole;
import eqlee.ctm.user.exception.ApplicationException;
import eqlee.ctm.user.service.IMenuService;
import eqlee.ctm.user.service.IPrivilegeService;
import eqlee.ctm.user.service.IRoleService;
import eqlee.ctm.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class PrivilegeServiceImpl extends ServiceImpl<PrivilegeMapper, UserPrivilege> implements IPrivilegeService {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IUserService userService;

    @Override
    public void insertPrivilege(String RoleName, String MenuName) {
        //查询到具体名称的id
        UserMenu userMenu = menuService.queryOne(MenuName);
        UserPrivilege  privilege = new UserPrivilege();
        privilege.setSystemMenuId(userMenu.getId());

        UserRole role = roleService.queryOne(RoleName);
        privilege.setSystemRoleId(role.getId());
        int insert = baseMapper.insert(privilege);

        if (insert <= 0) {
            log.error("insert privilege db fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR, "增加数据库失败");
        }
    }

    @Override
    public List<UserPrivilege> queryAll() {
        return baseMapper.selectList(null);
    }

    /**
     * 增加所有权限
     * @param roleName
     * @param menuList
     */
    @Override
    public synchronized void insertAllPrivilege(String roleName, List<String> menuList) {
        UserRole role = roleService.queryOne(roleName);

        for (String menu : menuList) {
            UserMenu userMenu = menuService.queryOne(menu);
            UserPrivilege userPrivilege = new UserPrivilege();
            userPrivilege.setSystemRoleId(role.getId());
            userPrivilege.setSystemMenuId(userMenu.getId());
            baseMapper.insert(userPrivilege);
        }
    }
}
