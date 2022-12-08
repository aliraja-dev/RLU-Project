# RLU Modified User Space Managed Memory Implementation

There are two ways to run the rlu and non rlu based implementations. One is to run the 13 different benchmarks that use JMH to report results and are precompiled into benchmarks.jar and can be run altogether or one by one by the benchmark class name, provided below.

### How to run the Benchmarks

To run all benchmarks together, $ java -jar target/benchmarks.jar
However, would not suggest and this will run all the 13 benchmarks one after another and output will be difficult to comprehend.

$ java -jar target/benchmarks.jar CoarseSetBenchmark
$ java -jar target/benchmarks.jar FineSetBenchmark
$ java -jar target/benchmarks.jar LockFreeSetBenchmark
$ java -jar target/benchmarks.jar RluCGSetBenchmark
$ java -jar target/benchmarks.jar RluFGSetBenchmark
$ java -jar target/benchmarks.jar RluCGPaddedSetBenchmark
$ java -jar target/benchmarks.jar RluMultiObjectSetBenchmark
$ java -jar target/benchmarks.jar FineBSTBenchmark
$ java -jar target/benchmarks.jar CoarseBSTBenchmark
$ java -jar target/benchmarks.jar RluCoarseBSTBenchmark
$ java -jar target/benchmarks.jar RluFineBSTBenchmark
$ java -jar target/benchmarks.jar URCUCGSetBenchmark
$ java -jar target/benchmarks.jar URCUFGSetBenchmark

### Expected Output

JMH will return extensive results for every iteration, the warmup iterations set to two wont be considered in measurements of throughput. Throughput is measured over 100ms and averaged over 5 counts.

At the end, of a benchmark result will be a table, showing the throughput numbers for every method calls and grouped by the thread and % age Contains ratio for that group such as
g8for20 means group of 8 threads where 20% of them are calling contains and rest calling write. The actual thread distribution is done hard coded with thread count.

### How to run the Runners

We have also setup runner classes to manually run the implementations and test for correctness and stolen nodes print outs. And they can be run from the class main method itself.
You can play around with the iterations and the threads you want to distribute between readers and writers all the fun stuff is possible there.

### Maven - build

To rebuild any modifications to the benchmarks, you would have to run
$ mvn clean install

and then run the benchmarks again to see the changes.

## Project Structure - RLU Directory

### Benchmarks

Holds 13 different benchmarks that use JMH for microbenchmarking

### Runners

To run implementations manually

### Sets

RLU and Non RLU based sets, URCU and BST

### Threads

Hold the different type of threads required for the different data structures.
