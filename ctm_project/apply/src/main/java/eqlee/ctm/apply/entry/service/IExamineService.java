package eqlee.ctm.apply.entry.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.entry.entity.Examine;
import eqlee.ctm.apply.entry.entity.query.ExaMqAdminQuery;
import eqlee.ctm.apply.entry.entity.vo.ExamineAddVo;
import eqlee.ctm.apply.entry.entity.vo.ExamineInfoVo;
import eqlee.ctm.apply.entry.entity.vo.ExamineUpdateInfoVo;
import eqlee.ctm.apply.entry.entity.vo.ExamineVo;

/**
 * 审核接口
 * @Author qf
 * @Date 2019/9/20
 * @Version 1.0
 */
public interface IExamineService {

    /**
     * 增加一条取消报名记录审核的记录
     * @param ApplyId
     */
    void CancelExamine(Long ApplyId);

    /**
     * 修改报名表
     * @param examineVo
     */
    void UpdateApplyExamine(ExamineVo examineVo);

    /**
     * 通过取消报名表的审核
     * @param ApplyId
     */
    void AdoptCancelExamine(Long ApplyId);

    /**
     * 拒绝取消报名表的审核
     * @param ApplyId
     */
    void NotAdoptCancelExamine (Long ApplyId);


    /**
     * 不通过报名审核
     * @param ApplyId
     */
    void NotAdoptExamine(Long ApplyId);

    /**
     *  通过报名表的审核
     * @param ApplyId
     */
    void doptExamine(Long ApplyId);


    /**
     * 增加审核记录
     * @param vo
     */
    void insertExamine (ExamineAddVo vo);


    /**
     * 修改记录展示
     * @param page
     * @return
     */
    Page<ExamineUpdateInfoVo> listUpdateInfo (Page<ExamineUpdateInfoVo> page);


    /**
     * 查询修改记录详情
     * @param Id
     * @return
     */
    ExamineInfoVo queryUpdateInfo (Long Id);

}
