package eqlee.ctm.finance.settlement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import eqlee.ctm.finance.settlement.dao.Out2FinanceMapper;
import eqlee.ctm.finance.settlement.entity.Outcome2;
import eqlee.ctm.finance.settlement.entity.bo.OutComeInfoBo;
import eqlee.ctm.finance.settlement.service.IOut2FinanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author qf
 * @date 2019/12/26
 * @vesion 1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class Out2FinanceServiceImpl extends ServiceImpl<Out2FinanceMapper, Outcome2> implements IOut2FinanceService {


   /**
    * 批量增加支出信息
    * @param list
    */
   @Override
   public void insertOut2Info(List<OutComeInfoBo> list) {
      baseMapper.insertOut2Info(list);
   }


   /**
    * 查询一个团所有支出
    * @param incomeId
    * @return
    */
   @Override
   public List<Outcome2> queryOut(Long incomeId) {
      LambdaQueryWrapper<Outcome2> wrapper = new LambdaQueryWrapper<Outcome2>()
            .eq(Outcome2::getIncomeId,incomeId);
      return baseMapper.selectList(wrapper);
   }


   /**
    * 删除
    * @param incomeId
    */
   @Override
   public void deleteOut(Long incomeId) {
      LambdaQueryWrapper<Outcome2> wrapper = new LambdaQueryWrapper<Outcome2>()
            .eq(Outcome2::getIncomeId,incomeId);
      int delete = baseMapper.delete(wrapper);

      if (delete <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "提交有误");
      }
   }
}
