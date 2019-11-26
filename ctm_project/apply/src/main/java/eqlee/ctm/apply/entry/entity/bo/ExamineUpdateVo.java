package eqlee.ctm.apply.entry.entity.bo;

import eqlee.ctm.apply.entry.entity.vo.UpdateInfoVo;
import lombok.Data;

/**
 * @author qf
 * @date 2019/11/25
 * @vesion 1.0
 **/
@Data
public class ExamineUpdateVo {

   private Long id;

   /**
    * 修改人
    */
   private String updateUserName;

   /**
    * 修改时间
    */
   private String updateDate;

   /**
    * 修改内容
    */
   private UpdateInfoVo infoVo;
}
