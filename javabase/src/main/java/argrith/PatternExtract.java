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
        String ts = "aaa[11-52]bb[52-60]cc[C-X]dd";
        if (ts == null || ts.length() <= 2 || ts.indexOf("[") == -1 || ts.indexOf("]") == -1) {
            return;
        }
        Map<KV, KV> patternMap = parsePatternMap(ts);
        String result = genRandomString(ts, patternMap);
        System.out.println(result);
    }

    private static String genRandomString(String ts, Map<KV, KV> patternMap) {
        Set<KV> kvs = patternMap.keySet();
        StringBuilder sb = new StringBuilder();
        int preOffset = 0;
        for (Iterator<KV> iterator = kvs.iterator(); iterator.hasNext();) {
            KV beginKV = iterator.next();
            KV endKV = patternMap.get(beginKV);
            int begin = beginKV.offset;
            int end = endKV.offset;

            // 因为 有可能有空格原因 所以 用substring截取替换
            String x = "";
            String bvTrim = StringUtils.trim(beginKV.value);
            String evTrim = StringUtils.trim(endKV.value);
            if (StringUtils.isNumeric(bvTrim)) {
                int b = NumberUtils.toInt(bvTrim);
                int e = NumberUtils.toInt(evTrim);
                int i = RandomUtils.nextInt(b, e + 1);
                x = i + "";
            } else if (bvTrim.length() == 1 && Character.isUpperCase(bvTrim.charAt(0))
                    &&
                    evTrim.length() == 1 && Character.isUpperCase(evTrim.charAt(0))) {
                char b = bvTrim.charAt(0);
                char e = evTrim.charAt(0);
                int c = RandomUtils.nextInt(b, e + 1);
                x = (char) c + "";
            }
            String substring = ts.substring(preOffset, begin);
            System.out.println(x);
            sb.append(substring).append(x);
            preOffset = end + 1;
        }
        return sb.toString();
    }

    private static Map<KV, KV> parsePatternMap(String ts) {
        int length = ts.length();
        Map<KV, KV> patternMap = new TreeMap<>();
        int preOffset = 0;
        for (int end = 1; end < length; end++) {
            if (']' != ts.charAt(end)) {
                continue;
            }
            for (int start = preOffset; start < end; start++) {
                if ('[' == ts.charAt(start)) {
                    String pattern = ts.substring(start + 1, end);
                    String[] split = pattern.split("-");
                    // start是[ 的开始offset, end 是 ] 的offset
                    patternMap.put(new KV(start, split[0]), new KV(end, split[1]));
                    preOffset = end;
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
