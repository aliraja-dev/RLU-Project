package rlu.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import rlu.sets.RluSet.RluPaddedCoarseSet;
import rlu.sets.RluSet.RluThread;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@State(Scope.Group)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 1, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class RluCGPaddedSetBenchmark<T> {

    @State(Scope.Group)
    public static class GlobalState {
        public RluPaddedCoarseSet<Integer> set;
        public int UPPER_BOUND = 100;
        public int item;

        @Setup(Level.Iteration)
        public void doSetup() {
            set = new RluPaddedCoarseSet<Integer>(16);
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
        public RluThread<Integer> thread;

        @Setup(Level.Iteration)
        public void doSetup() {
            thread = new RluThread<>();
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
     * For 4 threads -25% Updates- Throughput
     * 
     * @param State
     */
    @Benchmark
    @Group("g4for25")
    @GroupThreads(4)
    public void read4With25PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g4for25")
    @GroupThreads(6)
    public void write4With25PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 8 threads - 25% Updates-Throughput
     * 
     * @param State
     */
    @Benchmark
    @Group("g8for25")
    @GroupThreads(4)
    public void read8With25PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g8for25")
    @GroupThreads(6)
    public void write8With25PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 12 threads -25% Updates- Throughput
     * 
     * @param State
     */
    @Benchmark
    @Group("g12for25")
    @GroupThreads(4)
    public void read12With25PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g12for25")
    @GroupThreads(6)
    public void write12With25PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 16 threads -25% Updates- Throughput
     * 
     * @param State
     */
    @Benchmark
    @Group("g16for25")
    @GroupThreads(4)
    public void read16With25PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g16for25")
    @GroupThreads(6)
    public void write16With25PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 4 threads - 50% Updates- Throughput
     * 
     * @param State
     */
    @Benchmark
    @Group("g4for50")
    @GroupThreads(4)
    public void read4With50PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g4for50")
    @GroupThreads(6)
    public void write4With50PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 8 threads -50% Updates- Throughput
     * 
     * @param State
     */
    @Benchmark
    @Group("g8for50")
    @GroupThreads(4)
    public void read8With50PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g8for50")
    @GroupThreads(6)
    public void write8With50PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 12 threads -50% Updates- Throughput
     * 
     * @param State
     */
    @Benchmark
    @Group("g12for50")
    @GroupThreads(4)
    public void read12With50PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g12for50")
    @GroupThreads(6)
    public void write12With50PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 16 threads -50% Updates- Throughput
     * 
     * @param State
     */
    @Benchmark
    @Group("g16for50")
    @GroupThreads(4)
    public void read16With50PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g16for50")
    @GroupThreads(6)
    public void write16With50PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(RluCGPaddedSetBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
