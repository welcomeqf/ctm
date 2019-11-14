package eqlee.ctm.msg.message.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/11/13
 * @vesion 1.0
 **/
@Data
public class MsgConfigVo {

   /**
    * 消息类型
    */
   private Integer msgType;

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
}
