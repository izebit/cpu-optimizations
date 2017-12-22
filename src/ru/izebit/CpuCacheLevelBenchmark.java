package ru.izebit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 22/12/2017/.
 */
@Fork(1)
@Threads(1)
@Warmup(iterations = 5)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Measurement(iterations = 10)
public class CpuCacheLevelBenchmark {

    @Setup
    public void init(final Blackhole blackhole) {
        for (Caches cache : Caches.values())
            blackhole.consume(cache.array.length);
    }

    @Benchmark
    public void firstCacheLevel(final Blackhole blackhole) {
        int result = sumOfElementsWithRandomIndexes(Caches.FIRST_LEVEL.array);
        blackhole.consume(result);
    }

    @Benchmark
    public void secondCacheLevel(final Blackhole blackhole) {
        int result = sumOfElementsWithRandomIndexes(Caches.SECOND_LEVEL.array);
        blackhole.consume(result);
    }

    @Benchmark
    public void thirdCacheLevel(final Blackhole blackhole) {
        int result = sumOfElementsWithRandomIndexes(Caches.THIRD_LEVEL.array);
        blackhole.consume(result);
    }

    @Benchmark
    public void ramCacheLevel(final Blackhole blackhole) {
        int result = sumOfElementsWithRandomIndexes(Caches.RAM_LEVEL.array);
        blackhole.consume(result);
    }

    private static int sumOfElementsWithRandomIndexes(final int[] array) {

        final Random random = new Random(42L);
        final int count = 1_000_000;
        int result = 0;
        for (int i = 1; i < count; i++) {
            result += array[random.nextInt(array.length)];
        }
        return result;
    }

    @State(Scope.Benchmark)
    private enum Caches {
        /*
        cache sizes of my cpu:
            first  32  000         bytes
            second 256 000         bytes
            third  8   000 000     bytes
        */
        FIRST_LEVEL(1_000),
        SECOND_LEVEL(100_000),
        THIRD_LEVEL(1_000_000),
        RAM_LEVEL(10_000_000);

        private final int[] array;

        Caches(int size) {
            final Random random = new Random(42L);
            this.array = new int[size];
            for (int i = 0; i < array.length; i++)
                array[i] = random.nextInt();
        }
    }
}
