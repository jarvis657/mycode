package argrith;

/**
 * @Author:lmq
 * @Date: 2023/1/3
 * @Desc:
 **/
public class StringTest {

    public static boolean isMerge(String s, String part1, String part2) {
        // 在这⾥写代码
        char[] cs = s.toCharArray();
        //双指针归并处理
        int i = 0;//part1的index
        int j = 0;//part2的index
        for (int k = 0; k < cs.length; k++) {
            if (cs[k] == part1.charAt(i)) {
                i++;
                continue;
            }
            if (cs[k] == part2.charAt(j)) {
                j++;
                continue;
            }
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        int parent = (0 - 1) >>> 1;
        int p1 = (1 - 1) >>> 1;
        int p2 = (2 - 1) >>> 1;
        int p3 = (3 - 1) >>> 1;
        int p4 = (4 - 1) >>> 1;

        System.out.println(parent);
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);
        System.out.println(p4);
        System.out.println(isMerge("saomehow", "show", "ome"));
    }
}
