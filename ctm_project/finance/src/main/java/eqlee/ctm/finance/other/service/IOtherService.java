package eqlee.ctm.finance.other.service;


import eqlee.ctm.finance.other.entity.Other;
import eqlee.ctm.finance.other.entity.vo.OtherQueryVo;
import eqlee.ctm.finance.other.entity.vo.OtherUpdateVo;
import eqlee.ctm.finance.other.entity.vo.OtherVo;

import java.util.List;

/**
 * @author qf
 * @date 2019/12/4
 * @vesion 1.0
 **/
public interface IOtherService {

   /**
    * 增加其他收费名称
    * 修改
    * @param other
    */
   void addOther(OtherVo other);

   /**
    * 删除
    * @param id
    */
   void deleteOther(Long id);

   /**
    * 查询所有
    * @return
    */
   List<OtherVo> queryOther();

}
