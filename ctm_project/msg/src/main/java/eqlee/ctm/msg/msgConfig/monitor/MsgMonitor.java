package eqlee.ctm.msg.msgConfig.monitor;

import com.alibaba.fastjson.JSON;
import com.yq.constanct.CodeType;
import com.yq.entity.vo.WebSokcetVo;
import com.yq.exception.ApplicationException;
import eqlee.ctm.msg.message.entity.MsgIdList;
import eqlee.ctm.msg.message.entity.MsgResult;
import eqlee.ctm.msg.message.servise.IMsgService;
import eqlee.ctm.msg.websocket.group.GroupUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qf
 * @date 2019/12/13
 * @vesion 1.0
 **/
@Component
public class MsgMonitor {

   @Autowired
   private GroupUtils nettyUtils;

   @Autowired
   private IMsgService msgService;

   public void sendMsg () {

      List<MsgResult> msgResults = msgService.queryMsg();

//      System.out.println(msgResults);

      for (MsgResult result : msgResults) {
         //发送消息
         WebSokcetVo vo = new WebSokcetVo();
         vo.setToId(result.getToId());
         vo.setMsg(result.getMsg());
         vo.setType(result.getId());

         //根据id找到通道
         if (vo.getToId() == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "业务异常");
         }
         //找到连接
         Channel channel = nettyUtils.getChannel(vo.getToId());

         if (channel != null) {
            channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(vo)));
            //修改数据库未读状态
            msgService.updateMsgStatus(result.getId());
         }

      }



   }
}
