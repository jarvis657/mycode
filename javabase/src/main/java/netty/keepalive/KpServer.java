package netty.keepalive;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author:lmq
 * @Date: 2020/12/9
 * @Desc:
 **/
@Slf4j
public class KpServer {

    private int port;

    public KpServer(int port) {
        this.port = port;
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap server = new ServerBootstrap().group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerChannelInitializer());

        try {
            ChannelFuture future = server.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server start fail", e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        KpServer server = new KpServer(7788);
        server.start();
    }

    private class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            ChannelPipeline pipeline = socketChannel.pipeline();
            pipeline.addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
            // 字符串解码 和 编码
            pipeline.addLast("decoder", new StringDecoder());
            pipeline.addLast("encoder", new StringEncoder());

            // 自己的逻辑Handler
            pipeline.addLast("handler", new KpServerHandler());
        }

        private class KpServerHandler extends SimpleChannelInboundHandler {
            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                log.info("server channelActive");
            }


            @Override
            protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
                String message = (String) msg;
                if ("heartbeat".equals(message)) {
                    log.info(ctx.channel().remoteAddress() + "===>server: " + message);
                    ctx.write("heartbeat");
                    ctx.flush();
                }
            }

            /**
             * 如果5s没有读请求，则向客户端发送心跳
             *
             * @param ctx
             * @param evt
             * @throws Exception
             */
            @Override
            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                if (evt instanceof IdleStateEvent) {
                    IdleStateEvent event = (IdleStateEvent) evt;
                    if (IdleState.READER_IDLE.equals((event.state()))) {
                        ctx.writeAndFlush("heartbeat").addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                    }
                }
                super.userEventTriggered(ctx, evt);
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                super.exceptionCaught(ctx, cause);
                ctx.close();
            }
        }
    }
}
