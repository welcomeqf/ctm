package eqlee.ctm.apply.line.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.line.entity.Line;
import eqlee.ctm.apply.line.entity.query.LinePageQuery;
import eqlee.ctm.apply.line.entity.vo.LineVo;

/**
 * @Author qf
 * @Date 2019/9/17
 * @Version 1.0
 */
public interface ILineService {

    /**
     * 根据线路名查询线路
     * @param LineName
     * @return
     */
    Line queryLineByName(String LineName);

    /**
     * 增加线路
     * @param lineVo
     */
    void insertLine(LineVo lineVo);

    /**
     * 修改线路
     * @param line
     */
    void updateLine(Line line);

    /**
     * 查询所有线路
     * @return
     */
    Page<Line> listPageLine(LinePageQuery query);

    /**
     * 停用线路
     * @param id
     */
    void updateStatus(Long id);

    /**
     * 启用线路
     * @param id
     */
    void updateNormal(Long id);
}
