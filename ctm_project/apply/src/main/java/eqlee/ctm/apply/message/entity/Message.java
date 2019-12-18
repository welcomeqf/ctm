package eqlee.ctm.apply.message.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2019/11/13
 * @vesion 1.0
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("Message")
public class Message extends Model<Message> {

   /**
    * id
    */
   @TableField(exist = false)
   private Integer Id;

   /**
    * 发送人
    */
   private Long CreateId;

   /**
    * 接收人
    */
   private Long ToId;

   /**
    * 是否已读
    */
   private Boolean IsRead;

   /**
    * 创建时间
    */
   private LocalDateTime CreateDate;

   /**
    * 查看时间
    */
   private LocalDateTime ReadDate;

   /**
    * 消息类型
    */
   private Integer MsgType;

   /**
    * 消息名称
    */
   private String Msg;
}
