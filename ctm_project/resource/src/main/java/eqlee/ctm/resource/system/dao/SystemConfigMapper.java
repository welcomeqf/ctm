package eqlee.ctm.resource.system.dao;

import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.resource.system.entity.SystemConfig;
import eqlee.ctm.resource.system.entity.vo.SystemCompanyVo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qf
 * @date 2019/12/25
 * @vesion 1.0
 **/
@Component
public interface SystemConfigMapper extends IBaseMapper<SystemConfig> {


   /**
    * 批量修改配置信息
    * @param list
    * @return
    */
   Integer allUpdateConfig (List<SystemCompanyVo> list);
}
