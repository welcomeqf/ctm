package eqlee.ctm.apply.guider.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.guider.entity.vo.GuiderVo;

/**
 * @Author Claire
 * @Date 2019/9/23 0023
 * @Version 1.0
 */
public interface IGuiderService {

    /**
     * 导游首页展示
     * @param current
     * @param size
     * @return
     */
    Page<GuiderVo> guiderIndex(Integer current,Integer size);
}
