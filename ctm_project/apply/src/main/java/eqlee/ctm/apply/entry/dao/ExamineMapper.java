package eqlee.ctm.apply.entry.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.apply.entry.entity.Examine;
import eqlee.ctm.apply.entry.entity.vo.ExamineUpdateInfoVo;
import org.springframework.stereotype.Component;

/**
 * @Author qf
 * @Date 2019/9/20
 * @Version 1.0
 */
@Component
public interface ExamineMapper extends IBaseMapper<Examine> {

    /**
     * 展示修改记录
     * @param page
     * @return
     */
    Page<ExamineUpdateInfoVo> listUpdateInfo (Page<ExamineUpdateInfoVo> page);


    /**
     * 查询修改记录详情
     * @param Id
     * @return
     */
    ExamineUpdateInfoVo queryUpdateInfo (Long Id);

    /**
     * 查询财务未审核条数
     * @return
     */
    Integer queryNoExaCount ();
}
