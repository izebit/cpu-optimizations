package ru.izebit.cpu_optimizations;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.*;

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
public class FalseSharingBenchmark {
    private Tuple optimizedTuple;
    private Tuple unoptimizedTuple;
    private CyclicBarrier barrier;

    @Setup
    public void init() {
        this.optimizedTuple = new Tuple() {
            private volatile int firstValue;
            //shift for against false-sharing
            private long p1, p2, p3, p4, p5, p6, p7, p8;
            private volatile int secondValue;

            @Override
            public int setFirstValue(int value) {
                return this.firstValue = value;
            }

            @Override
            public int setSecondValue(int value) {
                return this.secondValue = value;
            }
        };

        this.unoptimizedTuple = new Tuple() {
            private volatile int firstValue;
            private volatile int secondValue;

            @Override
            public int setFirstValue(int value) {
                return this.firstValue = value;
            }

            @Override
            public int setSecondValue(int value) {
                return this.secondValue = value;
            }
        };

        this.barrier = new CyclicBarrier(2);
    }

    @Benchmark
    public void invokeWithOptimization(final Blackhole blackhole) {
        blackhole.consume(invoke(optimizedTuple));
    }

    @Benchmark
    public void invokeWithoutOptimization(final Blackhole blackhole) {
        blackhole.consume(invoke(unoptimizedTuple));
    }

    private int invoke(Tuple tuple) {
        final int count = 50_000_000;

        final Callable<Integer> firstWriter = () -> {
            barrier.await();
            int result = 0;
            for (int i = 0; i < count; i++)
                result += tuple.setFirstValue(i);
            return result;
        };

        final Callable<Integer> secondWriter = () -> {
            barrier.await();
            int result = 0;
            for (int i = 0; i < count; i++)
                result += tuple.setSecondValue(i);
            return result;
        };

        try {
            ForkJoinTask<Integer> t1 = ForkJoinPool.commonPool().submit(firstWriter);
            ForkJoinTask<Integer> t2 = ForkJoinPool.commonPool().submit(secondWriter);
            return t1.get() + t2.get();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }



    private interface Tuple {
        int setFirstValue(int value);
        int setSecondValue(int value);
    }
}