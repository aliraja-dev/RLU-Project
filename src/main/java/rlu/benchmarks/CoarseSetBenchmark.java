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

import rlu.sets.ComparisonSets.CoarseSet;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@State(Scope.Group)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 1, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class CoarseSetBenchmark {

    @State(Scope.Group)
    public static class MyState {
        public CoarseSet<Integer> set;
        public int UPPER_BOUND = 100;
        public int item;

        @Setup(Level.Iteration)
        public void doSetup() {
            set = new CoarseSet<>();
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

        @Setup(Level.Iteration)
        public void doThreadSetup() {
            // Add all the threads to the global threads array
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
    public void read16With20PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g16for20")
    @GroupThreads(12)
    public void write16With20PercentContains(MyState state) {
        state.set.add(state.item);
    }

    /**
     * For 16 threads - 40% Updates-Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g16for40")
    @GroupThreads(6)
    public void read16With40PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g16for40")
    @GroupThreads(12)
    public void write16With40PercentContains(MyState state) {
        state.set.add(state.item);
    }

    /**
     * For 16 threads -60% Updates- Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g16for60")
    @GroupThreads(10)
    public void read16With60PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g16for60")
    @GroupThreads(6)
    public void write16With60PercentContains(MyState state) {
        state.set.add(state.item);
    }

    /**
     * For 16 threads -80% Updates- Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g16for80")
    @GroupThreads(13)
    public void read16With80PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g16for80")
    @GroupThreads(3)
    public void write16With80PercentContains(MyState state) {
        state.set.add(state.item);
    }

    /**
     * For 16 threads - 100% Updates- Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g16for100")
    @GroupThreads(16)
    public void read16With100PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g16for100")
    @GroupThreads(0)
    public void write16With100PercentContains(MyState state) {
        state.set.add(state.item);
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
    public void read4With60PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g4for60")
    @GroupThreads(2)
    public void write4With60PercentContains(MyState state) {
        state.set.add(state.item);
    }

    /**
     * For 8 threads - 60% Updates-Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g8for60")
    @GroupThreads(5)
    public void read8With60PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g8for60")
    @GroupThreads(3)
    public void write8With60PercentContains(MyState state) {
        state.set.add(state.item);
    }

    /**
     * For 24 threads -60% Updates- Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g24for60")
    @GroupThreads(14)
    public void read24With60PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g24for60")
    @GroupThreads(10)
    public void write24With60PercentContains(MyState state) {
        state.set.add(state.item);
    }

    /**
     * For 32 threads -60% Updates- Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g32for60")
    @GroupThreads(19)
    public void read32With60PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g32for60")
    @GroupThreads(13)
    public void write32With60PercentContains(MyState state) {
        state.set.add(state.item);
    }

    /**
     * For 40 threads -60% Updates- Throughput
     *
     * @param state
     */
    @Benchmark
    @Group("g40for60")
    @GroupThreads(24)
    public void read40With60PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g40for60")
    @GroupThreads(16)
    public void write40With60PercentContains(MyState state) {
        state.set.add(state.item);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CoarseSetBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }

}
