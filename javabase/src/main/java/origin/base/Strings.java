package origin.base;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author:lmq
 * @Date: 2022/4/7
 * @Desc:
 **/
public class Strings {

    private Function<String, String> f = (a) -> {
        System.out.println(a);
        return a;
    };

    public static void main(String[] args) {
        List<String> strings = Arrays.asList("https://123.com", "http://455.com");
        List<String> list = strings.stream().map(d -> {
            String s = d.replaceAll("http[s]?://", "");
            return s;
        }).collect(Collectors.toList());
        System.out.println(list);
    }
}
