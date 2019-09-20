package eqlee.ctm.apply.entry.service;

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
     * 增加一条修改报名表的记录
     * @param examineVo
     */
    void UpdateApplyExamine(ExamineVo examineVo);

    /**
     * 通过取消报名表的审核
     * @param ApplyId
     */
    void AdoptCancelExamine(Long ApplyId);

    /**
     * 通过修改报名表记录的审核
     * @param ApplyId
     */
    void AdoptUpdateExamine(Long ApplyId);

    /**
     * 不通过取消报名表的审核
     * @param ApplyId
     */
    void NotAdoptExamine(Long ApplyId);


}
