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

    /**
     * 查询一天的线路
     * @param startDate
     * @param endDate
     * @return
     */
    List<LineQuery> listLine(@Param("startDate") LocalDateTime startDate,
                             @Param("endDate") LocalDateTime endDate);

    /**
     * 查询所有线路
     * @return
     */
    List<LineQuery> listAllLine();
}
