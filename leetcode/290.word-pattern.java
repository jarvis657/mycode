/*
 * @lc app=leetcode id=290 lang=java
 *
 * [290] Word Pattern
 */

// @lc code=start

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Solution {
    public boolean wordPattern(String pattern, String s) {
        String[] words = s.split(" ");
        if (words.length != pattern.length())
            return false;
        Map<Object,Integer> index = new HashMap<>();
        for (Integer i = 0; i < words.length; ++i) {
            if (index.put(pattern.charAt(i), i) != index.put(words[i], i)) {
                return false;
            }
        }
        return true;
    }
   // public boolean myWordPattern(String pattern, String s) {
    //     String[] splits = s.split("\\s+");
    //     if (pattern.length() != splits.length) {
    //         return false;
    //     }
    //     Map<String, Character> pn = new HashMap<String, Character>();
    //     Set<Character> exists = new HashSet<Character>();
    //     int pi = 0;
    //     for (String p : splits) {
    //         char c = pattern.charAt(pi++);
    //         Character cm = pn.get(p);
    //         if (cm != null && !cm.equals(c)) {
    //             return false;
    //         } else if (cm == null && !exists.contains(c)) {
    //             pn.put(p, c);
    //             exists.add(c);
    //             continue;
    //         } else if (cm == null && exists.contains(c)) {
    //             return false;
    //         }
    //     }
    //     return true;
    // } 
}
// @lc code=end
