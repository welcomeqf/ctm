package eqlee.ctm.resource.system.service;

import eqlee.ctm.resource.system.entity.SystemConfig;
import eqlee.ctm.resource.system.entity.vo.SystemCompanyVo;
import eqlee.ctm.resource.system.entity.vo.SystemFileVo;
import eqlee.ctm.resource.system.entity.vo.SystemResultCompanyVo;

import java.util.List;

/**
 * @author qf
 * @date 2019/12/25
 * @vesion 1.0
 **/
public interface ISystemConfigService {

   /**
    * 增加配置
    * @param config
    */
   void insertConfig (SystemConfig config);

   /**
    * 查询系统配置的公司信息
    * @return
    */
   List<SystemCompanyVo> querySystemConfig ();

   /**
    * 批量修改
    * @param list
    */
   void allUpdateConfig (List<SystemCompanyVo> list);


   /**
    * 同行查询的数据
    * @return
    */
   List<SystemResultCompanyVo> queryCompanyInfo ();

   /**
    * 公司文件
    * @param sort
    * @return
    */
   SystemFileVo queryFile (Integer sort);

}
