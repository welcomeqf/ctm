package eqlee.ctm.apply.guider.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.guider.entity.vo.GuiderVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @Author Claire
 * @Date 2019/9/23 0023
 * @Version 1.0
 */
@Component
public interface GuiderMapper {
     Page<GuiderVo> guiderIndex(@Param("page") Page<GuiderVo> page);
}
