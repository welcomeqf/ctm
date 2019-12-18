package eqlee.ctm.apply.message.entity.query;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2019/11/13
 * @vesion 1.0
 **/
@Data
public class MsgQuery {

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

}
