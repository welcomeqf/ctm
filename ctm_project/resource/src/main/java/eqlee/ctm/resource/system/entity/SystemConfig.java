package eqlee.ctm.resource.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author qf
 * @date 2019/12/25
 * @vesion 1.0
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("SystemConfig")
public class SystemConfig extends Model<SystemConfig> {

   private Long Id;

   /**
    * 0--文本  1--图片
    */
   private Integer Types;

   /**
    * 描述
    */
   private String Description;

   /**
    * 键(全英文）
    */
   private String Nos;

   /**
    * 值
    */
   private String Valuess;

   /**
    * 备注
    */
   private String Remark;

   /**
    * 是否开放给同行(0-不开放 1-开放)
    */
   private Boolean IsPublic;

   /**
    * 排序
    */
   private Integer Sort;

   /**
    * 是否系统级隐藏
    */
   private Boolean IsSystemic;
}
