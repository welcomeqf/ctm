package eqlee.ctm.apply.line.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.apply.line.entity.Line;
import eqlee.ctm.apply.line.entity.query.LineSeacherQuery;
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
     * 默认分页查询线路
     * @param page
     * @return
     */
    Page<LineSeacherQuery> queryLine2Page(Page<LineSeacherQuery> page);

    /**
     * 根据线路名模糊查询
     * @param page
     * @param lineName
     * @return
     */
    Page<LineSeacherQuery> queryLine2PageAndName(Page<LineSeacherQuery> page,
                                                 @Param("lineName") String lineName);


    /**
     * 默认分页查询可报名线路
     * @param page
     * @return
     */
    Page<LineSeacherQuery> queryLine(Page<LineSeacherQuery> page);

    /**
     * 根据线路名模糊查询可报名线路
     * @param page
     * @param lineName
     * @return
     */
    Page<LineSeacherQuery> queryLineAndName(Page<LineSeacherQuery> page,
                                                 @Param("lineName") String lineName);



    /**
     *   查询该城市下的所有线路
     * @param list
     * @return
     */
    List<Line> queryLocalCity (List<String> list);

}
