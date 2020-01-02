package eqlee.ctm.resource.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import eqlee.ctm.resource.system.dao.SystemConfigMapper;
import eqlee.ctm.resource.system.entity.SystemConfig;
import eqlee.ctm.resource.system.entity.vo.SystemCompanyVo;
import eqlee.ctm.resource.system.entity.vo.SystemFileVo;
import eqlee.ctm.resource.system.entity.vo.SystemResultCompanyVo;
import eqlee.ctm.resource.system.service.ISystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qf
 * @date 2019/12/25
 * @vesion 1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements ISystemConfigService {


   @Override
   public void insertConfig(SystemConfig config) {
      int insert = baseMapper.insert(config);

      if (insert <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "增加失败");
      }
   }


   /**
    * 查询配置信息
    * @return
    */
   @Override
   public List<SystemCompanyVo> querySystemConfig() {

      List<SystemConfig> configList = baseMapper.selectList(null);

      List<SystemCompanyVo> result = new ArrayList<>();
      for (SystemConfig config : configList) {
         SystemCompanyVo vo = new SystemCompanyVo();
         vo.setId(config.getId());
         vo.setDescription(config.getDescription());
         vo.setIsPublic(config.getIsPublic());
         vo.setNo(config.getNos());
         vo.setRemark(config.getRemark());
         vo.setType(config.getTypes());
         vo.setValue(config.getValuess());
         result.add(vo);
      }

      return result;
   }

   @Override
   public void allUpdateConfig(List<SystemCompanyVo> list) {

      Integer integer = baseMapper.allUpdateConfig(list);

      if (integer <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "修改失败");
      }
   }


   /**
    * 同行查询的数据
    * @return
    */
   @Override
   public List<SystemResultCompanyVo> queryCompanyInfo() {
      LambdaQueryWrapper<SystemConfig> wrapper = new LambdaQueryWrapper<SystemConfig>()
            .eq(SystemConfig::getIsPublic,1);

      List<SystemConfig> list = baseMapper.selectList(wrapper);

      List<SystemResultCompanyVo> result = new ArrayList<>();
      for (SystemConfig config : list) {
         SystemResultCompanyVo vo = new SystemResultCompanyVo();
         vo.setId(config.getId());
         vo.setDescription(config.getDescription());
         vo.setNo(config.getNos());
         vo.setRemark(config.getRemark());
         vo.setType(config.getTypes());
         vo.setValue(config.getValuess());
         result.add(vo);
      }
      return result;
   }

   /**
    * 公司文件
    * @return
    */
   @Override
   public SystemFileVo queryFile(Integer sort) {
      LambdaQueryWrapper<SystemConfig> wrapper = new LambdaQueryWrapper<SystemConfig>()
            .eq(SystemConfig::getTypes,1)
            .eq(SystemConfig::getSort,sort);
      SystemConfig config = baseMapper.selectOne(wrapper);
      SystemFileVo vo = new SystemFileVo();
      vo.setPicture(config.getValuess());
      return vo;
   }
}
