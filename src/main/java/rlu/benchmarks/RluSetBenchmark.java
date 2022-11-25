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
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import rlu.sets.RluCoarseSet;
import rlu.sets.RluThread;

@State(Scope.Group)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 1, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class RluSetBenchmark<T> {

    @State(Scope.Group)
    public static class GlobalState {
        public RluCoarseSet<Integer> set;
        public int UPPER_BOUND = 100;
        public int item;

        @Setup(Level.Iteration)
        public void doSetup() {
            set = new RluCoarseSet<Integer>(16);
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
     * For 16 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g16For20")
    @GroupThreads(4)
    public void read16With20PercentContains(GlobalState gState, ThreadState<T> tState) {
        gState.set.contains(gState.item, tState.thread);
    }

    @Benchmark
    @Group("g16For20")
    @GroupThreads(6)
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
                .include(RluSetBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
