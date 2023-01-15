package argrith.arrays;

import java.util.*;

/**
 * @Author:lmq
 * @Date: 2023/1/9
 * @Desc:
 **/
public class DpArrays {
//    public static void main(String[] args) {
//        int[] numbs = new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4};
//        int s = maxSubArray(numbs);
//        System.out.println(s);
//    }
//
//    // [-2,1,-3,4,-5,2,1,-5,4]
//    public static int maxSubArray(int[] numbs) {
//        int preMax = numbs[0];
//        int resMax = preMax;
//        for (int i = 1; i < numbs.length; i++) {
//            preMax = Math.max(numbs[i], preMax + numbs[i]);
//            resMax = Math.max(resMax, preMax);
//        }
//        return resMax;
//    }

    public static boolean wordBreak(String s, List<String> wordDict) {
        int len = s.length();
        // 状态定义：以 s[i] 结尾的子字符串是否符合题意
        boolean[] dp = new boolean[len];

        // 预处理
        Set<String> wordSet = new HashSet<>();
        for (String word : wordDict) {
            wordSet.add(word);
        }

        // 动态规划问题一般都有起点，起点也相对好判断一些
        // dp[0] = wordSet.contains(s.charAt(0));
        for (int r = 0; r < len; r++) {
            if (wordSet.contains(s.substring(0, r + 1))) {
                dp[r] = true;
                continue;
            }
            for (int l = 0; l < r; l++) {
                // dp[l] 写在前面会更快一点，否则还要去切片，然后再放入 hash 表判重
                if (dp[l] && wordSet.contains(s.substring(l + 1, r + 1))) {
                    dp[r] = true;
                    // 这个 break 很重要，一旦得到 dp[r] = True ，循环不必再继续
                    break;
                }
            }
        }
        return dp[len - 1];
    }

    /**
     *
     * 暴力时间会超时
     * @param p
     * @param s
     * @param dict
     * @return
     */
    public static boolean _wordBreak(int p, String s, Set<String> dict) {
        int n = s.length();
        if (p == n) {
            return true;
        }
        for (int i = p + 1; i <= n; i++) {
            System.out.println(p);
            if (dict.contains(s.substring(p, i)) && _wordBreak(i, s, dict)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
//        String s = "leetacode";
//        List<String> wordDict = new ArrayList<>();
//        wordDict.add("leet");
//        wordDict.add("code");
//        wordDict.add("a");
//        boolean res = wordBreak(s, wordDict);
//        System.out.println(res);
//        int a = 0;
//        Boolean x = false;
//        System.out.println(a = 1);
//        System.out.println(x = false);
//        System.out.println(x = true);
        String s = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab";
        String[] ws_ = new String[]{"a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa", "aaaaaaa", "aaaaaaaa", "aaaaaaaaa", "aaaaaaaaaa","b"};
        List<String> wordDict = Arrays.asList(ws_);
        boolean b = wordBreak( s, wordDict);
        System.out.println("result: " + b);
    }


}
