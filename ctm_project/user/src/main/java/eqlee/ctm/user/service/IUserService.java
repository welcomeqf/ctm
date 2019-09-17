package eqlee.ctm.user.service;

import eqlee.ctm.user.entity.User;
import eqlee.ctm.user.entity.vo.UserVo;

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


    /**
     * 退出账号
     * @param userName
     */
    void exitUser(String userName);

    /**
     * 冻结账户
     * @param userName
     */
    void stopUser(String userName);

    /**
     * 解冻账户
     * @param userName
     */
    void toStopUser(String userName);


    /**
     * 注册子账户
     * @param userVo
     */
    void dowmRegister(UserVo userVo);

}
