package eqlee.ctm.msg.websocket.handle;

import com.alibaba.fastjson.JSONObject;
import com.yq.constanct.CodeType;
import com.yq.entity.vo.WebSokcetVo;
import com.yq.exception.ApplicationException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

/**
 * @author qf
 * @date 2019/12/13
 * @vesion 1.0
 **/
@Component
@ChannelHandler.Sharable
public class TextWebSocketHandle extends SimpleChannelInboundHandler<TextWebSocketFrame> {


   @Override
   protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {


      //获得channel对象
      Channel channel = ctx.channel();

      //得到收到的信息
      String text = textWebSocketFrame.text();

      WebSokcetVo vo = null;
      try {
         vo = JSONObject.parseObject(text, WebSokcetVo.class);
         if (vo.getType() != 2) {
            //收到消息
            //非心跳消息
            System.out.println(vo);
         }
      }catch (Exception e) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "传的参数协议错误");
      }

      if (vo != null && vo.getType() >0 && vo.getToId() != null) {

         //符合协议 往后透传
         ctx.fireChannelRead(vo);
      } else {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "传的参数错误,请检查");
      }

   }
}
