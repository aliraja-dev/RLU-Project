package rlu.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import rlu.sets.URCU.CoarseSetURCU;
import rlu.sets.URCU.RcuThread;


import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@State(Scope.Group)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 1, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class URCUCoarseSetBenchmark<T> {

    @State(Scope.Group)
    public static class GlobalState {
        public CoarseSetURCU<Integer> set;
        public int UPPER_BOUND = 100;
        public int item;

        @Setup(Level.Iteration)
        public void doSetup() {
            set = new CoarseSetURCU<>(16);
        }

        @TearDown(Level.Iteration)
        public void doTearDown() {
            set = null;
        }

        @Setup(Level.Invocation)
        public void generateItem() {
            Random rand = ThreadLocalRandom.current();
            item = rand.nextInt(UPPER_BOUND);
        }
    }

    @State(Scope.Thread)
    public static class ThreadState<T> {
        public RcuThread<Integer> thread;

        @Setup(Level.Iteration)
        public void doSetup() {
            thread = new RcuThread<>();
        }

        @TearDown(Level.Iteration)
        public void doTearDown() {
            thread = null;
        }

        @Setup(Level.Invocation)
        public void forEveryInvocation() {
            // nothing yet
        }
    }

    /**
     * For 16 threads - Throughput
     *
     * @param //state
     */
    @Benchmark
    @Group("g20for10")
    @GroupThreads(4)
    public void read16With20PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g20for10")
    @GroupThreads(12)
    public void write16With20PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    // @Benchmark
    // @Group("g16For20")
    // @GroupThreads(6)
    // public void remove16With20PercentContains(GlobalState gState, ThreadState<T>
    // tState) {
    // gState.set.remove(gState.item, tState.thread);
    // }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(URCUCoarseSetBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
