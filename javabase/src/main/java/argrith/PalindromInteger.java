package argrith;

/**
 * @Author:lmq
 * @Date: 2020/3/1
 * @Desc:
 **/
public class PalindromInteger {
    public static boolean isPalindrome(int x) {
        if (x < 0) {
            return false;
        }
        String s = String.valueOf(x);
        boolean e = s.length() % 2 == 0;
        if (e) {
            return p(s.length() / 2 - 1, s.length()/2 , s);
        } else {
            return p(s.length() / 2, s.length() / 2, s);
        }
    }

    public static boolean p(int start, int end, String s) {
        while (start >= 0 && end < s.length() && s.charAt(start) == s.charAt(end)) {
            start--;
            end++;
        }
        if (start > 0 || end < s.length()) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        boolean palindrome = isPalindrome(101101);
        System.out.println(palindrome);
    }
}
