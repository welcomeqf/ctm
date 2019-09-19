package eqlee.ctm.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.user.entity.User;
import eqlee.ctm.user.entity.query.UserLoginQuery;
import eqlee.ctm.user.entity.query.UserQuery;
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
    UserLoginQuery login(String userName, String password);

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

    /**
     * 分页查询所有用户加模糊查询
     * @param page
     * @return
     */
    Page<UserQuery> queryAllUserByPage(Page<UserQuery> page);

    /**
     * 对用户分页数据进行用户模糊以及角色帅选
     * @param page
     * @param userName
     * @param roleName
     * @return
     */
    Page<UserQuery> queryPageUserByName(Page<UserQuery> page,String userName,String roleName);

    /**
     * 只模糊查询加分页
     * @param page
     * @param userName
     * @return
     */
    Page<UserQuery> queryUserByName(Page<UserQuery> page, String userName);

}
