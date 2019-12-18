package eqlee.ctm.msg.websocket.handle;

import com.yq.entity.vo.WebSokcetVo;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

/**
 * @author qf
 * @date 2019/12/13
 * @vesion 1.0
 **/
@Component
@ChannelHandler.Sharable
public class HertWebSocketHandle extends SimpleChannelInboundHandler<WebSokcetVo> {



   @Override
   protected void channelRead0(ChannelHandlerContext ctx, WebSokcetVo vo) throws Exception {

      if (vo.getType() == 2) {
         System.out.println("心跳消息");
      } else {
         //往后透传
         ctx.fireChannelRead(vo);
      }
   }
}
