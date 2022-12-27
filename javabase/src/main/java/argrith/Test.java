package argrith;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author:lmq
 * @Date: 2020/12/10
 * @Desc:
 **/
public class Test {
    //    public static void main(String[] args) {
//        int[] arrs = {3, 4, 2, 6, 4, 1, 8, 9, 3,1, 56, 22};
//        quickSort(arrs, 0, arrs.length - 1);
//        System.out.println(Arrays.deepToString(new int[][]{arrs}));
//    }
//
//    public static void quickSort(int[] arrs, int start, int end) {
//        if (start < end) {
//            int index = partition(arrs, start, end);
//            quickSort(arrs, start, index - 1);
//            quickSort(arrs, index + 1, end);
//        }
//    }
//
//    private static int partition(int[] arrs, int start, int end) {
//        int p = arrs[start];
//        while (start < end) {
//            while (start < end && arrs[end] >= p) {
//                end--;
//            }
//            arrs[start] = arrs[end];
//            while (start < end && arrs[start] <= p) {
//                start++;
//            }
//            arrs[end] = arrs[start];
//        }
//        arrs[start] = p;
//        return start;
//    }
    public static void main(String[] args) {
        int[] arrs = new int[]{1, 2, 3, 4, 5, 6};
        int i = search(arrs, 6);
        System.out.println("i:" + i);
        System.out.println();
        System.out.println();
        Map<String, Object> hashMap = new ConcurrentHashMap<>();
        String k1 = "AaAa";
        String k2 = "BBBB";
        hashMap.computeIfAbsent(k1, key -> {
            System.out.println("k1 hashcode:"+k1.hashCode()+" k2 hashcode:"+k2.hashCode());
          return hashMap.computeIfAbsent(k2, k -> 43);
        });
        System.out.println(hashMap);

    }

    public static int search(int[] arrs, int target) {
        int start = 0;
        int end = arrs.length;
        int index = (start + end) / 2;
        int r = 0;
        while (start < end) {
            if (arrs[index] < target) {
                start = index;
                index = (start + end) / 2;
            } else if (arrs[index] > target) {
                end = index;
                index = (start + end) / 2;
            } else {
                System.out.println("index:" + index);
                r = index;
                break;
            }
        }
        System.out.println("r:" + r);
        return arrs[r];
    }
}
