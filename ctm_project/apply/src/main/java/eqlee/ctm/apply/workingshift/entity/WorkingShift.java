package eqlee.ctm.apply.workingshift.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2020/1/15
 * @vesion 1.0
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("WorkingShift")
public class WorkingShift extends Model<WorkingShift> {

   /**
    * 主键ID
    */
   private Long Id;

   /**
    * 备注
    */
   private String Remark;

   /**
    * 班次名称
    */
   private String ShiftName;

   /**
    * 创建人
    */
   private Long CreateUserId;

   /**
    * 创建时间
    */
   private LocalDateTime CreateDate;

   /**
    * 修改人
    */
   private Long UpdateUserId;

   /**
    * 修改时间
    */
   private LocalDateTime UpdateDate;
}
