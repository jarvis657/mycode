package origin.nio;

import java.nio.ByteBuffer;

/**
 * @Author:lmq
 * @Date: 2020/8/2
 * @Desc:
 **/
public class BufferTest {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(100);
        show(buffer);
        buffer.put("hehe".getBytes());
        show(buffer);
        buffer.flip();//转读
        show(buffer);
        buffer.clear();//转写
        show(buffer);
        buffer.put("vvvv".getBytes());
        show(buffer);
        buffer.flip();
        show(buffer);
        buffer.compact();//压缩
    }

    private static void show(ByteBuffer buffer) {
        System.out.print("position:" + buffer.position());
        System.out.print("\t");
        System.out.print("capacity:" + buffer.capacity());
        System.out.print("\t");
        System.out.println("limit:" + buffer.limit());
        System.out.println("-----------------");
    }
}
