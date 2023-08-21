package origin.perf;

import java.util.Arrays;
import java.util.List;

/**
 * @Author:lmq
 * @Date: 2020/10/19
 * @Desc:
 **/
public class Per {

    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        integers.forEach(a -> {
            System.out.println(a);
            if (a > 5) {
                return;
            }
        });
        int fibonacci = fibonacci(1000000, 0);
        System.out.println(fibonacci);
    }

    public static int fibonacci(int a, int sum) {
        if (a == 0) return sum;
        return fibonacci(a - 1, sum + a);
    }
}
