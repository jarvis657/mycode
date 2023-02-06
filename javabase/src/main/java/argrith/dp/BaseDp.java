package argrith.dp;

import java.util.Arrays;

/**
 * @Author:lmq
 * @Date: 2021/1/5
 * @Desc:
 **/
public class BaseDp {
    //  fn= a*n b*n c*n  -> target ;
    /*
    1,2,5  20
    f(20) =  Min(19+ 1 , 18+2, 15+5) + 1(1,2,5)
    f(10) = Min(f(9) ,f(8),f(15) +1(1,2,5) ...
    f(1) = Min(0,0,0)+1(1)
     */
    public static int calc(int[] arras, int target) {
        int[] result = new int[target + 1];
        for (int i = 0; i < target + 1; i++) {
            result[i] = Integer.MAX_VALUE;
        }
        result[0] = 0;
        for (int i = 1; i <= target; i++) {
            int minCounter = Integer.MAX_VALUE;
            for (int j = 0; j < arras.length; j++) {
                if (i - arras[j] >= 0 && result[i - arras[j]] != Integer.MAX_VALUE) {
                    minCounter = Math.min(result[i - arras[j]], minCounter) + 1;
                }
            }
            result[i] = minCounter;
        }
        System.out.println(Arrays.deepToString(new int[][]{result}));
        return result[target] != Integer.MAX_VALUE ? result[target] : -1;
    }

    public static void main(String[] args) {
        int[] arras = {1, 2,3, 5, 10};
        int result = 11;
        int calc = calc(arras, result);
        System.out.println(calc);
    }
}

