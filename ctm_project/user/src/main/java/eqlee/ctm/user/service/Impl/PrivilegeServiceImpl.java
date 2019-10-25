package eqlee.ctm.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.utils.IdGenerator;
import eqlee.ctm.user.dao.PrivilegeMapper;
import eqlee.ctm.user.entity.*;
import eqlee.ctm.user.entity.query.PrivilegeMenuQuery;
import eqlee.ctm.user.entity.query.PrivilegeWithQuery;
import eqlee.ctm.user.service.*;
import eqlee.ctm.user.vilidata.DataUtils;
import eqlee.ctm.user.vilidata.SignData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private ISignService signService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMenuService menuService;



    @Override
    public List<UserPrivilege> queryAll() {
        return baseMapper.selectList(null);
    }

    /**
     * 增加所有权限
     * @param roleId
     * @param menuList
     */
    @Override
    public void insertAllPrivilege(Long roleId, List<PrivilegeWithQuery> menuList, String AppId) {
        //验证签名
        Sign sign = signService.queryOne(AppId);
        Boolean result = null;
        try {
            result = SignData.getResult(DataUtils.getDcodeing(AppId), DataUtils.getDcodeing(sign.getInformation()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!result) {
            throw new ApplicationException(CodeType.AUTHENTICATION_ERROR);
        }


        UserRole role = roleService.queryRoleById(roleId);

        if (role == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"请传入正确的角色名");
        }

        //先查询数据库是否有   有就修改
        LambdaQueryWrapper<UserPrivilege> wrapper = new LambdaQueryWrapper<UserPrivilege>()
                .eq(UserPrivilege::getSystemRoleId,role.getId());
        List<UserPrivilege> selectList = baseMapper.selectList(wrapper);


        if (selectList.size() != 0) {
            //修改

            //先删再增
            LambdaQueryWrapper<UserPrivilege> lambdaQueryWrapper = new LambdaQueryWrapper<UserPrivilege>()
                    .eq(UserPrivilege::getSystemRoleId,roleId);
            int delete = baseMapper.delete(lambdaQueryWrapper);

            if (delete <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"删除失败");
            }

        }

        //添加
        List<UserPrivilege> list = new ArrayList<>();

        IdGenerator idGenerator = new IdGenerator();

        for (PrivilegeWithQuery menu : menuList) {

            UserPrivilege userPrivilege = new UserPrivilege();
            userPrivilege.setId(idGenerator.getNumberId());
            userPrivilege.setSystemRoleId(roleId);
            userPrivilege.setSystemMenuId(menu.getMenuId());

            list.add(userPrivilege);


        }
        int privilege = baseMapper.insertPrivilege(list);

        if (privilege <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"保存失败");
        }

    }

    /**
     * 根据角色名查询所有菜单权限
     * @param roleId
     * @return
     */
    @Override
    public List<PrivilegeMenuQuery> queryAllMenu(Long roleId) {
        LambdaQueryWrapper<UserPrivilege> queryWrapper = new LambdaQueryWrapper<UserPrivilege>()
                .eq(UserPrivilege::getSystemRoleId,roleId);
        //查询数据库
        List<UserPrivilege> userPrivileges = baseMapper.selectList(queryWrapper);
        //将数据装配到新集合中返回
        List<PrivilegeMenuQuery> result = new ArrayList<>();

        for (UserPrivilege userPrivilege : userPrivileges) {
            PrivilegeMenuQuery query = new PrivilegeMenuQuery();
            //查询数据库将菜单权限返回
            UserMenu userMenu = menuService.queryMenuById(userPrivilege.getSystemMenuId());
            query.setMenuId(userMenu.getId());
            query.setMenuName(userMenu.getMenuName());
            query.setAction(userMenu.getAction());
            query.setIconClass(userMenu.getIconClass());
            query.setIconColor(userMenu.getIconColor());
            result.add(query);
        }


        return result;
    }

}
