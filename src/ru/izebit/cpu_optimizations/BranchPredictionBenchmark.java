package ru.izebit.cpu_optimizations;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
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
public class BranchPredictionBenchmark {
    private ArrayTuple sortedArray;
    private ArrayTuple unsortedArray;

    @Setup
    public void init() {
        sortedArray = new ArrayTuple(true);
        unsortedArray = new ArrayTuple(false);
    }

    @Benchmark
    public void invokeWithOptimization(final Blackhole blackhole) {
        int result = countOfElementsLessThen(sortedArray.average, sortedArray.array);
        blackhole.consume(result);
    }

    @Benchmark
    public void invokeWithoutOptimization(final Blackhole blackhole) {
        int result = countOfElementsLessThen(unsortedArray.average, unsortedArray.array);
        blackhole.consume(result);
    }

    private static int countOfElementsLessThen(final int value,
                                               final int[] array) {
        int result = 0;
        for (int anArray : array)
            if (anArray < value)
                result++;

        return result;
    }


    private static class ArrayTuple {
        private static final int ARRAY_LENGTH = 1_000_000;
        private final int[] array;
        private final int average;

        private ArrayTuple(final boolean isSorted) {
            final int[] array = getArray(ARRAY_LENGTH);
            if (isSorted)
                Arrays.sort(array);
            this.array = array;
            this.average = getAverage(array);
        }

        private static int getAverage(final int[] array) {
            int sum = 0;
            for (int anArray : array)
                sum += anArray;

            return sum / array.length;
        }

        private static int[] getArray(final int size) {
            final int[] array = new int[size];
            final Random random = new Random(42);
            for (int i = 0; i < array.length; i++)
                array[i] = random.nextInt();

            return array;
        }
    }
}
