package com.yq.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.user.dao.RoleMapper;
import com.yq.user.entity.UserRole;
import com.yq.user.exception.ApplicationException;
import com.yq.user.service.IRoleService;
import com.yq.utils.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @Author qf
 * @Date 2019/9/10
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, UserRole> implements IRoleService {

    @Autowired
    private RoleMapper mapper;

    @Override
    public void addRole(UserRole role) {
        IdGenerator idGenerator = new IdGenerator();
        role.setId(idGenerator.getNumberId());
        int insert = baseMapper.insert(role);
        if (insert <= 0) {
            log.error("insert role db fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加角色数据库失败~");
        }
    }

    @Override
    public void deleteRole(Long id) {
        int delete = baseMapper.deleteById(id);
        if (delete <= 0) {
            log.error("delete role db fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"删除角色失败");
        }
    }

    @Override
    public void updateRole(UserRole role) {
        int updateById = baseMapper.updateById(role);
        if (updateById <= 0) {
            log.error("update role db fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改角色失败");
        }
    }

    @Override
    public List<UserRole> queryAllRole() {
        return baseMapper.selectList(null);
    }


    @Override
    public UserRole queryOne(String RoleName) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getRoleName,RoleName);
        UserRole role = baseMapper.selectOne(queryWrapper);
        return role;
    }
}
