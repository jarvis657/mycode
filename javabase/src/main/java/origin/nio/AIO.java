package origin.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.concurrent.*;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Random;

/**
 * 1 IO，NIO，AIO区别
 * IO 阻塞同步通信模式，客户端和服务器连接需要三次握手，使用简单，但吞吐量小
 * NIO 非阻塞同步通信模式，客户端与服务器通过Channel连接，采用多路复用器轮询注册的Channel。提高吞吐量和可靠性。
 * AIO 非阻塞异步通信模式，NIO的升级版，采用异步通道实现异步通信，其read和write方法均是异步方法。
 * <p>
 * 2 Stock通信的伪代码实现流程
 * 服务器绑定端口：server = new ServerSocket(PORT)
 * 服务器阻塞监听：socket = server.accept()
 * 服务器开启线程：new Thread(Handle handle)
 * 服务器读写数据：BufferedReader PrintWriter
 * 客户端绑定IP和PORT：new Socket(IP_ADDRESS, PORT)
 * 客户端传输接收数据：BufferedReader PrintWriter
 * <p>
 * 3 TCP协议与UDP协议有什么区别
 * TCP : 传输控制协议是基于连接的协议，在正式收发数据前，必须和对方建立可靠的连接。速度慢，合适传输大量数据。
 * UDP : 用户数据报协议是与TCP相对应的协议。面向非连接的协议，不与对方建立连接，而是直接就把数据包发送过去，速度快，适合传输少量数据。
 * <p>
 * 4 什么是同步阻塞BIO，同步非阻塞NIO，异步非阻塞AIO
 * 同步阻塞IO : 用户进程发起一个IO操作以后，必须等待IO操作的真正完成后，才能继续运行。
 * 同步非阻塞IO: 用户进程发起一个IO操作以后，可做其它事情，但用户进程需要经常询问IO操作是否完成，这样造成不必要的CPU资源浪费。
 * 异步非阻塞IO: 用户进程发起一个IO操作然后，立即返回，等IO操作真正的完成以后，应用程序会得到IO操作完成的通知。类比Future模式。
 * 总结
 * <p>
 * 1 BIO模型中通过Socket和ServerSocket完成套接字通道实现。阻塞，同步，连接耗时。
 * 2 NIO模型中通过SocketChannel和ServerSocketChannel完成套接字通道实现。非阻塞/阻塞，同步，避免TCP建立连接使用三次握手带来的开销。
 * 3 AIO模型中通过AsynchronousSocketChannel和AsynchronousServerSocketChannel完成套接字通道实现。非阻塞，异步。
 *
 * @Author:lmq
 * @Date: 2019/9/15
 * @Desc:
 **/
public class AIO {
    public static void main(String[] args) {
        ITDragonAIOServer server = new ITDragonAIOServer();
        server.start(8888);
    }

    /**
     * AIO, 也叫 NIO2.0 是一种异步非阻塞的通信方式
     * AIO 引入了异步通道的概念 AsynchronousServerSocketChannel和AsynchronousSocketChannel 其read和write方法返回值类型是Future对象。
     */
    public static class ITDragonAIOServer {

        private ExecutorService executorService;          // 线程池
        private AsynchronousChannelGroup threadGroup;      // 通道组
        public AsynchronousServerSocketChannel asynServerSocketChannel;  // 服务器通道

        public void start(Integer port) {
            try {
                // 1.创建一个缓存池
                executorService = Executors.newCachedThreadPool();
                // 2.创建通道组
                threadGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService, 1);
                // 3.创建服务器通道
                asynServerSocketChannel = AsynchronousServerSocketChannel.open(threadGroup);
                // 4.进行绑定
                asynServerSocketChannel.bind(new InetSocketAddress(port));
                System.out.println("server start , port : " + port);
                // 5.等待客户端请求
                asynServerSocketChannel.accept(this, new ITDragonAIOServerHandler());
                // 一直阻塞 不让服务器停止，真实环境是在tomcat下运行，所以不需要这行代码
                Thread.sleep(Integer.MAX_VALUE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class ITDragonAIOServerHandler implements CompletionHandler<AsynchronousSocketChannel, ITDragonAIOServer> {
        private final Integer BUFFER_SIZE = 1024;

        @Override
        public void completed(AsynchronousSocketChannel asynSocketChannel, ITDragonAIOServer attachment) {
            // 保证多个客户端都可以阻塞
            attachment.asynServerSocketChannel.accept(attachment, this);
            read(asynSocketChannel);
        }

        //读取数据
        private void read(final AsynchronousSocketChannel asynSocketChannel) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            asynSocketChannel.read(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer resultSize, ByteBuffer attachment) {
                    //进行读取之后,重置标识位
                    attachment.flip();
                    //获取读取的数据
                    String resultData = new String(attachment.array()).trim();
                    System.out.println("Server -> " + "收到客户端的数据信息为:" + resultData);
                    String response = resultData + " = ";
                    write(asynSocketChannel, response);
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    exc.printStackTrace();
                }
            });
        }

        // 写入数据
        private void write(AsynchronousSocketChannel asynSocketChannel, String response) {
            try {
                // 把数据写入到缓冲区中
                ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
                buf.put(response.getBytes());
                buf.flip();
                // 在从缓冲区写入到通道中
                asynSocketChannel.write(buf).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void failed(Throwable exc, ITDragonAIOServer attachment) {
            exc.printStackTrace();
        }
    }


    public class ITDragonAIOClient implements Runnable {
        private Integer PORT = 8888;
        private String IP_ADDRESS = "127.0.0.1";
        private AsynchronousSocketChannel asynSocketChannel;

        public ITDragonAIOClient() throws Exception {
            asynSocketChannel = AsynchronousSocketChannel.open();  // 打开通道
        }

        public void connect() {
            asynSocketChannel.connect(new InetSocketAddress(IP_ADDRESS, PORT));  // 创建连接 和NIO一样
        }

        public void write(String request) {
            try {
                asynSocketChannel.write(ByteBuffer.wrap(request.getBytes())).get();
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                asynSocketChannel.read(byteBuffer).get();
                byteBuffer.flip();
                byte[] respByte = new byte[byteBuffer.remaining()];
                byteBuffer.get(respByte); // 将缓冲区的数据放入到 byte数组中
                System.out.println(new String(respByte, "utf-8").trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
            }
        }

        public void main(String[] args) throws Exception {
            for (int i = 0; i < 10; i++) {
                ITDragonAIOClient myClient = new ITDragonAIOClient();
                myClient.connect();
                new Thread(myClient, "myClient").start();
                String[] operators = {"+", "-", "*", "/"};
                Random random = new Random(System.currentTimeMillis());
                String expression = random.nextInt(10) + operators[random.nextInt(4)] + (random.nextInt(10) + 1);
                myClient.write(expression);
            }
        }
    }
}
