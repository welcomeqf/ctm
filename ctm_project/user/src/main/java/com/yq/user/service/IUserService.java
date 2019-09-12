package com.yq.user.service;

import com.yq.user.entity.User;
import com.yq.user.entity.vo.UserVo;

/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
public interface IUserService {

    /**
     * 注册
     * @param userVo
     */
    void register(UserVo userVo);

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    User login(String userName, String password);

    /**
     * 注销
     * @param userName
     */
    void deleteUser(String userName);

    /**
     * 根据用户名查询用户信息
     * @param userName
     * @return
     */
    User queryUser(String userName);


}
