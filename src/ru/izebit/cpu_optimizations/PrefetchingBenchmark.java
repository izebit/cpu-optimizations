package ru.izebit.cpu_optimizations;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
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
public class PrefetchingBenchmark {
    private List<Integer> linkedList;
    private List<Integer> arrayList;

    @Setup
    public void init() {
        final int size = 1_000_000;
        this.arrayList = new ArrayList<>(size);
        this.linkedList = new LinkedList<>();
        final Random random = new Random(42L);
        for (int i = 0; i < size; i++) {
            this.arrayList.add(random.nextInt());
            this.linkedList.add(random.nextInt());
        }
    }

    @Benchmark
    public void invokeWithOptimization(final Blackhole blackhole) {
        for (Integer value : arrayList)
            blackhole.consume(value);
    }

    @Benchmark
    public void invokeWithoutOptimization(final Blackhole blackhole) {
        for (Integer value : linkedList)
            blackhole.consume(value);
    }
}
