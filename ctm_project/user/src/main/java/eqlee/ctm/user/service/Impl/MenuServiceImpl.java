package eqlee.ctm.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import eqlee.ctm.user.dao.MenuMapper;
import eqlee.ctm.user.entity.UserMenu;
import eqlee.ctm.user.exception.ApplicationException;
import eqlee.ctm.user.service.IMenuService;
import com.yq.utils.IdGenerator;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public List<UserMenu> queryAllMenu() {
        return baseMapper.selectList(null);
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
}
