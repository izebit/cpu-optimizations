package ru.izebit;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Launcher {
    public static void main(String[] args) throws RunnerException {

        new Runner(
                new OptionsBuilder()
                        .include(BranchPredictionBenchmark.class.getSimpleName())
                        .include(CpuCacheLevelBenchmark.class.getSimpleName())
                        .include(FalseSharingBenchmark.class.getSimpleName())
                        .build()
        ).run();

    }

}
