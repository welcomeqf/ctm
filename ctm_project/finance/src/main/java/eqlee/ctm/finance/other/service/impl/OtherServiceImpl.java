package eqlee.ctm.finance.other.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.IdGenerator;
import eqlee.ctm.finance.other.dao.OtherMapper;
import eqlee.ctm.finance.other.entity.Other;
import eqlee.ctm.finance.other.entity.vo.OtherPageVo;
import eqlee.ctm.finance.other.entity.vo.OtherQueryVo;
import eqlee.ctm.finance.other.entity.vo.OtherUpdateVo;
import eqlee.ctm.finance.other.entity.vo.OtherVo;
import eqlee.ctm.finance.other.service.IOtherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

   @Autowired
   private LocalUser localUser;

   /**
    * 增加修改紧急通知
    * @param otherVo
    */
   @Override
   public void addOther(OtherVo otherVo) {

      Other other = new Other();

      other.setTypes(0);
      //修改所有的状态
      baseMapper.update(other, null);

      if (otherVo.getId() == null) {
         //增加
         other.setId(idGenerator.getNumberId());

         UserLoginQuery user = localUser.getUser();
         other.setCreateUserId(user.getId());
         other.setUpdateUserId(user.getId());
         other.setOtherName(otherVo.getOtherName());
         other.setOtherContent(otherVo.getOtherContent());
         other.setTypes(otherVo.getTypes());

         int i = baseMapper.insert(other);

         if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改失败");
         }
      }

      if (otherVo.getId() != null) {
         //修改
         other.setId(otherVo.getId());

         UserLoginQuery user = localUser.getUser();
         other.setUpdateUserId(user.getId());
         other.setOtherName(otherVo.getOtherName());
         other.setOtherContent(otherVo.getOtherContent());
         other.setTypes(otherVo.getTypes());

         int i = baseMapper.updateById(other);

         if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改失败");
         }
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
    * 查询选定的数据
    * @return
    */
   @Override
   public OtherVo queryOtherByFirst() {
      LambdaQueryWrapper<Other> wrapper = new LambdaQueryWrapper<Other>()
            .eq(Other::getTypes,1);
      Other other = baseMapper.selectOne(wrapper);

      OtherVo vo = new OtherVo();
      vo.setId(other.getId());
      vo.setOtherName(other.getOtherName());
      vo.setOtherContent(other.getOtherContent());
      return vo;
   }

   /**
    * 查询所有
    * @param vo
    * @return
    */
   @Override
   public Page<Other> queryPageOther(OtherPageVo vo) {

      Page<Other> page = new Page<>();
      page.setCurrent(vo.getCurrent());
      page.setSize(vo.getSize());

      LambdaQueryWrapper<Other> wrapper = new LambdaQueryWrapper<Other>()
            .orderByDesc(Other::getCreateDate);

      baseMapper.selectPage(page,wrapper);
      return page;
   }

}
