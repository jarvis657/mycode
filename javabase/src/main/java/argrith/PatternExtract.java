package argrith;// 根据 字符串模板表达式 随机生成 字符串实例

// 如:
// 模板 xxx 可生成 000~999
// 模板 [10-52]xxx 可生成 10000~52999 
// 模板 M[C-Z][33-66] 可生成 MC33~MZ66

// 模板规则说明:
// x: 0-9 随机数字
// [n1-n2]: 数字范围占位符 生成 [n1,n2] 区间内的任意一个正整数
// [A-Z]: 字母范围占位符 随机生成 A-Z 任意字母
// 其他字符: 原样保留

// 假定输入模板肯定符合格式要求
// 无需处理格式异常

// 应用场景 - 生成全球各国邮编：
// 德国 xxxxx
// 西班牙 [10-52]xxx
// 中国 [0-8][0-7]xxxx
// 加拿大 M[3-4]C [0-1]C[1-4]

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// 调用方法：
// makeString("[10-52]xxx-AC-xx"); // 返回 "15673-AC-82"
public class PatternExtract {
    static class KV implements Comparable<KV> {
        // 当前pattern在string的位置
        int offset;
        // 当前patter的值
        String value;

        KV(int offset, String value) {
            this.offset = offset;
            this.value = value;
        }

        @Override
        public int compareTo(KV o) {
            return this.offset - o.offset;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof KV))
                return false;
            KV kv = (KV) o;
            return offset == kv.offset && Objects.equals(value, kv.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(offset, value);
        }
    }

    public static void main(String[] args) {
        // 目前只限定一个字符的情况
//        String ts = "[A-Z]xxaxb[11-52]cdxxe[C-X]fxgh";
//        String ts = "xxxxx";
//        String ts = "[10-52]xxx-AC-xx";
//        String ts = "[0-8][0-7]xxxx";
//        String ts = "M[3-4]C [0-1]C[1-4]";
        String ts = "M[C-Z][33-66]";
        if (ts == null || ts.length() <= 2) {
            return;
        }
        Map<KV, KV> patternMap = parsePatternMap(ts);
        //TODO 这里可以加 pattern验证
        String result = genRandomString(ts, patternMap);
        System.out.println(result);
    }

    private static String genRandomString(String ts, Map<KV, KV> patternMap) {
        Set<KV> kvs = patternMap.keySet();
        StringBuilder sb = new StringBuilder();
        int preOffset = 0;
        for (Iterator<KV> iterator = kvs.iterator(); iterator.hasNext(); ) {
            KV beginKV = iterator.next();
            KV endKV = patternMap.get(beginKV);
            int begin = beginKV.offset;
            int end = endKV.offset;
            // 因为 有可能有空格原因 所以 用substring截取替换
            String x = "";
            String bvTrim = StringUtils.trim(beginKV.value);
            String evTrim = StringUtils.trim(endKV.value);
            String substring = ts.substring(preOffset, begin);
            if (StringUtils.isNumeric(bvTrim) && evTrim != null) {
                int b = NumberUtils.toInt(bvTrim);
                int e = NumberUtils.toInt(evTrim);
                int i = RandomUtils.nextInt(b, e + 1);
                x = i + "";
            } else if (
                    evTrim != null &&
                            bvTrim.length() == 1 && Character.isUpperCase(bvTrim.charAt(0))
                            &&
                            evTrim.length() == 1 && Character.isUpperCase(evTrim.charAt(0))
            ) {
                char b = bvTrim.charAt(0);
                char e = evTrim.charAt(0);
                int c = RandomUtils.nextInt(b, e + 1);
                x = (char) c + "";
            } else if (evTrim == null) {
                IntStream chars = bvTrim.chars();
                x = chars.mapToObj(c -> RandomUtils.nextInt(0, 9) + "").collect(Collectors.joining());
            }
            System.out.println(bvTrim + " " + evTrim + ":" + x);
            sb.append(substring).append(x);
            preOffset = end;
        }
        if (preOffset < ts.length() - 1) {
            sb.append(ts.substring(preOffset + 1));
        }
        return sb.toString();
    }

    /**
     * 返回pattern的kv对，[begin,end) 这样的左包右闭区间
     * @param ts
     * @return
     */
    private static Map<KV, KV> parsePatternMap(String ts) {
        int length = ts.length();
        Map<KV, KV> patternMap = new TreeMap<>();
        int preOffset = 0;
        String pattenMode = "";
        for (int end = 0; end < length; end++) {
            if ('x' == ts.charAt(end)) {
                pattenMode = "x";
            } else if ('[' == ts.charAt(end)) {
                pattenMode = "[";
            }
            if (pattenMode.length() == 0 ||
                    ('x' == ts.charAt(end) && end != ts.length() - 1) ||
                    (pattenMode.equalsIgnoreCase("[") && ']' != ts.charAt(end))
            ) {
                continue;
            }
            for (int start = preOffset; start <= end; start++) {
                if ('[' == ts.charAt(start)) {
                    int offset = start + 1;
                    String pattern = ts.substring(offset, end);
                    String[] split = pattern.split("-");
                    // start是[ 的开始offset, end 是 ] 的offset
                    patternMap.put(new KV(start, split[0]), new KV(end + 1, split.length == 2 ? split[1] : null));
                    pattenMode = "";
                    //】后面的继续
                    preOffset = end + 1;
                    start = end + 1;
                    break;
                }
                if ('x' == ts.charAt(start)) {
                    String pattern = ts.substring(start, end);
                    if (start == end || end == ts.length() - 1) {
                        pattern = ts.substring(start, end + 1);
                    }
                    patternMap.put(new KV(start, pattern), new KV(end == ts.length() - 1 ? end + 1 : end, null));
                    preOffset = end;
                    pattenMode = "";
                    break;
                }
            }
        }
        return patternMap;
    }

    public static String makeString(String template) {
        // template.indexOf(template)
        return "";
    }

}
