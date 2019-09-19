package eqlee.ctm.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import eqlee.ctm.user.dao.MenuMapper;
import eqlee.ctm.user.entity.UserMenu;
import eqlee.ctm.user.entity.query.UserMenuQuery;
import eqlee.ctm.user.exception.ApplicationException;
import eqlee.ctm.user.service.IMenuService;
import com.yq.utils.IdGenerator;
import lombok.extern.slf4j.Slf4j;
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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, UserMenu> implements IMenuService {

    /**
     * 增加
     * @param userMenu
     */
    @Override
    public void addMenu(UserMenu userMenu) {
        IdGenerator idGenerator = new IdGenerator();
        Long id = idGenerator.getNumberId();
        userMenu.setId(id);
//        userMenu.setMenuName("审核");
////        userMenu.setParent(0L);
////        userMenu.setAction("null");
        int insert = baseMapper.insert(userMenu);

        if (insert <= 0) {
            log.error("insert db fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加菜单失败");
        }
    }

    /**
     * 查询所有菜单权限
     * @param Id
     * @return
     */
    @Override
    public List<UserMenuQuery> queryAllMenu(Long Id) {
        List<UserMenuQuery> list = new ArrayList<>();
        //查询所有系统功能
        if (Id == 0) {
            LambdaQueryWrapper<UserMenu> queryWrapper = new LambdaQueryWrapper<UserMenu>()
                    .eq(UserMenu::getParent,0);
            List<UserMenu> userMenus = baseMapper.selectList(queryWrapper);
            for (UserMenu userMenu : userMenus) {
                UserMenuQuery query = new UserMenuQuery();
                query.setId(userMenu.getId());
                query.setMenuName(userMenu.getMenuName());
                query.setAction(userMenu.getAction());
                query.setIconClass(userMenu.getIconClass());
                list.add(query);
            }
            return list;
        }
        //查询所有系统功能下的所有功能
        LambdaQueryWrapper<UserMenu> query = new LambdaQueryWrapper<UserMenu>()
                .eq(UserMenu::getParent,Id);
        List<UserMenu> userMenuList = baseMapper.selectList(query);
        for (UserMenu userMenu : userMenuList) {
            UserMenuQuery userMenuQuery = new UserMenuQuery();
            //装配query
            userMenuQuery.setId(userMenu.getId());
            userMenuQuery.setMenuName(userMenu.getMenuName());
            userMenuQuery.setAction(userMenu.getAction());
            userMenuQuery.setIconClass(userMenu.getIconClass());
            list.add(userMenuQuery);
        }

        return list;
    }

    /**
     * 根据名称查询
     * @param MenuName
     * @return
     */
    @Override
    public UserMenu queryOne(String MenuName) {
        LambdaQueryWrapper<UserMenu> queryWrapper = new LambdaQueryWrapper<UserMenu>()
                .eq(UserMenu::getMenuName,MenuName);
        UserMenu userMenu = baseMapper.selectOne(queryWrapper);
        return userMenu;
    }

    /**
     * 根据ID查询所有菜单
     * @param Id
     * @return
     */
    @Override
    public UserMenu queryMenuById(Long Id) {
        return baseMapper.selectById(Id);
    }
}
