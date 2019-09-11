package com.yq.user.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.user.dao.PrivilegeMapper;
import com.yq.user.entity.UserMenu;
import com.yq.user.entity.UserPrivilege;
import com.yq.user.entity.UserRole;
import com.yq.user.exception.ApplicationException;
import com.yq.user.service.IMenuService;
import com.yq.user.service.IPrivilegeService;
import com.yq.user.service.IRoleService;
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
}
