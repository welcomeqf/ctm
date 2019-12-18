package eqlee.ctm.resource.region.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import eqlee.ctm.resource.region.dao.RegionMapper;
import eqlee.ctm.resource.region.entity.Region;
import eqlee.ctm.resource.region.entity.query.RegionUpdateQuery;
import eqlee.ctm.resource.region.service.IRegionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author qf
 * @date 2019/11/20
 * @vesion 1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements IRegionService {


   /**
    * 增加
    * @param region
    */
   @Override
   public void addRegion(Region region) {

      LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<Region>()
            .eq(Region::getRegionName,region.getRegionName());

      Region one = baseMapper.selectOne(wrapper);

      if (one != null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "该区域名已存在");
      }

      int insert = baseMapper.insert(region);

      if (insert <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "增加失败");
      }
   }

   @Override
   public void deleteRegion(Long id) {
      int i = baseMapper.deleteById(id);

      if (i <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "删除失败");
      }
   }

   @Override
   public List<Region> queryRegion() {
      return baseMapper.selectList(null);
   }

   /**
    * 修改区域
    * @param query
    */
   @Override
   public void updateRegion(RegionUpdateQuery query) {

      Region region = new Region();
      region.setId(query.getId());
      region.setRegionName(query.getRegionName());

      int byId = baseMapper.updateById(region);

      if (byId <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "修改失败");
      }
   }
}
