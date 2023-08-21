package origin.nio;

import java.io.*;
import java.net.ServerSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

/**
 * @Author:lmq
 * @Date: 2019/9/15
 * @Desc:
 **/
public class BIO {
    /**
     * IO 也称为 BIO，Block IO 阻塞同步的通讯方式
     * 比较传统的技术，实际开发中基本上用Netty或者是AIO。熟悉BIO，NIO，体会其中变化的过程。作为一个web开发人员，stock通讯面试经常问题。
     * BIO最大的问题是：阻塞，同步。
     * BIO通讯方式很依赖于网络，若网速不好，阻塞时间会很长。每次请求都由程序执行并返回，这是同步的缺陷。
     * BIO工作流程：
     * 第一步：server端服务器启动
     * 第二步：server端服务器阻塞监听client请求
     * 第三步：server端服务器接收请求，创建线程实现任务
     */

    private static final Integer PORT = 8888; // 服务器对外的端口号

    public static void main(String[] args) {
        ServerSocket server = null;
        Socket socket = null;
        ThreadPoolExecutor executor = null;
        try {
            server = new ServerSocket(PORT); // ServerSocket 启动监听端口
            System.out.println("BIO Server 服务器启动.........");
            /*--------------传统的新增线程处理----------------*/
            /*while (true) {
                // 服务器监听：阻塞，等待Client请求
                socket = server.accept();
                System.out.println("server 服务器确认请求 : " + socket);
                // 服务器连接确认：确认Client请求后，创建线程执行任务  。很明显的问题，若每接收一次请求就要创建一个线程，显然是不合理的。
                new Thread(new ITDragonBIOServerHandler(socket)).start();
            } */
            /*--------------通过线程池处理缓解高并发给程序带来的压力（伪异步IO编程）----------------*/
            executor = new ThreadPoolExecutor(10, 100, 1000, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(50));
            while (true) {
                socket = server.accept();  // 服务器监听：阻塞，等待Client请求
                ITDragonBIOServerHandler serverHandler = new ITDragonBIOServerHandler(socket);
                executor.execute(serverHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != socket) {
                    socket.close();
                    socket = null;
                }
                if (null != server) {
                    server.close();
                    server = null;
                    System.out.println("BIO Server 服务器关闭了！！！！");
                }
                executor.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class ITDragonBIOServerHandler implements Runnable {

        private Socket socket;

        public ITDragonBIOServerHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader reader = null;
            PrintWriter writer = null;
            try {
                reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                writer = new PrintWriter(this.socket.getOutputStream(), true);
                String body = null;
                while (true) {
                    body = reader.readLine(); // 若客户端用的是 writer.print() 传值，那readerLine() 是不能获取值，细节
                    if (null == body) {
                        break;
                    }
                    System.out.println("server服务端接收参数 : " + body);
                    writer.println(body + " = ");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != writer) {
                    writer.close();
                }
                try {
                    if (null != reader) {
                        reader.close();
                    }
                    if (null != this.socket) {
                        this.socket.close();
                        this.socket = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * BIO 客户端
     * Socket :         向服务端发送连接
     * PrintWriter :     向服务端传递参数
     * BufferedReader : 从服务端接收参数
     */
    public static class ITDragonBIOClient {
        private Integer PORT = 8888;
        private String IP_ADDRESS = "127.0.0.1";

        public void main(String[] args) {
            for (int i = 0; i < 10; i++) {
                clientReq(i);
            }
        }

        private void clientReq(int i) {
            Socket socket = null;
            BufferedReader reader = null;
            PrintWriter writer = null;
            try {
                socket = new Socket(IP_ADDRESS, PORT); // Socket 发起连接操作。连接成功后，双方通过输入和输出流进行同步阻塞式通信
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 获取返回内容
                writer = new PrintWriter(socket.getOutputStream(), true);
                String[] operators = {"+", "-", "*", "/"};
                Random random = new Random(System.currentTimeMillis());
                String expression = random.nextInt(10) + operators[random.nextInt(4)] + (random.nextInt(10) + 1);
                writer.println(expression); // 向服务器端发送数据
                System.out.println(i + " 客户端打印返回数据 : " + reader.readLine());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != reader) {
                        reader.close();
                    }
                    if (null != socket) {
                        socket.close();
                        socket = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
