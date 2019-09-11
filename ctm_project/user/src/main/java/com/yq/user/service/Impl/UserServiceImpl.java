package com.yq.user.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.user.dao.UserMapper;
import com.yq.user.entity.User;
import com.yq.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
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

    /**
     * 注册
     * @param user
     */
    @Override
    public void register(User user) {
        byte[] bytes = DigestUtils.sha1(user.getPassword());
        user.setPassword(bytes.toString());
    }

    @Override
    public Boolean login(String userName, String password) {
        return null;
    }
}

