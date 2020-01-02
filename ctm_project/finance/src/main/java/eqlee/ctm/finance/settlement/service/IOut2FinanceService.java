package eqlee.ctm.finance.settlement.service;

import eqlee.ctm.finance.settlement.entity.Outcome2;
import eqlee.ctm.finance.settlement.entity.bo.OutComeInfoBo;

import java.util.List;

/**
 * @author qf
 * @date 2019/12/26
 * @vesion 1.0
 **/
public interface IOut2FinanceService {

   /**
    * 批量增加
    * @param list
    */
   void insertOut2Info (List<OutComeInfoBo> list);

   /**
    * 查询一个团的所有支出
    * @param incomeId
    * @return
    */
   List<Outcome2> queryOut (Long incomeId);


   /**
    * 删除支出
    * @param incomeId
    */
   void deleteOut (Long incomeId);


}
