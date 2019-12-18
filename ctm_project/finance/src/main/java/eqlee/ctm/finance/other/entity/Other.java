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
    * 收入id
    */
   private Long IncomeId;

   /**
    * 其他收费名称
    */
   private String OtherName;

   /**
    * 其他收费金额
    */
   private Double OtherPrice;

   /**
    * 创建时间
    */
   private LocalDateTime CreateDate;
}
