package eqlee.ctm.msg.websocket;

import eqlee.ctm.msg.websocket.handle.ConnWebSocketHandle;
import eqlee.ctm.msg.websocket.handle.HertWebSocketHandle;
import eqlee.ctm.msg.websocket.handle.TextWebSocketHandle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author qf
 * @date 2019/12/3
 * @vesion 1.0
 **/
@Component
@Slf4j
public class WebSocketServer implements CommandLineRunner {

   /**
    * 创建主从线程池
    */
   private static final EventLoopGroup MASTER_GROUP= new NioEventLoopGroup();
   private static final EventLoopGroup SLAVE_GROUP = new NioEventLoopGroup();

   private final String ip = "";

   private final int port = 1350;

   private Channel channel;

   @Autowired
   private TextWebSocketHandle textWebSocketHandle;

   @Autowired
   private ConnWebSocketHandle connWebSocketHandle;

   @Autowired
   private HertWebSocketHandle hertWebSocketHandle;

   /**
    * 初始化服务器
    *
    * @return
    */
   private ChannelFuture init() {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap
            .group(MASTER_GROUP,SLAVE_GROUP)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer() {

               @Override
               protected void initChannel(Channel channel) throws Exception {
                  ChannelPipeline pipeline = channel.pipeline();

                  pipeline.addLast(new HttpServerCodec());
                  pipeline.addLast(new HttpObjectAggregator(512 * 1024));
                  pipeline.addLast(new WebSocketServerProtocolHandler("/ctm"));
                  pipeline.addLast(new ReadTimeoutHandler(120, TimeUnit.SECONDS));

                  //文本帧  检查协议
                  pipeline.addLast(textWebSocketHandle);
                  //握手连接
                  pipeline.addLast(connWebSocketHandle);
                  //心跳消息
                  pipeline.addLast(hertWebSocketHandle);


               }
            });

      ChannelFuture future = bootstrap.bind(port);

      future.addListener(new ChannelFutureListener() {
         @Override
         public void operationComplete(ChannelFuture channelFuture) throws Exception {

            if (channelFuture.isSuccess()) {
               //开启连接
               System.out.println("WebSocket服务已经启动，端口为：" + port);
               channel = future.channel();


            } else {
               //关闭连接
               destory ();

               Throwable e = channelFuture.cause();
               e.printStackTrace();
            }
         }
      });


      return future;
   }

   /**
    * 销毁线程
    * 关闭连接
    */
   public void destory () {
      if (channel != null && channel.isActive()) {
         channel.close();
      }
      MASTER_GROUP.shutdownGracefully();
      SLAVE_GROUP.shutdownGracefully();
   }

   @Override
   public void run(String... args) throws Exception {

      ChannelFuture channelFuture = init();

      Runtime.getRuntime().addShutdownHook(new Thread(){

         @Override
         public void run() {
            destory();
         }
      });

      //同步堵塞
      channelFuture.channel().closeFuture().syncUninterruptibly();
   }
}
