package ru.izebit.cpu_optimizations;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 22/12/2017/.
 */
@Fork(1)
@Threads(1)
@Warmup(iterations = 5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Measurement(iterations = 10)
public class LoopUnrollingBenchmark {
    private long[] array;

    @Setup
    public void init() {
        this.array = new long[1_000_000];
        Arrays.fill(array, System.nanoTime());
    }

    @Benchmark
    public void invokeWithOptimization(final Blackhole blackhole) {
        int result = 0;
        for (int i = 0; i < array.length; i += 2)
            result += (array[i] + array[2]);

        blackhole.consume(result);
    }

    @Benchmark
    public void invokeWithoutOptimization(final Blackhole blackhole) {
        int result = 0;
        for (int i = 0; i < array.length; i++)
            result += array[i];

        blackhole.consume(result);
    }

}
