package eqlee.ctm.finance.settlement.service;

import eqlee.ctm.finance.settlement.entity.vo.ContectUserNumberVo;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/27
 * @Version 1.0
 */
public interface INumberDetailedService {


    /**
     * 批量增加未付款代收具体人员信息
     * @param vo
     */
    void insertAllNumberDetailed(List<ContectUserNumberVo> vo);
}
