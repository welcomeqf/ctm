package eqlee.ctm.apply.workingshift.entity.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author qf
 * @date 2020/1/15
 * @vesion 1.0
 **/
@Data
public class WorkingShiftVo {

   private Long id;
   /**
    * 班次名称
    */
   private String ShiftName;

   /**
    * 备注
    */
   private String Remark;
}
