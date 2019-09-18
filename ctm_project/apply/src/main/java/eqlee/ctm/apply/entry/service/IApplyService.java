package eqlee.ctm.apply.entry.service;

import eqlee.ctm.apply.entry.entity.Apply;
import eqlee.ctm.apply.entry.entity.vo.ApplyVo;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/17
 * @Version 1.0
 */
public interface IApplyService {

    /**
     * 报名
     * @param applyVo
     */
    void insertApply(ApplyVo applyVo);

    List<Apply> listApply();
}
