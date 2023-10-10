import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Test {
    public boolean wordPattern(String pattern, String s) {
        String[] words = s.split(" ");
        if (words.length != pattern.length())
            return false;
        Map<Object, Integer> index = new HashMap<>();
        for (Integer i = 0; i < words.length; ++i) {
            if (index.put(pattern.charAt(i), i) != index.put(words[i], i)) {
                return false;
            }
        }
        return true;
    }

    public int firstBadVersion(int n) {
        int left = 1;
        int end = n;
        while (end - left >= 1) {
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
    private boolean isBadVersion(int cur) {
        return cur >= 4;
    }

    public static void main(String[] args) {
        Test test = new Test();
        // boolean d = test.wordPattern("abba", "dog cat cat dog");
        int firstBadVersion = test.firstBadVersion(20);
        System.out.println(firstBadVersion);
    }
}
