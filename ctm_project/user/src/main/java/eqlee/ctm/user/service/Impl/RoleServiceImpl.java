package eqlee.ctm.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import eqlee.ctm.user.dao.RoleMapper;
import eqlee.ctm.user.entity.UserRole;
import eqlee.ctm.user.entity.vo.RoleVo;
import eqlee.ctm.user.exception.ApplicationException;
import eqlee.ctm.user.service.IRoleService;
import com.yq.utils.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/10
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, UserRole> implements IRoleService {


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

    /**
     * 删除角色
     * @param id
     */
    @Override
    public synchronized void deleteRole(Long id) {
        UserRole role = baseMapper.selectById(id);
        if (role.getStopped() == false) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该角色正在被使用,不能删除");
        }
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


    /**
     * 根据名称查询角色
     * @param RoleName
     * @return
     */
    @Override
    public UserRole queryOne(String RoleName) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getRoleName,RoleName);
        UserRole role = baseMapper.selectOne(queryWrapper);
        return role;
    }

    /**
     * 分页查询所有角色
     * @param roleVo
     * @return
     */
    @Override
    public Page<UserRole> queryPageRole(RoleVo roleVo) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<UserRole>()
                .orderByDesc(UserRole::getId);
        Page<UserRole> page = new Page<>();
        page.setSize(roleVo.getSize());
        page.setCurrent(roleVo.getCurrent());
        baseMapper.selectPage(page,queryWrapper);
        return page;
    }

    /**
     * 根据ID查询角色
     * @param Id
     * @return
     */
    @Override
    public UserRole queryRoleById(Long Id) {
        return baseMapper.selectById(Id);
    }
}
