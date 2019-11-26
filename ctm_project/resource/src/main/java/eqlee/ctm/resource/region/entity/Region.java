package eqlee.ctm.resource.region.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2019/11/20
 * @vesion 1.0
 **/
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
@TableName("Region")
public class Region extends Model<Region> {

   private Long Id;

   private String RegionName;

   private Long CreateUserId;

   private LocalDateTime CreateDate;

   private String CreateUser;
}
