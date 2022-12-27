package netty.base;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @Date: 2020/6/1 11:12
 * @Description: 通用handler，处理I/O事件
 * 1）@ChannelHandler.Sharable：这个注解是为了线程安全，如果你不在乎是否线程安全，不加也可以；
 * 2）SimpleChannelInboundHandler：这里的类型可以是ByteBuf，也可以是String，还可以是对象，根据实际情况来；
 * 3）channelRead0：消息读取方法，注意名称中有个0；
 * 4）ChannelHandlerContext：通道上下文，代指Channel；
 * 5）ByteBuf：字节序列，通过ByteBuf操作基础的字节数组和缓冲区，因为JDK原生操作字节麻烦、效率低，所以Netty对字节的操作进行了封装，实现了指数级的性能提升，同时使用更加便利；
 * 6）CharsetUtil.UTF_8：这个是JDK原生的方法，用于指定字节数组转换为字符串时的编码格式。
 */
@ChannelHandler.Sharable
public class HandlerClientHello extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        /**
         * @Description 处理接收到的消息
         **/
        System.out.println("接收到的消息：" + byteBuf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        /**
         * @Description 处理I/O事件的异常
         **/
        cause.printStackTrace();
        ctx.close();
    }
}
