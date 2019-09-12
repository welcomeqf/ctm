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
import com.yq.utils.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 注册
     * @param userVo
     */
    @Override
    public void register(UserVo userVo) {
        byte[] bytes = DigestUtils.sha1(userVo.getPassword());
        User user = new User();
        IdGenerator idGenerator = new IdGenerator();
        user.setId(idGenerator.getNumberId());
        user.setAccount(userVo.getUserName());
        user.setPassword(bytes.toString());
        user.setCName(userVo.getName());
        user.setTel(userVo.getPhone());

        UserRole role = roleService.queryOne(userVo.getRoleName());
        user.setSystemRoleId(role.getId());

        //TODO
        //根据输入的公司名查询该公司信息
        //根据公司信息查询角色表的公司ID信息 如果有则不管   如果没有则设置该账号为IsSuper==1
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
    public User login(String userName, String password) {
        byte[] bytes = DigestUtils.sha1(password);
        String Password = bytes.toString();

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount,userName)
                .eq(User::getPassword,Password);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 注销
     * @param userName
     */
    @Override
    public void deleteUser(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount,userName);

        User user = baseMapper.selectOne(queryWrapper);

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
}

