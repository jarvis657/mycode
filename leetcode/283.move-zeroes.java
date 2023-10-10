/*
 * @lc app=leetcode id=283 lang=java
 *
 * [283] Move Zeroes
 */

// @lc code=start
class Solution {
    public void moveZeroes(int[] nums) {
        if (nums == null || nums.length == 0) {
            return;
        }
        int cur = 0;
        for (int n : nums) {
            if (n != 0) {
                nums[cur++] = n;
            }
        }
        while (cur < nums.length) {
            nums[cur++] = 0;
        }
    }
}
// @lc code=end
