package eqlee.ctm.finance.other.dao;

import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.finance.other.entity.Other;
import eqlee.ctm.finance.other.entity.vo.OtherUpdateVo;
import eqlee.ctm.finance.other.entity.vo.OtherVo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qf
 * @date 2019/12/4
 * @vesion 1.0
 **/
@Component
public interface OtherMapper extends IBaseMapper<Other> {

   /**
    * 查询所有其他收费
    * @return
    */
   List<OtherVo> queryOther();

   /**
    * 批量修改
    * @param list
    * @return
    */
   Integer updateOther(List<OtherUpdateVo> list);
}
