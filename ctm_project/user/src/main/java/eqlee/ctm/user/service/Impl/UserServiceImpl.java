package eqlee.ctm.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.utils.ShaUtils;
import eqlee.ctm.user.dao.UserMapper;
import eqlee.ctm.user.entity.Sign;
import eqlee.ctm.user.entity.User;
import eqlee.ctm.user.entity.UserRole;
import eqlee.ctm.user.entity.query.PrivilegeMenuQuery;
import eqlee.ctm.user.entity.query.UserLoginQuery;
import eqlee.ctm.user.entity.query.UserQuery;
import eqlee.ctm.user.entity.vo.*;
import eqlee.ctm.user.service.IPrivilegeService;
import eqlee.ctm.user.service.IRoleService;
import eqlee.ctm.user.service.ISignService;
import eqlee.ctm.user.service.IUserService;
import com.yq.utils.IdGenerator;
import eqlee.ctm.user.vilidata.DataUtils;
import eqlee.ctm.user.vilidata.SignData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPrivilegeService privilegeService;

    @Autowired
    private ISignService signService;


    IdGenerator idGenerator = new IdGenerator();

    /**
     * 管理人员注册
     * @param userVo
     */
    @Override
    public synchronized void register(UserVo userVo) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount,userVo.getUserName());
        User user1 = baseMapper.selectOne(queryWrapper);
        if (user1 != null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该账号已被使用");
        }
        String s = ShaUtils.getSha1(userVo.getPassword());
        User user = new User();
        user.setId(idGenerator.getNumberId());
        user.setAccount(userVo.getUserName());
        user.setPassword(s);
        user.setCName(userVo.getName());
        user.setTel(userVo.getPhone());
        user.setCompanyId(userVo.getCompanyId());

        UserRole role = roleService.queryOne(userVo.getRoleName());
        user.setSystemRoleId(role.getId());

        if (role.getStopped()) {
            role.setStopped(false);
            roleService.updateRole(role);
        }

        int insert = baseMapper.insert(user);

        if (insert <= 0) {
            log.error("insert user db fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"注册失败");
        }
    }

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    @Override
    public synchronized UserLoginQuery login(String userName, String password, String AppId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount,userName);
        User user = baseMapper.selectOne(queryWrapper);

        if (user == null) {
            log.error("user login fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"用户名有误");
        }

        if (!ShaUtils.getSha1(password).equals(user.getPassword())) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"密码错误");
        }
        if (user.getStatus() == 1) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该账户已被冻结,请解冻后再登录");
        }
        if (user.getStopped()) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该账号已经登录");
        }

        LocalDateTime now = LocalDateTime.now();
        user.setLastLoginTime(now);
        user.setStopped(true);
        baseMapper.updateById(user);

        //到此处表示已经登录成功签名认证已经通过，将签名信息保存进数据库
        Sign sign = new Sign();
        //将信息装进Sign表中
        sign.setId(idGenerator.getNumberId());
        Sign one = signService.queryOne(AppId);
        if (one == null) {
            sign.setAppId(AppId);
        }
        ResultSignVo vo = null;
        try {
            vo = SignData.getSign(DataUtils.getDcodeing(AppId), userName);
        }catch (Exception e) {
            e.printStackTrace();
        }
        sign.setMySig(vo.getMySig());
        sign.setInformation(DataUtils.getEncodeing(userName));
        sign.setPrivateKey(vo.getKeypair());
        sign.setPublicKey(vo.getBytes());
        signService.insertSign(sign);

        UserRole userRole = roleService.queryRoleById(user.getSystemRoleId());
        List<PrivilegeMenuQuery> list = privilegeService.queryAllMenu(userRole.getRoleName());
        //装配UserLoginQuery
        UserLoginQuery query = new UserLoginQuery();
        query.setId(user.getId());
        query.setAccount(user.getAccount());
        query.setCName(user.getCName());
        query.setCompanyId(user.getCompanyId());
        query.setPassword(user.getPassword());
        query.setTel(user.getTel());
        query.setRoleName(userRole.getRoleName());
        query.setMenuList(list);
        return query;
    }

    /**
     * 注销删除账号
     * @param userName
     */
    @Override
    public synchronized void deleteUser(String userName,String AppId) {
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

        //执行业务
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount,userName);

        User user = baseMapper.selectOne(queryWrapper);

        if (user.getStopped() == true) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该账号正在使用，不能删除");
        }

        int delete = baseMapper.deleteById(user.getId());

        if (delete <= 0) {
            log.error("delete user fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"注销失败");
        }
    }

    /**
     * 查询用户信息
     * @param userName
     * @return
     */
    @Override
    public User queryUser(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount,userName);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 退出账号
     * @param userName
     */
    @Override
    public void exitUser(String userName, String AppId) {
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
        //执行业务
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount,userName);
        User user = new User();
        user.setStopped(false);
        int update = baseMapper.update(user, queryWrapper);

        if (update <= 0) {
            log.error("exit user fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"退出账号失败");
        }

    }

    /**
     * 冻结账户
     * @param userName
     */
    @Override
    public void stopUser(String userName, String AppId) {
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

        //执行业务
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount,userName);
        User user = new User();
        user.setStatus(1);
        int update = baseMapper.update(user, queryWrapper);

        if (update <= 0) {
            log.error("stop user fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"冻结账户失败");
        }
    }

    /**
     * 解冻
     * @param userName
     */
    @Override
    public void toStopUser(String userName, String AppId) {
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

        //执行业务
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount,userName);
        User user = new User();
        user.setStatus(0);
        int update = baseMapper.update(user, queryWrapper);

        if (update <= 0) {
            log.error("toStop user fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"解冻账户失败");
        }
    }

    /**
     * 注册子账户
     * @param userVo
     */
    @Override
    public synchronized void dowmRegister(UserVo userVo) {
        //验证签名
        Sign sign = signService.queryOne(userVo.getAppId());
        Boolean result = null;
        try {
            result = SignData.getResult(DataUtils.getDcodeing(userVo.getAppId()), DataUtils.getDcodeing(sign.getInformation()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!result) {
            throw new ApplicationException(CodeType.AUTHENTICATION_ERROR);
        }

        //执行业务
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount,userVo.getUserName());
        User user1 = baseMapper.selectOne(queryWrapper);
        if (user1 != null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该账号已被使用");
        }
        String s = ShaUtils.getSha1(userVo.getPassword());
        User user = new User();
        user.setId(idGenerator.getNumberId());
        user.setAccount(userVo.getUserName());
        user.setPassword(s);
        user.setCName(userVo.getName());
        user.setTel(userVo.getPhone());
        user.setCompanyId(userVo.getCompanyId());

        //装配用户角色
        UserRoleVo userRole = new UserRoleVo();
        long numberId = idGenerator.getNumberId();
        userRole.setId(numberId);
        userRole.setRoleName(userVo.getRoleName());
        userRole.setStatu(1);
        roleService.addRole(userRole);

        user.setSystemRoleId(numberId);

        user.setIsSuper(true);
        int insert = baseMapper.insert(user);

        if (insert <= 0) {
            log.error("insert user db fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"注册失败");
        }
    }

    /**
     * 分页高级查询用户列表
     * @param page
     * @return
     */
    @Override
    public Page<UserQuery> queryAllUserByPage(Page<UserQuery> page, String AppId) {
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

        return baseMapper.queryUserInfo(page);
    }

    /**
     * 对用户分页数据进行用户模糊以及角色帅选
     * @param page
     * @param userName
     * @param roleName
     * @return
     */
    @Override
    public Page<UserQuery> queryPageUserByName(Page<UserQuery> page,String userName,String roleName, String AppId) {
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

        return baseMapper.queryPageUserByName(page,userName,roleName);
    }

    /**
     * 模糊查询加分页
     * @param page
     * @param userName
     * @return
     */
    @Override
    public Page<UserQuery> queryUserByName(Page<UserQuery> page, String userName, String AppId) {
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
        return baseMapper.queryUserByName(page,userName);
    }

    /**
     * 重置密码
     * @param vo
     */
    @Override
    public synchronized void updateUserPassword(Long Id,UserUpdatePasswordVo vo) {
        //验证签名
        Sign sign = signService.queryOne(vo.getAppId());
        Boolean result = null;
        try {
            result = SignData.getResult(DataUtils.getDcodeing(vo.getAppId()), DataUtils.getDcodeing(sign.getInformation()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!result) {
            throw new ApplicationException(CodeType.AUTHENTICATION_ERROR);
        }

        //业务
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount,vo.getUserName());
        User user = baseMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该用户不存在");
        }

        if (!user.getTel().equals(vo.getTel())) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "手机号填写有误");
        }

        User u = new User();
        u.setId(Id);
        u.setPassword(ShaUtils.getSha1(vo.getPassword()));
        int updateById = baseMapper.updateById(u);

        if (updateById <= 0) {
            log.error("update password fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"重置密码失败");
        }
    }

    /**
     * 修改用户信息
     * @param vo
     */
    @Override
    public void updateUser(UserUpdateVo vo,Long Id) {
        //验证签名
        Sign sign = signService.queryOne(vo.getAppId());
        Boolean result = null;
        try {
            result = SignData.getResult(DataUtils.getDcodeing(vo.getAppId()), DataUtils.getDcodeing(sign.getInformation()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!result) {
            throw new ApplicationException(CodeType.AUTHENTICATION_ERROR);
        }

        //修改用户
        User user = new User();
        user.setId(Id);
        user.setCompanyId(vo.getCompanyId());
        user.setCName(vo.getCName());
        user.setSelfImagePath(vo.getSelfImagePath());
        user.setTel(vo.getTel());

        int byId = baseMapper.updateById(user);

        if (byId <= 0) {
            log.error("update user fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改用户信息失败");
        }
    }

}

