package argrith;

import java.util.stream.Stream;

/**
 * @Author:lmq
 * @Date: 2020/3/2
 * @Desc:
 **/
public class DpTest {
    /**
     * 1,5,11 3种钱, 拼出15 最少要几张
     * f(n) = min{f(n-1),f(n-5),f(n-11)} + 1   最后一张 是1,5,11的时候最少f(w)  逆向推
     *
     * @param args
     */
    public static void main(String[] args) {
        f(15);
        System.out.println("========================");
        long b = System.currentTimeMillis();
        long fq = fq(200);
        long b2 = System.currentTimeMillis();
        long l = fq_fast(200);
        long b3 = System.currentTimeMillis();
        Long integer = Stream.iterate(new long[]{1, 1}, t -> new long[]{t[1], t[0] + t[1]}).limit(200).map(t -> t[0]).skip(199).findFirst().get();
        long b4 = System.currentTimeMillis();
        System.out.println("cost:" + (b2 - b));
        System.out.println("cost:" + (b3 - b2));
        System.out.println("cost:" + (b4 - b3));
        System.out.println(l + "=====l-fq=======" + fq);
        System.out.println("=======" + integer);

    }

    //我从哪里来 逆推 dp 最优子结构,重复
    public static void f(int n) {
        int[] fs = new int[16];
        for (int i = 1; i <= n; i++) {
            int cost = Integer.MAX_VALUE;
            if (i - 5 >= 0) {
                cost = Math.min(cost, fs[i - 5] + 1);
            }
            if (i - 11 >= 0) {
                cost = Math.min(cost, fs[i - 11] + 1);
            }
            if (i - 1 >= 0) {
                cost = Math.min(cost, fs[i - 1] + 1);
            }
            fs[i] = cost;
            System.out.println("f[" + i + "] : " + cost);
        }
    }

    //fibnaqi
    // an = a[n-1] + a[n-2]
    public static long fq(int n) {
        long[] fs = new long[200];
        if (n <= 2) {
            return 1;
        }
        fs[0] = 1;
        fs[1] = 1;
        for (int i = 2; i < n; i++) {
            fs[i] = fs[i - 1] + fs[i - 2];
        }
        return fs[n-1];
    }

    // an = a[n-1] + a[n-2]
    public static long fq_fast(int n) {
        if (n <= 2) {
            return 1;
        }
        long before = 1;
        long b = 1;
        long tmp = 0;
        for (int i = 2; i < n; i++) {
            tmp = b;
            b = before + b;
            before = tmp;
        }
        return b;
    }

}
