package eqlee.ctm.resource.region.service;

import eqlee.ctm.resource.region.entity.Region;

import java.util.List;

/**
 * @author qf
 * @date 2019/11/20
 * @vesion 1.0
 **/
public interface IRegionService {

   /**
    * 增加摔选的区域
    * @param region
    */
   void addRegion (Region region);

   /**
    * 删除区域
    * @param id
    */
   void deleteRegion (Long id);

   /**
    * 查询区域
    * @return
    */
   List<Region> queryRegion ();
}
