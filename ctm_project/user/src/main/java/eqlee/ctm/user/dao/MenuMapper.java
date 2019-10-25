package eqlee.ctm.user.dao;

import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.user.entity.UserMenu;
import eqlee.ctm.user.entity.query.UserMenuWithQuery;
import eqlee.ctm.user.entity.query.WithQuery;
import eqlee.ctm.user.entity.query.menuIdQuery;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
@Component
public interface MenuMapper extends IBaseMapper<UserMenu> {

    /**
     * 查询所有菜单
     * @return
     */
    List<WithQuery> queryMenu ();

    /**
     * 查询所有
     * @return
     */
    List<WithQuery> queryListMenu ();
}
