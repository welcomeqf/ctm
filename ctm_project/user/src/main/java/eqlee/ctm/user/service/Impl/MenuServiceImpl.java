package eqlee.ctm.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.entity.PrivilegeMenuQuery;
import eqlee.ctm.user.dao.MenuMapper;
import eqlee.ctm.user.entity.Sign;
import eqlee.ctm.user.entity.UserMenu;
import eqlee.ctm.user.entity.query.UserMenuQuery;
import eqlee.ctm.user.entity.query.UserPrivilegeQuery;
import eqlee.ctm.user.entity.query.WithQuery;
import eqlee.ctm.user.entity.vo.MenuUpdateVo;
import eqlee.ctm.user.entity.vo.MenuVo;
import eqlee.ctm.user.service.IMenuService;
import com.yq.utils.IdGenerator;
import eqlee.ctm.user.service.IPrivilegeService;
import eqlee.ctm.user.service.ISignService;
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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, UserMenu> implements IMenuService {


    @Autowired
    private ISignService signService;

    @Autowired
    private IPrivilegeService privilegeService;

    /**
     * 增加
     * @param vo
     */
    @Override
    public void addMenu(MenuVo vo) {

        IdGenerator idGenerator = new IdGenerator();
        Long id = idGenerator.getNumberId();
        UserMenu userMenu = new UserMenu();
        userMenu.setId(id);
        userMenu.setMenuName(vo.getMenuName());
        userMenu.setAction(vo.getAction());
        userMenu.setIconClass(vo.getIconClass());
        userMenu.setParent(vo.getParent());

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
    public List<UserMenuQuery> queryAllMenu(Long Id, String AppId) {
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
     * 根据ID查询菜单
     * @param Id
     * @return
     */
    @Override
    public UserMenu queryMenuById(Long Id) {
        return baseMapper.selectById(Id);
    }

    /**
     * 修改增加链接地址和图标
     * @param vo
     */
    @Override
    public void updateMenu(MenuUpdateVo vo) {

        //验证签名
        Sign sign = signService.queryOne(vo.getAppId());
        Boolean result = null;
        try {
            result = SignData.getResult(DataUtils.getDcodeing(vo.getAppId()), DataUtils.getDcodeing(sign.getInformation()));
        } catch (Exception e) {
            throw new ApplicationException(CodeType.DATABASE_ERROR,"签名认证失败");
        }
        if (!result) {
            throw new ApplicationException(CodeType.AUTHENTICATION_ERROR);
        }

        UserMenu menu = new UserMenu();

        menu.setId(vo.getId());
        menu.setAction(vo.getAction());
        menu.setIconColor(vo.getIconClass());

        int i = baseMapper.updateById(menu);

        if (i <= 0) {
            log.error("修改菜单失败");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加链接地址失败");
        }
    }

    /**
     * 查询所有
     * @param AppId
     * @param roleId
     * @return
     */
    @Override
    public List<WithQuery> queryAll(String AppId, Long roleId) {

        //验证签名
        Sign sign = signService.queryOne(AppId);
        Boolean result = null;
        try {
            result = SignData.getResult(DataUtils.getDcodeing(AppId), DataUtils.getDcodeing(sign.getInformation()));
        } catch (Exception e) {
            throw new ApplicationException(CodeType.DATABASE_ERROR,"签名认证失败");
        }
        if (!result) {
            throw new ApplicationException(CodeType.AUTHENTICATION_ERROR);
        }


        List<WithQuery> queries = new ArrayList<>();

        //菜单表
        List<WithQuery> withQueries = baseMapper.queryListMenu();

        if (roleId == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"角色id不能为空");
        }
        //权限表
        List<PrivilegeMenuQuery> list1 = privilegeService.queryAllMenu(roleId);

        if (list1.size() == 0) {
            //没有设置权限
            for (WithQuery query : withQueries) {
                query.setStart(false);
                queries.add(query);
            }
            return queries;
        }

        //对所有菜单摔选
        for (WithQuery query : withQueries) {

            for (PrivilegeMenuQuery menuQuery : list1) {

                if (menuQuery.getMenuId().equals(query.getId())) {
                    query.setStart(true);
                    break;
                } else {
                    query.setStart(false);
                }

            }
            queries.add(query);
        }
        return queries;
    }

    /**
     * 查询所有子菜单
     * @param AppId
     * @param roleId
     * @return
     */
    @Override
    public List<WithQuery> queryZiAll(String AppId, Long roleId,List<UserPrivilegeQuery> list) {

        //验证签名
        Sign sign = signService.queryOne(AppId);
        Boolean result = null;
        try {
            result = SignData.getResult(DataUtils.getDcodeing(AppId), DataUtils.getDcodeing(sign.getInformation()));
        } catch (Exception e) {
            throw new ApplicationException(CodeType.DATABASE_ERROR,"签名认证失败");
        }
        if (!result) {
            throw new ApplicationException(CodeType.AUTHENTICATION_ERROR);
        }


        List<WithQuery> queries = new ArrayList<>();


        if (roleId == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"角色id不能为空");
        }
        //权限表
        List<PrivilegeMenuQuery> list1 = privilegeService.queryAllMenu(roleId);

        if (list1.size() == 0) {
            //没有设置权限
            for (UserPrivilegeQuery query : list) {
                WithQuery withQuery = new WithQuery();
                withQuery.setId(query.getMenuId());
                withQuery.setMenuName(query.getMenuName());
                withQuery.setParent(query.getParent());
                withQuery.setStart(false);
                queries.add(withQuery);
            }
            return queries;
        }

        //对所有菜单摔选
        for (UserPrivilegeQuery query : list) {

            WithQuery withQuery = new WithQuery();
            withQuery.setMenuName(query.getMenuName());
            withQuery.setId(query.getMenuId());
            withQuery.setParent(query.getParent());
            for (PrivilegeMenuQuery menuQuery : list1) {

                if (menuQuery.getMenuId().equals(query.getMenuId())) {
                    withQuery.setStart(true);
                    break;
                } else {
                    withQuery.setStart(false);
                }


            }
            queries.add(withQuery);
        }
        return queries;
    }

    /**
     * 修改启用状态
     * @param id
     * @param start
     */
    @Override
    public void updateStart(Long id, Boolean start) {
        UserMenu menu = new UserMenu();

        menu.setId(id);

        int byId = baseMapper.updateById(menu);

        if (byId <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改菜单启用状态失败");
        }


    }

    /**
     * 查询所有父
     * @param AppId
     * @return
     */
    @Override
    public List<WithQuery> queryAllParent(String AppId) {
        //验证签名
        Sign sign = signService.queryOne(AppId);
        Boolean result = null;
        try {
            result = SignData.getResult(DataUtils.getDcodeing(AppId), DataUtils.getDcodeing(sign.getInformation()));
        } catch (Exception e) {
            throw new ApplicationException(CodeType.DATABASE_ERROR,"签名认证失败");
        }
        if (!result) {
            throw new ApplicationException(CodeType.AUTHENTICATION_ERROR);
        }

        return baseMapper.queryMenu();
    }



}
