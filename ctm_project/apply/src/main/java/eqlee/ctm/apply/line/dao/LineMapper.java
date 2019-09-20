package eqlee.ctm.apply.line.dao;

import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.apply.line.entity.Line;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/17
 * @Version 1.0
 */
@Component
public interface LineMapper extends IBaseMapper<Line> {

}
