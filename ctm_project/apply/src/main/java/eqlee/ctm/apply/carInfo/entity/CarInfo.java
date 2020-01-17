package eqlee.ctm.apply.carInfo.entity;

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
@TableName("CarInfo")
public class CarInfo extends Model<CarInfo> {

   /**
    * 主键ID
    */
   private Long Id;

   /**
    * 车辆ID
    */
   private Long CarId;

   /**
    * 订单Id
    */
   private Long OrderId;

   /**
    * 出行日期
    */
   private LocalDate OutDate;

   /**
    * 车牌号
    */
   private String CarNo;

   /**
    * 创建人
    */
   private Long CreateUserId;

   /**
    * 创建时间
    */
   private LocalDateTime CreateDate;

   /**
    * 备注
    */
   private String Remark;
}
