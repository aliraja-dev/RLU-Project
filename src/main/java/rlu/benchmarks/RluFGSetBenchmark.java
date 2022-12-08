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

import rlu.sets.RluSets.SingleObjRluFineSet;
import rlu.sets.RluSets.RluSetInterface;
import rlu.sets.RluSets.RluThread;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@State(Scope.Group)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class RluFGSetBenchmark<T> {

    @State(Scope.Group)
    public static class GlobalState {
        public RluSetInterface<Integer> set;
        public int UPPER_BOUND = 100;
        public int item;

        @Setup(Level.Iteration)
        public void doSetup() {
            set = new SingleObjRluFineSet<Integer>();
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
     * For 8 threads - 20% Updates-Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g8for20")
    @GroupThreads(2)
    public void read8With20PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g8for20")
    @GroupThreads(6)
    public void write8With20PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 8 threads - 40% Updates-Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g8for40")
    @GroupThreads(3)
    public void read8With40PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g8for40")
    @GroupThreads(5)
    public void write8With40PercentContains(GlobalState gState, ThreadState<T> tState) {
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
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g8for60")
    @GroupThreads(3)
    public void write8With60PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 8 threads - 80% Updates-Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g8for80")
    @GroupThreads(6)
    public void read8With80PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g8for80")
    @GroupThreads(2)
    public void write8With80PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
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
     * For 24 threads -20% Updates- Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g24for20")
    @GroupThreads(5)
    public void read24With20PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g24for20")
    @GroupThreads(19)
    public void write24With20PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 24 threads -40% Updates- Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g24for40")
    @GroupThreads(10)
    public void read24With40PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g24for40")
    @GroupThreads(14)
    public void write24With40PercentContains(GlobalState gState, ThreadState<T> tState) {
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
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g24for60")
    @GroupThreads(10)
    public void write24With60PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 24 threads -80% Updates- Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g24for80")
    @GroupThreads(19)
    public void read24With80PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g24for80")
    @GroupThreads(5)
    public void write24With80PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 32 threads -20% Updates- Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g32for20")
    @GroupThreads(6)
    public void read32With20PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g32for20")
    @GroupThreads(26)
    public void write32With20PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 32 threads -40% Updates- Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g32for40")
    @GroupThreads(13)
    public void read32With40PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g32for40")
    @GroupThreads(19)
    public void write32With40PercentContains(GlobalState gState, ThreadState<T> tState) {
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
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g32for60")
    @GroupThreads(13)
    public void write32With60PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    /**
     * For 32 threads -80% Updates- Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g32for80")
    @GroupThreads(26)
    public void read32With80PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g32for80")
    @GroupThreads(7)
    public void write32With80PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.add(gState.item, tState.thread);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(RluFGSetBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
