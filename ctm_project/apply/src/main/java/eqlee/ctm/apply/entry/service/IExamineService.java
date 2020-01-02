package eqlee.ctm.apply.entry.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.entry.entity.Examine;
import eqlee.ctm.apply.entry.entity.query.ApplyNoReadCountQuery;
import eqlee.ctm.apply.entry.entity.query.ExaApplyResultQuery;
import eqlee.ctm.apply.entry.entity.query.ExaMqAdminQuery;
import eqlee.ctm.apply.entry.entity.vo.*;
import eqlee.ctm.apply.line.entity.vo.ResultVo;

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
     * @return
     */
    ExaApplyResultQuery AdoptCancelExamine(Long ApplyId);

    /**
     * 拒绝取消报名表的审核
     * @param ApplyId
     * @param exaRemark
     * @return
     */
    ExaApplyResultQuery NotAdoptCancelExamine (Long ApplyId, String exaRemark);


    /**
     * 不通过报名审核
     * @param ApplyId
     * @param exaRemark
     * @return
     */
    ExaApplyResultQuery NotAdoptExamine(Long ApplyId, String exaRemark);

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

    /**
     * 查询未读的条数
     * @param toId
     * @param msgType
     * @param msg
     * @return
     */
    ApplyNoReadCountQuery queryNoReadCount (Long toId, Integer msgType,String msg);

    /**
     * 修改当前用户的所有未读消息状态
     * @return
     */
    ResultVo updateLocalMsgStatus ();

    /**
     * 查询所有未审核信息
     * @return
     */
    ApplyNoReadCountQuery queryAllNoExaCount ();

    /**
     * 查看备注
     * @param examineType
     * @param applyId
     * @return
     */
    ResultVo queryRemark (String examineType, Long applyId);

}
