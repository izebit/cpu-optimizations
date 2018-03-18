package ru.izebit.cpu_optimizations;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author <a href="izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 22/12/2017/.
 */
public class Launcher {
    public static void main(String[] args) throws RunnerException {

        new Runner(
                new OptionsBuilder()
                        .include(BranchPredictionBenchmark.class.getSimpleName())
                        .include(CpuCacheLevelBenchmark.class.getSimpleName())
                        .include(FalseSharingBenchmark.class.getSimpleName())
                        .include(PrefetchingBenchmark.class.getSimpleName())
                        .include(LoopUnrollingBenchmark.class.getSimpleName())
                        .build()
        ).run();

    }

}
