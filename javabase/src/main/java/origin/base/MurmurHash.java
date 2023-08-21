package origin.base;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @Author:lmq
 * @Date: 2020/3/25
 * @Desc:
 **/
public class MurmurHash {
    public static void main(String[] args) {
        String key = "SP3Dvw40eMyP4ejSFHZlgU9GvKZ7SB0MUNoCqb7r+/k=";
        System.out.println(Long.MAX_VALUE - hash(key));
    }
    public static long hash(String key) {

        ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
        int seed = 0x1234ABCD;

        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (buf.remaining() * m);

        long k;
        while (buf.remaining() >= 8) {
            k = buf.getLong();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            // for big-endian version, do this first:
            // finish.position(8-buf.remaining());
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        buf.order(byteOrder);
        return h;
    }
}
