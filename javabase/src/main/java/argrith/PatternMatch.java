package argrith;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author:lmq
 * @Date: 2022/12/27
 * @Desc:
 **/
public class PatternMatch {
    public static boolean wordPattern(String pattern, String str) {
        if (str == null || pattern == null)
            return false;

        //reflect ： 模式字符与词的匹配关系
        //strs : 切分好的词
        Map<Character, String> reflect = new HashMap<>();
        String[] strs = str.split(" ");
        if (pattern.length() != strs.length) return false;

        //遍历模式，在匹配关系中查找key
        //如果找到，比较value是否与词是否相同，如果不同，返回false
        //如果未找到，查找value，如果存在返回false，不存在则将key，value存入
        for (int i = 0; i < pattern.length(); i++) {
            boolean isHaveKey = reflect.containsKey(pattern.charAt(i));
            boolean isHaveValue = reflect.containsValue(strs[i]);
            String value = reflect.get(pattern.charAt(i));
            if (isHaveKey) {
                if (!value.equals(strs[i])) return false;
            } else {
                if (isHaveValue) return false;
                else reflect.put(pattern.charAt(i), strs[i]);
            }
        }

        //输入匹配关系
        for (Character ch : reflect.keySet()) {
            System.out.println(ch + ":" + reflect.get(ch));
        }

        return true;
    }

    public static void main(String[] args) {
        boolean flag = wordPattern("abbcad", "北京 杭州 杭州 北京1 杭州 哈哈");
        if (flag) {
            System.out.println("模式匹配");
        } else {
            System.out.println("模式不匹配");
        }
        System.out.println("##  HashMap  ##");
        Map<String , String> hm = new HashMap<String , String>();
        hm.put("1", "OOO");
        hm.put("3", "OOO");
        hm.put("2", "OOO");
        hm.put("5", "OOO");
        hm.put("4", "OOO");

        Iterator<String> it3 =  hm.keySet().iterator();

        while (it3.hasNext()) {
            System.out.println(it3.next());
        }

    }
}
