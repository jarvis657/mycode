package origin.utils;

import com.google.common.util.concurrent.RateLimiter;

/**
 * @Author:lmq
 * @Date: 2020/9/26
 * @Desc:
 **/
public class RateLimiterTest {
    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(2);
        for (int i = 0; i < 5; i++) {
            //acquire方法传入的是需要的令牌个数，当令牌不足时会进行等待，该方法返回的是等待的时间
            double waitTime = rateLimiter.acquire(1);
            System.out.println(System.currentTimeMillis() / 1000 + " , " + waitTime);
        }
    }
}
