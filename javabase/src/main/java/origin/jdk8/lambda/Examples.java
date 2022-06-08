package origin.jdk8.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import origin.jdk8.lambda.StreamPartitions.Domain;

/**
 * @Author:qishan
 * @Date: 2019-03-29
 * @Desc:
 **/
public class Examples {

    public static void main(String[] args) {
        List<Integer> intList = Arrays.asList(1, 2, 3);
        Integer result2 = intList.stream().reduce(100, Integer::sum);
        System.out.println(result2);
        AtomicInteger ai = new AtomicInteger();

        ConcurrentHashMap<Integer, Integer> reduce = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
                        17, 18, 19, 20, 21, 22, 23, 23).parallel()
//                .map(d -> {
//                    System.out.println("map:"+d);
//                    return d;
//                })
                .reduce(new ConcurrentHashMap<>(),
                        (integers, item) -> {
                            System.out.println(
                                    Thread.currentThread().getName() + "  " + item + ":put:" + integers.get(item - 1));
                            integers.put(item, item);
                            return integers;
                        }, (integers, integers2) -> {
//                            System.out.println(Thread.currentThread().getName()+"  "+integers.size() +" combiner "+integers2.size());
//                            if (integers != integers2) {
//                                System.out.println(ai.getAndIncrement());
//                                integers.putAll(integers2);
//                            }
//                            String ee = "";
//                            if (integers == integers2) {
//                                ee = "==";
//                            } else {
//                                ee = "##";
//                            }
//                            System.out.println(ai.getAndIncrement());
                            return integers;
                        });
        //打印结果几乎每次都不同
        System.out.println("accResult: " + reduce);
    }

    /**
     * jdk12
     */
    public void tee() {
//        Collector<CharSequence, ?, String> joiningCollector = Collectors.joining("-");
//        Collector<String, ?, List<String>> listCollector = Collectors.toList();
//        //returns joined string and individual strings as array
//        Collector<String, ?, String[]> compositeCollector = Collectors.teeing(joiningCollector, listCollector,
//                (joinedString, strings) -> {
//                    ArrayList<String> list = new ArrayList<>(strings);
//                    list.add(joinedString);
//                    String[] array = list.toArray(new String[0]);
//                    return array;
//                });
//
//        String[] strings = Stream.of("Apple", "Banana", "Orange").collect(compositeCollector);
//        System.out.println(Arrays.toString(strings));//[Apple, Banana, Orange, Apple-Banana-Orange]
//        Collector<Integer, ?, Long> summing = Collectors.summingLong(Integer::valueOf);
//        Collector<Integer, ?, Long> counting = Collectors.counting();
//
//        //example list
//        List<Integer> list = List.of(1, 2, 4, 5, 7, 8);
//
//        //collector for  map entry consisting of sum and count
//        Collector<Integer, ?, Map.Entry<Long, Long>> sumAndCountMapEntryCollector =
//                Collectors.teeing(summing, counting, Map::entry);
//        Map.Entry<Long, Long> sumAndCountMap = list.stream().collect(sumAndCountMapEntryCollector);
//        System.out.println(sumAndCountMap);
//
//        //collect sum and count as List
//        Collector<Integer, ?, List<Long>> sumAndCountListCollector =
//                Collectors.teeing(summing, counting, List::of);//(v1, v2) -> List.of(v1, v2)
//        List<Long> sumAndCountArray = list.stream().collect(sumAndCountListCollector);
//        System.out.println(sumAndCountArray);
        //27=6
        //[27, 6]
    }
}
