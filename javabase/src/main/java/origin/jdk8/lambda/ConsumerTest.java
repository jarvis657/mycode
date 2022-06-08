package origin.jdk8.lambda;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import origin.utils.Jacksons;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author muqi.lmq
 * @date 19/03/2018.
 */
public class ConsumerTest {
    @Autowired
    private ResourceLoader resourceLoader;

    public static void main(String[] args) {
        ConsumerTest consumerTest = new ConsumerTest();
        Consumer<Integer> consumer = i -> System.out.println(i);
        IntConsumer intConsumer = i -> System.out.println(i);
        consumerTest.test(consumer);  // 面向对象的方式
        consumerTest.test(consumer::accept); // 函数式的方式
        consumerTest.test(intConsumer::accept); // 函数式的方式

    }

    public void test() throws IOException {
        Resource resource = resourceLoader.getResource("path");
        File cf = resource.getFile();
        List<String> strings = Files.readAllLines(cf.toPath());
        Map<Integer, String> collect = strings.stream().map(s -> {
            String[] kv = StringUtils.split(s, ",");
            Map.Entry<Integer, String> entry = new AbstractMap.SimpleEntry<>(Integer.valueOf(kv[0]), kv[1]);
            return entry;
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        //key可以是list
        Map<List<Integer>, Long> dataMap = collect.keySet().parallelStream().map(d -> {
            return null;
        }).collect(Collectors.groupingBy(b -> Arrays.asList(1, 2, 3), Collectors.counting()));
    }

    public void test(Consumer<? super Integer> consumer) {
        System.out.println(consumer instanceof IntConsumer);
        consumer.accept(100);
    }

    public void testGroup() {
        List<String> datas = Arrays.asList("20160304,141728,2342,9145189435856852120,116.352728,39.897975,41.0,268.0,6,0,0,0,,");
        ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, List<String>>> map = datas.parallelStream().map(d -> d.split(",", 5))
                .collect(Collectors.groupingByConcurrent(dd -> dd[2], ConcurrentSkipListMap::new,
                        Collectors.groupingByConcurrent(ddd -> ddd[3], ConcurrentSkipListMap::new,
                                Collectors.mapping((String[] dddd) -> dddd[0] + dddd[1] + "," + dddd[4], Collectors.toList())
                        )));
    }
    public void testGroup2(){
        JsonNode node = Jacksons.getJsonNode("");
        Map<Integer, String> sourceGroupMap = StreamSupport.stream(node.get("data").get("dictionary").get("sourceGroup").spliterator(), true).map(d -> {
            Integer sourceId = d.get("value").asInt();
            String chineseName = d.get("label").asText();
            AbstractMap.SimpleEntry<Integer, String> se = new AbstractMap.SimpleEntry(sourceId, chineseName);
            return se;
        }).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));


        Map<JsonNode, Map<Integer, String>> map = StreamSupport.stream(node.get("data").get("dictionary").get("eventSource").spliterator(), true)
                .collect(Collectors.groupingBy(d -> d.get("eventSourceGroup"), Collectors.mapping(d -> {
                    Integer sourceId = d.get("sourceId").asInt();
                    String chineseName = d.get("chineseName").asText();
                    AbstractMap.SimpleEntry<Integer, String> se = new AbstractMap.SimpleEntry(sourceId, chineseName);
                    return se;
                }, Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue))));
        System.out.println(map);
    }
}
