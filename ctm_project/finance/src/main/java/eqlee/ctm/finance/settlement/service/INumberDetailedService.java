package eqlee.ctm.finance.settlement.service;

import eqlee.ctm.finance.settlement.entity.vo.ContectUserNumberVo;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/27
 * @Version 1.0
 */
public interface INumberDetailedService {


    void insertAllNumberDetailed(List<ContectUserNumberVo> vo);
}
