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
@Warmup(iterations = 2, time = 100, timeUnit = TimeUnit.MILLISECONDS)
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
     * For 16 threads -20% Updates- Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g16for20")
    @GroupThreads(4)
    public void read16With20PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g16for20")
    @GroupThreads(12)
    public void write16With20PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 16 threads - 40% Updates-Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g16for40")
    @GroupThreads(6)
    public void read16With40PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g16for40")
    @GroupThreads(12)
    public void write16With40PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 16 threads -60% Updates- Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g16for60")
    @GroupThreads(10)
    public void read16With60PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g16for60")
    @GroupThreads(6)
    public void write16With60PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 16 threads -80% Updates- Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g16for80")
    @GroupThreads(13)
    public void read16With80PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g16for80")
    @GroupThreads(3)
    public void write16With80PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 16 threads - 100% Updates- Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g16for100")
    @GroupThreads(16)
    public void read16With100PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g16for100")
    @GroupThreads(0)
    public void write16With100PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }
//------------------------------------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------------------------------

    /**
     * For 4 threads -60% Updates- Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g4for60")
    @GroupThreads(2)
    public void read4With60PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g4for60")
    @GroupThreads(2)
    public void write4With60PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 8 threads - 60% Updates-Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g8for60")
    @GroupThreads(5)
    public void read8With60PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g8for60")
    @GroupThreads(3)
    public void write8With60PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 24 threads -60% Updates- Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g24for60")
    @GroupThreads(14)
    public void read24With60PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g24for60")
    @GroupThreads(10)
    public void write24With60PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 32 threads -60% Updates- Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g32for60")
    @GroupThreads(19)
    public void read32With60PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g32for60")
    @GroupThreads(13)
    public void write32With60PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 40 threads -60% Updates- Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g40for60")
    @GroupThreads(24)
    public void read40With60PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g40for60")
    @GroupThreads(16)
    public void write40With60PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(URCUCoarseSetBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
