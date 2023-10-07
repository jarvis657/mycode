package origin.jdk8;

/**
 * @Author:jarvis
 * @Date: 2023/9/28
 * @Desc:
 **/
public class ForkJoinR extends java.util.concurrent.RecursiveTask<Long> {

    private final long[] numbers;
    private final int start;
    private final int end;
    public static final long THRESHOLD = 10_000;

    public ForkJoinR(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    private ForkJoinR(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - start;
        if (length <= THRESHOLD) {
            return computeSequentially();
        }
        ForkJoinR leftTask = new ForkJoinR(numbers, start, start + length / 2);
        leftTask.fork();

        ForkJoinR rightTask = new ForkJoinR(numbers, start + length / 2, end);
        Long rightResult = rightTask.compute();
        Long leftResult = leftTask.join();
        return leftResult + rightResult;
    }

    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += numbers[i];
        }
        return sum;
    }
}
