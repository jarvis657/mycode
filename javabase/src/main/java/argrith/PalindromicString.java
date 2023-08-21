package argrith;

/**
 * @Author:lmq
 * @Date: 2020/2/29
 * @Desc:
 **/
public class PalindromicString {
    private static int low = 0, high = 0;
    private static int L = 0, R = 0;

    public static String longestPalindrome(String s) {
//        if (s.length() < 2) {
//            return s;
//        }
//        for (int i = 0; i < s.length(); i++) {
//            sPalindromic(i, s);
//            dPalindromic(i, i + 1, s);
//        }
//        return s.substring(low, high + 1);
        if (s==null || s.length() ==0) {
            return "";
        }
        char[] cs = s.toCharArray();
        L = 0; R = 0;
        for (int i=0; i<cs.length-1; i++) {
            helper(cs, i, i);
            helper(cs, i, i+1);
        }
        helper(cs, cs.length-1, cs.length-1);
        return s.substring(L+1, R);
    }
    private static void helper(char[] cs, int l, int r) {
        while (l>=0 && r <cs.length && cs[l] == cs[r]) {
            l--; r++;
        }
        if (r-l > R-L) {
            R = r;
            L = l;
        }
    }

    //single
    public static void sPalindromic(int i, String s) {
        int begin = i - 1, end = i + 1;
        while (begin >= 0 && end < s.length() && s.charAt(begin) == s.charAt(end)) {
            if ((high - low) < (end - begin)) {
                low = begin;
                high = end;
            }
            begin--;
            end++;
        }
    }

    //double
    public static void dPalindromic(int i, int j, String s) {
        while (i >= 0 && j < s.length() && s.charAt(i) == s.charAt(j) ) {
            if( (high - low) < (j - i) ){
                low = i;
                high = j;
            }
            i--;
            j++;

        }
    }

    public static void main(String[] args) {
        String s = "aaaa";
        String t = longestPalindrome(s);
        System.out.println(t);
    }
}
