package eqlee.ctm.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import eqlee.ctm.user.dao.UserOptionMapper;
import eqlee.ctm.user.entity.UserOption;
import eqlee.ctm.user.service.IUserOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author qf
 * @Date 2019/10/23
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserOptionServiceImpl extends ServiceImpl<UserOptionMapper, UserOption> implements IUserOptionService {


    /**
     * 增加权限相关
     * @param userOption
     */
    @Override
    public void insertOption(UserOption userOption) {
        int insert = baseMapper.insert(userOption);

        if (insert <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加失败");
        }
    }

    /**
     * 修改权限状态
     * @param roleId
     * @param menuId
     */
    @Override
    public void updateStart(Long roleId, Long menuId, Boolean start) {

        LambdaQueryWrapper<UserOption> queryWrapper = new LambdaQueryWrapper<UserOption>()
                .eq(UserOption::getRoleId,roleId)
                .eq(UserOption::getMenuId,menuId);

        UserOption userOption = new UserOption();
        userOption.setStarted(start);

        int update = baseMapper.update(userOption, queryWrapper);

        if (update <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改失败");
        }
    }
}
