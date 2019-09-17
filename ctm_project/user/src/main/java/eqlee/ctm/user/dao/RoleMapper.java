package eqlee.ctm.user.dao;

import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.user.entity.UserRole;
import org.springframework.stereotype.Component;

/**
 * @Author qf
 * @Date 2019/9/10
 * @Version 1.0
 */
@Component
public interface RoleMapper extends IBaseMapper<UserRole> {

    /**
     * 添加
     * @param RoleName
     * @return
     */
    Integer add(String RoleName);
}
