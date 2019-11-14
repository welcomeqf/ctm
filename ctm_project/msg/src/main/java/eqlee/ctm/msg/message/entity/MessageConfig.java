package eqlee.ctm.msg.message.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author qf
 * @date 2019/11/13
 * @vesion 1.0
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("MessageConfig")
public class MessageConfig extends Model<MessageConfig> {

   /**
    * id
    */
   @TableField(exist = false)
   private Integer Id;

   /**
    * 消息类型
    */
   private Integer MsgType;

   /**
    * 消息类型名称
    */
   private String MsgTitle;

   /**
    * 消息内容
    */
   private String MsgContent;

   /**
    * 消息具体配置
    */
   private String MsgRemark;
}
