package eqlee.ctm.msg.websocket.handle;

import com.yq.entity.vo.WebSokcetVo;
import eqlee.ctm.msg.websocket.group.GroupUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author qf
 * @date 2019/12/13
 * @vesion 1.0
 **/
@Component
@ChannelHandler.Sharable
public class ConnWebSocketHandle extends SimpleChannelInboundHandler<WebSokcetVo> {

   @Autowired
   private GroupUtils channelUtils;

   @Override
   public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
      System.out.println("有一个新的连接");
      ctx.fireChannelRegistered();
   }


   @Override
   protected void channelRead0(ChannelHandlerContext ctx, WebSokcetVo vo) throws Exception {

      if (vo.getType() == 1) {
         //这是一个连接请求

         channelUtils.put(vo.getToId(), ctx.channel());

         System.out.println("当前连接长度：" + channelUtils.size());

      } else {

         //往后透传
         ctx.fireChannelRead(vo);
      }

   }

   @Override
   public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
      System.out.println("有一个连接下线了");
      channelUtils.removeByChannel(ctx.channel());
      ctx.fireChannelUnregistered();
   }

   @Override
   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      ctx.fireExceptionCaught(cause);
      channelUtils.removeByChannel(ctx.channel());
   }


}
