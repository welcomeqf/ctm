package eqlee.ctm.msg.message.entity.query;

import lombok.Data;

/**
 * @author qf
 * @date 2019/11/13
 * @vesion 1.0
 **/
@Data
public class MsgInfoQuery {

   private Integer id;

   /**
    * 发送人
    */
   private Long createId;

   /**
    * 接收人
    */
   private Long toId;

   /**
    * 创建时间
    */
   private String createDate;

   /**
    * 查看时间
    */
   private String readDate;

   /**
    * 消息类型名称
    */
   private String msgTitle;

   /**
    * 消息内容
    */
   private String msgContent;

   /**
    * 消息具体配置
    */
   private String msgRemark;

   /**
    * 消息名称
    */
   private String msg;
}
