//package rlu.benchmarks;
//
//import org.openjdk.jmh.annotations.*;
//import org.openjdk.jmh.runner.Runner;
//import org.openjdk.jmh.runner.RunnerException;
//import org.openjdk.jmh.runner.options.Options;
//import org.openjdk.jmh.runner.options.OptionsBuilder;
//import rlu.sets.ComparisonSets.FineBST;
//
//import java.util.Random;
//import java.util.concurrent.ThreadLocalRandom;
//import java.util.concurrent.TimeUnit;
//
//@State(Scope.Group)
//@BenchmarkMode(Mode.Throughput)
//@OutputTimeUnit(TimeUnit.MILLISECONDS)
//@Warmup(iterations = 2, time = 100, timeUnit = TimeUnit.MILLISECONDS)
//@Measurement(iterations = 1, time = 100, timeUnit = TimeUnit.MILLISECONDS)
//@Fork(1)
//public class FineBSTBenchmark {
//
//    @State(Scope.Group)
//    public static class MyState {
//        public FineBST<Integer> set;
//        public int UPPER_BOUND = 100;
//        public int item;
//
//        @Setup(Level.Iteration)
//        public void doSetup() {
//            set = new FineBST<>();
//        }
//
//        @TearDown(Level.Iteration)
//        public void doTearDown() {
//            set = null;
//        }
//
//        @Setup(Level.Invocation)
//        public void generateItem() {
//            Random rand = ThreadLocalRandom.current();
//            item = rand.nextInt(UPPER_BOUND);
//        }
//    }
//
//    /**
//     * For 4 threads -25% Updates- Throughput
//     *
//     * @param state
//     */
//    @Benchmark
//    @Group("g4for25")
//    @GroupThreads(4)
//    public void read4With25PercentContains(FineBSTBenchmark.MyState state) {
//        state.set.contains(state.item);
//    }
//
//    @Benchmark
//    @Group("g4for25")
//    @GroupThreads(6)
//    public void write4With25PercentContains(FineBSTBenchmark.MyState state) {
//        state.set.add(state.item);
//    }
//
//    /**
//     * For 8 threads - 25% Updates-Throughput
//     *
//     * @param state
//     */
//    @Benchmark
//    @Group("g8for25")
//    @GroupThreads(4)
//    public void read8With25PercentContains(FineBSTBenchmark.MyState state) {
//        state.set.contains(state.item);
//    }
//
//    @Benchmark
//    @Group("g8for25")
//    @GroupThreads(6)
//    public void write8With25PercentContains(FineBSTBenchmark.MyState state) {
//        state.set.add(state.item);
//    }
//
//    /**
//     * For 12 threads -25% Updates- Throughput
//     *
//     * @param state
//     */
//    @Benchmark
//    @Group("g12for25")
//    @GroupThreads(4)
//    public void read12With25PercentContains(FineBSTBenchmark.MyState state) {
//        state.set.contains(state.item);
//    }
//
//    @Benchmark
//    @Group("g12for25")
//    @GroupThreads(6)
//    public void write12With25PercentContains(FineBSTBenchmark.MyState state) {
//        state.set.add(state.item);
//    }
//
//    /**
//     * For 16 threads -25% Updates- Throughput
//     *
//     * @param state
//     */
//    @Benchmark
//    @Group("g16for25")
//    @GroupThreads(4)
//    public void read16With25PercentContains(FineBSTBenchmark.MyState state) {
//        state.set.contains(state.item);
//    }
//
//    @Benchmark
//    @Group("g16for25")
//    @GroupThreads(6)
//    public void write16With25PercentContains(FineBSTBenchmark.MyState state) {
//        state.set.add(state.item);
//    }
//
//    /**
//     * For 4 threads - 50% Updates- Throughput
//     *
//     * @param state
//     */
//    @Benchmark
//    @Group("g4for50")
//    @GroupThreads(4)
//    public void read4With50PercentContains(FineBSTBenchmark.MyState state) {
//        state.set.contains(state.item);
//    }
//
//    @Benchmark
//    @Group("g4for50")
//    @GroupThreads(6)
//    public void write4With50PercentContains(FineBSTBenchmark.MyState state) {
//        state.set.add(state.item);
//    }
//
//    /**
//     * For 8 threads -50% Updates- Throughput
//     *
//     * @param state
//     */
//    @Benchmark
//    @Group("g8for50")
//    @GroupThreads(4)
//    public void read8With50PercentContains(FineBSTBenchmark.MyState state) {
//        state.set.contains(state.item);
//    }
//
//    @Benchmark
//    @Group("g8for50")
//    @GroupThreads(6)
//    public void write8With50PercentContains(FineBSTBenchmark.MyState state) {
//        state.set.add(state.item);
//    }
//
//    /**
//     * For 12 threads -50% Updates- Throughput
//     *
//     * @param state
//     */
//    @Benchmark
//    @Group("g12for50")
//    @GroupThreads(4)
//    public void read12With50PercentContains(FineBSTBenchmark.MyState state) {
//        state.set.contains(state.item);
//    }
//
//    @Benchmark
//    @Group("g12for50")
//    @GroupThreads(6)
//    public void write12With50PercentContains(FineBSTBenchmark.MyState state) {
//        state.set.add(state.item);
//    }
//
//    /**
//     * For 16 threads -50% Updates- Throughput
//     *
//     * @param state
//     */
//    @Benchmark
//    @Group("g16for50")
//    @GroupThreads(4)
//    public void read16With50PercentContains(FineBSTBenchmark.MyState state) {
//        state.set.contains(state.item);
//    }
//
//    @Benchmark
//    @Group("g16for50")
//    @GroupThreads(6)
//    public void write16With50PercentContains(FineBSTBenchmark.MyState state) {
//        state.set.add(state.item);
//    }
//
//    public static void main(String[] args) throws RunnerException {
//        Options opt = new OptionsBuilder()
//                .include(FineBSTBenchmark.class.getSimpleName())
//                .build();
//
//        new Runner(opt).run();
//    }
//
//}
