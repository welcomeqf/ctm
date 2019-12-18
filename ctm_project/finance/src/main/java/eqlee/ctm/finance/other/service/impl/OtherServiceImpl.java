package eqlee.ctm.finance.other.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.utils.IdGenerator;
import eqlee.ctm.finance.other.dao.OtherMapper;
import eqlee.ctm.finance.other.entity.Other;
import eqlee.ctm.finance.other.entity.vo.OtherQueryVo;
import eqlee.ctm.finance.other.entity.vo.OtherUpdateVo;
import eqlee.ctm.finance.other.entity.vo.OtherVo;
import eqlee.ctm.finance.other.service.IOtherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author qf
 * @date 2019/12/4
 * @vesion 1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class OtherServiceImpl extends ServiceImpl<OtherMapper, Other> implements IOtherService {


   IdGenerator idGenerator = new IdGenerator();

   /**
    * 增加修改
    * @param other
    */
   @Override
   public void addOther(OtherVo other) {
      if (other.getId() == null) {
         //增加
         Other other1 = new Other();
         other1.setId(idGenerator.getNumberId());
         other1.setOtherName(other.getOtherName());
         int insert = baseMapper.insert(other1);

         if (insert <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "增加失败");
         }
         return;
      }

      Other other1 = new Other();
      other1.setId(other.getId());
      other1.setOtherName(other.getOtherName());
      int i = baseMapper.updateById(other1);

      if (i <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "修改失败");
      }
   }

   @Override
   public void deleteOther(Long id) {
      int i = baseMapper.deleteById(id);

      if (i <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "删除失败");
      }
   }

   @Override
   public List<OtherVo> queryOther() {
      return baseMapper.queryOther();
   }

   /**
    * 装配其他收入表
    * @param vo
    */
   @Override
   public void updateOther(List<OtherUpdateVo> vo) {
      Integer integer = baseMapper.updateOther(vo);

      if (integer <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "装配其他收入失败");
      }

   }

   /**
    * 查询结算的其他收入具体信息
    * @param incomeId
    * @return
    */
   @Override
   public List<Other> queryOtherByIncome(Long incomeId) {
      LambdaQueryWrapper<Other> wrapper = new LambdaQueryWrapper<Other>()
            .eq(Other::getIncomeId,incomeId);
      return baseMapper.selectList(wrapper);
   }
}
