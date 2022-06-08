package origin.stream;

import java.util.Comparator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


/**
 * @author muqi.lmq
 * @date 24/03/2018.
 */

public final class StreamPartitionMain {

    public static void main(String[] args) {
        Stream<String> stream = IntStream.range(10, 30).mapToObj(i -> String.format("%3d", i));
        List<String> list = stream.collect(Collectors.toList());
        System.out.println(list);
        Stream<Stream<String>> partitioned1 = partition(list, 8);
        partitioned1.map(s -> s.collect(Collectors.joining(",")))
                .forEachOrdered(System.out::println);

        // partitioning example 1
//        Stream<String> origin.stream = IntStream.range(0, 30).mapToObj(i -> String.format("%3d", i));
//        Stream<Stream<String>> partitioned1 = partition(origin.stream, 8);
//        partitioned1.map(s -> s.collect(Collectors.joining(",")))
//                    .forEachOrdered(System.out::println);

        System.out.println(" -- ");

//        // partitioning example 2
//        origin.stream = IntStream.iterate(0, i -> ++i).mapToObj(i -> String.format("%3d", i));
//        Stream<Stream<String>> partitioned2 = partition(origin.stream, 8);
//        partitioned2.map(s -> s.collect(Collectors.joining(",")))
//                    .limit(3)
//                    .forEachOrdered(System.out::println);
//
//        System.out.println(" -- ");
//
//        // heading example
//        origin.stream = IntStream.iterate(0, i -> ++i).mapToObj(i -> String.format("%3d", i));
//        Stream<String> head = head(origin.stream, 8);
//        System.out.println(head.collect(Collectors.joining(",")));
    }

    private static <T> Stream<Stream<T>> partition(List<T> list, int partitionSize) {
        return StreamSupport.stream(new PartitioningSpliterator<>(list.spliterator(), partitionSize),
                        list.parallelStream().isParallel())
                .map(sp -> StreamSupport.stream(sp, list.parallelStream().isParallel()));
    }


    private static <T> Stream<Stream<T>> partition(Stream<T> stream, int partitionSize) {
        return StreamSupport.stream(new PartitioningSpliterator<>(stream.spliterator(), partitionSize),
                        stream.isParallel())
                .map(sp -> StreamSupport.stream(sp, stream.isParallel()));
    }

    private static <T> Stream<T> head(Stream<T> stream, int size) {
        return StreamSupport.stream(new FixedSizeSpliterator<>(stream.spliterator(), size),
                stream.isParallel());
    }

    private static class PartitioningSpliterator<E> extends Spliterators.AbstractSpliterator<Spliterator<E>> {

        private final Spliterator<E> wrapped;
        private final int partitionSize;

        PartitioningSpliterator(Spliterator<E> wrapped, int partitionSize) {
            super(wrapped.estimateSize(), wrapped.characteristics() & ~Spliterator.SORTED);
            this.wrapped = wrapped;
            this.partitionSize = partitionSize;
        }

        @Override
        public boolean tryAdvance(Consumer<? super Spliterator<E>> action) {
            Spliterator<E> s = new FixedSizeSpliterator<>(wrapped, partitionSize);
            action.accept(s);
            return wrapped.estimateSize() != 0;
        }

        @Override
        public long estimateSize() {
            long e = wrapped.estimateSize();
            if (e == Long.MAX_VALUE) {
                return Long.MAX_VALUE;
            }
            long remainder = e % partitionSize;
            if (remainder == 0) {
                return e / partitionSize;
            }
            return e / partitionSize + 1;
        }
    }

    private static class FixedSizeSpliterator<E> extends Spliterators.AbstractSpliterator<E> {

        private final Spliterator<E> wrapped;
        private final long size;
        private final AtomicInteger cursor = new AtomicInteger(0);

        FixedSizeSpliterator(Spliterator<E> wrapped, int limitSize) {
            this(wrapped, size(wrapped, limitSize), wrapped.characteristics());
        }

        private FixedSizeSpliterator(Spliterator<E> wrapped, long size, int characteristics) {
            super(size, characteristics);
            this.wrapped = wrapped;
            this.size = size;
        }

        private static <E> long size(Spliterator<E> wrapped, long size) {
            long e = wrapped.estimateSize();
            if (e > size) {
                return size;
            }
            return e;
        }

        @Override
        public boolean tryAdvance(Consumer<? super E> action) {
            if (cursor.getAndIncrement() > size) {
                return false;
            }
            return wrapped.tryAdvance(action);
        }

        @Override
        public long estimateSize() {
            long s = size(wrapped, size - cursor.get());
            return s > 0 ? s : 0;
        }

        @Override
        public Comparator<? super E> getComparator() {
            if (wrapped.hasCharacteristics(Spliterator.SORTED)) {
                return wrapped.getComparator();
            }
            return super.getComparator();
        }
    }
}
