package eqlee.ctm.user.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.user.entity.User;
import eqlee.ctm.user.entity.query.UserQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
@Component
public interface UserMapper extends IBaseMapper<User> {


    /**
     * 分页查询所有用户信息
     * @param page
     * @return
     */
    Page<UserQuery> queryUserInfo(Page<UserQuery> page);

    /**
     * 对用户分页数据进行用户模糊以及角色帅选
     * @param page
     * @param userName
     * @param roleName
     * @return
     */
    Page<UserQuery> queryPageUserByName(Page<UserQuery> page,
                                        @Param("userName") String userName,
                                        @Param("roleName") String roleName);


    /**
     * 模糊查询加分页
     * @param page
     * @param userName
     * @return
     */
    Page<UserQuery> queryUserByName(Page<UserQuery> page,
                                        @Param("userName") String userName);
}
