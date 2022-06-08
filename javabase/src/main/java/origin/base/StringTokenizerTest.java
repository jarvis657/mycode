package origin.base;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @Author:jarvmuqiliu
 * @Date: 2022/5/17
 * @Desc:
 **/
public class StringTokenizerTest {

    public static void main(String[] args) throws InterruptedException {
        try {
//            StringTokenizer stringTokenizer = new StringTokenizer("1,2,3;4,5,6;7", "@");
            StringTokenizer stringTokenizer = new StringTokenizer(null, "@");
            while (stringTokenizer.hasMoreTokens()) {
                String s = stringTokenizer.nextToken();
                System.out.println(s);
            }
            List<String> dimRepoOwners = new ArrayList<>();
            System.out.println(dimRepoOwners.contains("aa"));
        } finally {
            System.out.println("final: bein sleeping");
            try {
                Thread.currentThread().sleep(1000 * 100);
            } catch (InterruptedException e) {
                System.out.println("final: inner e");
            }
            System.out.println("final: sleep done");
        }
        System.out.println("done...................");
    }
}
