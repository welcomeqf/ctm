package eqlee.ctm.msg.message.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @author qf
 * @date 2019/11/14
 * @vesion 1.0
 **/
@Data
public class MsgAddVo {

   /**
    * 发送人
    */
   private Long createId;

   /**
    * 接收人
    */
   private List<Long> toId;

   /**
    * 消息类型
    */
   private Integer msgType;

   /**
    * 消息名称
    */
   private String msg;
}
