package eqlee.ctm.apply.line.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.line.entity.Line;
import eqlee.ctm.apply.line.entity.query.LinePageQuery;
import eqlee.ctm.apply.line.entity.query.LineSeacherQuery;
import eqlee.ctm.apply.line.entity.vo.LineInfomationVo;
import eqlee.ctm.apply.line.entity.vo.LineUpdateVo;
import eqlee.ctm.apply.line.entity.vo.LineVo;

import java.util.List;

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
     * @param Id
     */
    void updateLine(LineUpdateVo line, Long Id);

    /**
     * 查询所有线路
     * @param query
     * @param lineName
     * @return
     */
    Page<LineSeacherQuery> listPageLine(Page<LineSeacherQuery> query, String lineName);

    /**
     * 查询所有可报名线路
     * @param query
     * @param lineName
     * @return
     */
    Page<LineSeacherQuery> pageLine(Page<LineSeacherQuery> query, String lineName);

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


    /**
     * 根据Id查询一条线路
     * @param Id
     * @return
     */
    Line queryOneLine (Long Id);


    /**
     * 根据Id删除线路
     * @param Id
     */
    void deleteLine (Long Id);

    /**
     * 查询所有线路
     * @return
     */
    List<Line> listAllLine ();

    /**
     * 查询所有的线路
     * @return
     */
    List<Line> queryAllLine ();

    /**
     * 查询图文
     * @param id
     * @return
     */
    LineInfomationVo queryContent (Long id);


    /**
     * 查询所有线路
     * @param list
     * @return
     */
    List<Line> queryByIdList (List<Long> list);


    /**
     * 查询自己所在城市的所有线路
     * @return
     */
    List<Line> queryLocalCityLine ();



}
