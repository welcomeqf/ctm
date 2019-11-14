package eqlee.ctm.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.user.entity.User;
import eqlee.ctm.user.entity.query.*;
import eqlee.ctm.user.entity.vo.UserUpdatePasswordVo;
import eqlee.ctm.user.entity.vo.UserUpdateVo;
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
     * @param AppId
     * @return
     */
    LoginQuery login(String userName, String password, String AppId);

    /**
     * 注销
     * @param userName
     * @param AppId
     */
    void deleteUser(String userName, String AppId);

    /**
     * 根据用户名查询用户信息
     * @param userName
     * @return
     */
    User queryUser(String userName);


    /**
     * 退出账号
     * @param userName
     * @param AppId
     */
    void exitUser(String userName, String AppId);

    /**
     * 冻结账户
     * @param userName
     * @param AppId
     */
    void stopUser(String userName, String AppId);

    /**
     * 解冻账户
     * @param userName
     * @param AppId
     */
    void toStopUser(String userName, String AppId);


    /**
     * 注册子账户
     * @param userVo
     */
    void dowmRegister(UserZiQuery userVo);

    /**
     * 分页查询所有用户加模糊查询
     * @param page
     * @param AppId
     * @return
     */
    Page<UserQuery> queryAllUserByPage(Page<UserQuery> page, String AppId);

    /**
     * 对用户分页数据进行用户模糊以及角色帅选
     * @param page
     * @param userName
     * @param roleName
     * @param AppId
     * @return
     */
    Page<UserQuery> queryPageUserByName(Page<UserQuery> page,String userName,String roleName, String AppId);

    /**
     * 只模糊查询加分页
     * @param page
     * @param userNameOrRole
     * @param AppId
     * @return
     */
    Page<User2Query> queryUserByName(Page<User2Query> page, String userNameOrRole, String AppId);


    /**
     * 根据用户名和手机号重置密码
     * @param vo
     */
    void updateUserPassword (UserUpdatePasswordVo vo);

    /**
     * 修改用户信息
     * @param vo
     */
    void updateUser(UserUpdateVo vo);

    /**
     * 根绝ID查询用户信息
     * @param id
     * @param appId
     * @return
     */
    UserByIdQuery getUserById (Long id, String appId);

}
