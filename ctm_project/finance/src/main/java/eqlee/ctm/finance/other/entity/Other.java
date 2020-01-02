package eqlee.ctm.finance.other.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2019/12/4
 * @vesion 1.0
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("Other")
public class Other extends Model<Other> {

   /**
    * ID
    */
   private Long Id;

   /**
    * 紧急通知标题
    */
   private String OtherName;

   /**
    * 紧急通知内容
    */
   private String OtherContent;

   /**
    * 0-不展示 1-展示
    */
   private Integer Types;

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
