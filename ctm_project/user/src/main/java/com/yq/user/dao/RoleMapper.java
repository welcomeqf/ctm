package com.yq.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yq.user.entity.UserRole;
import org.springframework.stereotype.Component;

/**
 * @Author qf
 * @Date 2019/9/10
 * @Version 1.0
 */
@Component
public interface RoleMapper extends BaseMapper<UserRole> {

    /**
     * 添加
     * @param RoleName
     * @return
     */
    Integer add(String RoleName);
}
