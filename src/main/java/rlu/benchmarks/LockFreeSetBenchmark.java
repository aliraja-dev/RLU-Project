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
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import rlu.sets.LockFreeSet;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@State(Scope.Group)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 1, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class LockFreeSetBenchmark {

    @State(Scope.Group)
    public static class MyState {
        public LockFreeSet<Integer> set;
        public int UPPER_BOUND = 100;
        public int item;

        @Setup(Level.Iteration)
        public void doSetup() {
            set = new LockFreeSet<>();
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

    /**
     * !For 4 threads - Throughput - 20% Contains
     * 
     * @param state
     */
    @Benchmark
    @Group("g4For20")
    @GroupThreads(1)
    public void read4With20PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g4For20")
    @GroupThreads(2)
    public void write4With20PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g4For20")
    @GroupThreads(1)
    public void remove4With20PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 8 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g8For20")
    @GroupThreads(2)
    public void read8With20PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g8For20")
    @GroupThreads(3)
    public void write8With20PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g8For20")
    @GroupThreads(3)
    public void remove8With20PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 12 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g12For20")
    @GroupThreads(3)
    public void read12With20PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g12For20")
    @GroupThreads(4)
    public void write12With20PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g12For20")
    @GroupThreads(5)
    public void remove12With20PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 16 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g16For20")
    @GroupThreads(4)
    public void read16With20PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g16For20")
    @GroupThreads(6)
    public void write16With20PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g16For20")
    @GroupThreads(6)
    public void remove16With20PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 24 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g24For20")
    @GroupThreads(5)
    public void read24With20PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g24For20")
    @GroupThreads(10)
    public void write24With20PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g24For20")
    @GroupThreads(9)
    public void remove24With20PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 30 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g30For20")
    @GroupThreads(6)
    public void read30With20PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g30For20")
    @GroupThreads(12)
    public void write30With20PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g30For20")
    @GroupThreads(12)
    public void remove30With20PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 36 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g36For20")
    @GroupThreads(7)
    public void read36With20PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g36For20")
    @GroupThreads(14)
    public void write36With20PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g36For20")
    @GroupThreads(15)
    public void remove36With20PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 36 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g40For20")
    @GroupThreads(8)
    public void read40With20PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g40For20")
    @GroupThreads(16)
    public void write40With20PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g40For20")
    @GroupThreads(16)
    public void remove40With20PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * ! For 4 threads - Throughput - 40% Contains
     * 
     * @param state
     */
    @Benchmark
    @Group("g4For40")
    @GroupThreads(2)
    public void read4With40PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g4For40")
    @GroupThreads(1)
    public void write4With40PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g4For40")
    @GroupThreads(1)
    public void remove4With40PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 8 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g8For40")
    @GroupThreads(3)
    public void read8With40PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g8For40")
    @GroupThreads(3)
    public void write8With40PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g8For40")
    @GroupThreads(2)
    public void remove8With40PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 12 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g12For40")
    @GroupThreads(5)
    public void read12With40PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g12For40")
    @GroupThreads(4)
    public void write12With40PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g12For40")
    @GroupThreads(3)
    public void remove12With40PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 16 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g16For40")
    @GroupThreads(6)
    public void read16With40PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g16For40")
    @GroupThreads(5)
    public void write16With40PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g16For40")
    @GroupThreads(5)
    public void remove16With40PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 24 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g24For40")
    @GroupThreads(10)
    public void read24With40PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g24For40")
    @GroupThreads(7)
    public void write24With40PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g24For40")
    @GroupThreads(7)
    public void remove24With40PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 30 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g30For40")
    @GroupThreads(12)
    public void read30With40PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g30For40")
    @GroupThreads(9)
    public void write30With40PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g30For40")
    @GroupThreads(9)
    public void remove30With40PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 36 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g36For40")
    @GroupThreads(14)
    public void read36With40PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g36For40")
    @GroupThreads(11)
    public void write36With40PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g36For40")
    @GroupThreads(11)
    public void remove36With40PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 36 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g40For40")
    @GroupThreads(16)
    public void read40With40PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g40For40")
    @GroupThreads(12)
    public void write40With40PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g40For40")
    @GroupThreads(12)
    public void remove40With40PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * ! For 4 threads - Throughput - 60% Contains
     * 
     * @param state
     */
    @Benchmark
    @Group("g4For60")
    @GroupThreads(2)
    public void read4With60PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g4For60")
    @GroupThreads(1)
    public void write4With60PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g4For60")
    @GroupThreads(1)
    public void remove4With60PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 8 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g8For60")
    @GroupThreads(5)
    public void read8With60PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g8For40")
    @GroupThreads(2)
    public void write8With60PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g8For60")
    @GroupThreads(1)
    public void remove8With60PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 12 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g12For60")
    @GroupThreads(7)
    public void read12With60PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g12For60")
    @GroupThreads(3)
    public void write12With60PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g12For60")
    @GroupThreads(2)
    public void remove12With60PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 16 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g16For60")
    @GroupThreads(10)
    public void read16With60PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g16For60")
    @GroupThreads(3)
    public void write16With60PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g16For60")
    @GroupThreads(3)
    public void remove16With60PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 24 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g24For60")
    @GroupThreads(14)
    public void read24With60PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g24For60")
    @GroupThreads(5)
    public void write24With60PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g24For60")
    @GroupThreads(5)
    public void remove24With60PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 30 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g30For60")
    @GroupThreads(18)
    public void read30With60PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g30For60")
    @GroupThreads(6)
    public void write30With60PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g30For60")
    @GroupThreads(6)
    public void remove30With60PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 36 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g36For60")
    @GroupThreads(22)
    public void read36With60PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g36For60")
    @GroupThreads(7)
    public void write36With60PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g36For60")
    @GroupThreads(7)
    public void remove36With60PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 36 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g40For60")
    @GroupThreads(24)
    public void read40With60PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g40For60")
    @GroupThreads(8)
    public void write40With60PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g40For60")
    @GroupThreads(8)
    public void remove40With60PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * ! For 4 threads - Throughput - 80% Contains
     * 
     * @param state
     */
    @Benchmark
    @Group("g4For80")
    @GroupThreads(2)
    public void read4With80PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g4For80")
    @GroupThreads(1)
    public void write4With80PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g4For80")
    @GroupThreads(1)
    public void remove4With80PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 8 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g8For80")
    @GroupThreads(6)
    public void read8With80PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g8For80")
    @GroupThreads(1)
    public void write8With80PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g8For80")
    @GroupThreads(1)
    public void remove8With80PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 12 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g12For80")
    @GroupThreads(10)
    public void read12With80PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g12For80")
    @GroupThreads(1)
    public void write12With80PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g12For80")
    @GroupThreads(1)
    public void remove12With80PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 16 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g16For80")
    @GroupThreads(13)
    public void read16With80PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g16For80")
    @GroupThreads(2)
    public void write16With80PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g16For80")
    @GroupThreads(1)
    public void remove16With80PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 24 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g24For80")
    @GroupThreads(19)
    public void read24With80PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g24For80")
    @GroupThreads(3)
    public void write24With80PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g24For80")
    @GroupThreads(2)
    public void remove24With80PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 30 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g30For80")
    @GroupThreads(24)
    public void read30With80PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g30For80")
    @GroupThreads(3)
    public void write30With80PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g30For80")
    @GroupThreads(3)
    public void remove30With80PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 36 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g36For80")
    @GroupThreads(29)
    public void read36With80PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g36For80")
    @GroupThreads(4)
    public void write36With80PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g36For80")
    @GroupThreads(3)
    public void remove36With80PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 36 threads - Throughput
     * 
     * @param state
     */
    @Benchmark
    @Group("g40For80")
    @GroupThreads(32)
    public void read40With80PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g40For80")
    @GroupThreads(4)
    public void write40With80PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g40For80")
    @GroupThreads(4)
    public void remove40With80PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * !For 20 threads - Throughput vs Contains 20% & 40 & 60 & 80
     * 
     * @param state
     */
    @Benchmark
    @Group("g20For20")
    @GroupThreads(4)
    public void read20With20PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g20For20")
    @GroupThreads(8)
    public void write20With20PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g20For20")
    @GroupThreads(8)
    public void remove20With20PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 20 threads - Throughput vs Contains 40%
     * 
     * @param state
     */
    @Benchmark
    @Group("g20For40")
    @GroupThreads(8)
    public void read20With40PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g20For40")
    @GroupThreads(6)
    public void write20With40PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g20For40")
    @GroupThreads(6)
    public void remove20With40PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 20 threads - Throughput vs Contains 60%
     * 
     * @param state
     */
    @Benchmark
    @Group("g20For60")
    @GroupThreads(12)
    public void read20With60PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g20For60")
    @GroupThreads(3)
    public void write20With60PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g20For60")
    @GroupThreads(3)
    public void remove20With60PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    /**
     * For 20 threads - Throughput vs Contains 80%
     * 
     * @param state
     */
    @Benchmark
    @Group("g20For80")
    @GroupThreads(16)
    public void read20With80PercentContains(MyState state) {
        state.set.contains(state.item);
    }

    @Benchmark
    @Group("g20For80")
    @GroupThreads(2)
    public void write20With80PercentContains(MyState state) {
        state.set.add(state.item);
    }

    @Benchmark
    @Group("g20For80")
    @GroupThreads(2)
    public void remove20With80PercentContains(MyState state) {
        state.set.remove(state.item);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(LockFreeSetBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }

}
