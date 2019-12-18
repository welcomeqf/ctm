package eqlee.ctm.apply.message.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/11/13
 * @vesion 1.0
 **/
@Data
public class MsgVo {

   /**
    * 发送人
    */
   private Long createId;

   /**
    * 接收人
    */
   private Long toId;

   /**
    * 消息类型
    */
   private Integer msgType;

   /**
    * 消息名称
    */
   private String msg;
}
