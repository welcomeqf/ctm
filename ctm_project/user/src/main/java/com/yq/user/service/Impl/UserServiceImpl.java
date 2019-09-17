package com.yq.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.user.dao.UserMapper;
import com.yq.user.entity.User;
import com.yq.user.entity.UserRole;
import com.yq.user.entity.vo.UserVo;
import com.yq.user.exception.ApplicationException;
import com.yq.user.service.IRoleService;
import com.yq.user.service.IUserService;
import com.yq.utils.Base64Util;
import com.yq.utils.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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


    /**
     * 运营人员注册
     * @param userVo
     */
    @Override
    public void register(UserVo userVo) {
        String s = Base64Util.encodeString(userVo.getPassword());
        User user = new User();
        IdGenerator idGenerator = new IdGenerator();
        user.setId(idGenerator.getNumberId());
        user.setAccount(userVo.getUserName());
        user.setPassword(s);
        user.setCName(userVo.getName());
        user.setTel(userVo.getPhone());

        UserRole role = roleService.queryOne(userVo.getRoleName());
        user.setSystemRoleId(role.getId());

        if (role.getStopped() == false) {
            role.setStopped(true);
            roleService.updateRole(role);
        }

        //TODO
        //根据输入的公司名查询该公司信息
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
    public synchronized User login(String userName, String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount,userName);
        User user = baseMapper.selectOne(queryWrapper);

        if (user != null) {
            if (!Base64Util.encodeString(password).equals(user.getPassword())) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"密码错误");
            }
            if (user.getStatus() == 1) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"该账户已被冻结,请解冻后再登录");
            }
            if (user.getStopped() == true) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"该账号已经登录");
            }
            LocalDateTime now = LocalDateTime.now();
            user.setLastLoginTime(now);
            user.setStopped(true);
            baseMapper.updateById(user);
        }

        return user;
    }

    /**
     * 注销删除账号
     * @param userName
     */
    @Override
    public synchronized void deleteUser(String userName) {
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
    public void exitUser(String userName) {
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
    public void stopUser(String userName) {
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
    public void toStopUser(String userName) {
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
    public void dowmRegister(UserVo userVo) {
        String s = Base64Util.encodeString(userVo.getPassword());
        User user = new User();
        IdGenerator idGenerator = new IdGenerator();
        user.setId(idGenerator.getNumberId());
        user.setAccount(userVo.getUserName());
        user.setPassword(s);
        user.setCName(userVo.getName());
        user.setTel(userVo.getPhone());

        UserRole userRole = new UserRole();
        long numberId = idGenerator.getNumberId();
        userRole.setId(numberId);
        userRole.setRoleName(userVo.getRoleName());
        userRole.setStopped(true);
        roleService.addRole(userRole);

        user.setSystemRoleId(numberId);

        //根据输入的公司名查询该公司信息

        user.setIsSuper(true);
        int insert = baseMapper.insert(user);

        if (insert <= 0) {
            log.error("insert user db fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"注册失败");
        }
    }

}

