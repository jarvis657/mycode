/*
 * @lc app=leetcode id=278 lang=java
 *
 * [278] First Bad Version
 */

// @lc code=start
/* The isBadVersion API is defined in the parent class VersionControl.
      boolean isBadVersion(int version); */

public class Solution extends VersionControl {
    public int firstBadVersion(int n) {
        int left = 1;
        int end = n;
        while (end - left > 1) {
            int cur = (left + end) / 2;
            boolean isBad = isBadVersion(cur);
            if (isBad) {
                end = cur;
            } else {
                left = cur;
            }
            cur = (left + end) / 2;
        }
        return end;
    }
}
// @lc code=end
