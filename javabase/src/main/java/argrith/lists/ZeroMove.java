package argrith.lists;

import java.util.Arrays;

/**
 * @Author:lmq
 * @Date: 2023/9/29
 * @Desc:
 **/
public class ZeroMove {


    public static void move(int[] nums) {
        int i = 0, j = 0;
        for (; j < nums.length; j++) {
            if (nums[j] != 0) {
                nums[i] = nums[j];
                i++;
            }
            j++;
        }

        int k = i;

        while (k < nums.length) {
            nums[k] = 0;
            k++;
        }
    }


    public static void main(String[] args) {

        int[] nums = new int[]{0, 1, 0, 3, 12};
        ZeroMove.move(nums);
        System.out.println(Arrays.toString(nums));
    }
}
