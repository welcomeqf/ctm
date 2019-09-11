package com.yq.user.service;

import com.yq.user.entity.User;

/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
public interface IUserService {

    void register(User user);

    Boolean login(String userName, String password);
}
