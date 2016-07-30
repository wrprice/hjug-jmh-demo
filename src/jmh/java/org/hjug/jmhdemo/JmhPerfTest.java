/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.hjug.jmhdemo;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

/**
 * Demonstrates how simple mistakes when benchmarking can result in drastically incorrect
 * results due to JIT compiler optimizations, e.g. dead code elimination.  JMH is here to
 * help, but it cannot protect you from all possible mistakes.
 *
 * <pre>
 * # Intel(R) Core(TM) i7-3520M CPU @ 2.90GHz
 * # Linux 4.5.7-202.fc23.x86_64 #1 SMP x86_64 GNU/Linux
 * # JMH 1.12
 * # VM version: JDK 1.8.0_51, VM 25.51-b03
 * # Warmup: 3 iterations, 1 s each
 * # Measurement: 5 iterations, 1 s each
 * # Threads: 1 threads, will synchronize iterations
 * # Run complete. Total time: 00:09:37
 *
 * Benchmark                          Mode  Cnt   Score   Error  Units
 * JmhPerfTest.nop                    avgt   50   0.299 ± 0.007  ns/op
 * JmhPerfTest.constant               avgt   50   2.746 ± 0.146  ns/op
 * JmhPerfTest.badIgnoredConst        avgt   50   0.295 ± 0.006  ns/op
 * JmhPerfTest.badConstInput          avgt   50   2.683 ± 0.057  ns/op
 * JmhPerfTest.badIgnoredComputation  avgt   50   0.302 ± 0.007  ns/op
 * JmhPerfTest.useReturn              avgt   50  21.711 ± 0.381  ns/op
 * JmhPerfTest.useBlackhole           avgt   50  21.492 ± 0.275  ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JmhPerfTest {

    private double pi = Math.PI;

    /** For comparison purposes, this measures the cost of doing absolutely nothing. */
    @Benchmark
    public void nop() {
        // nothing to do
    }

    /** For comparison purposes, this measures the cost of returning a hard-coded constant. */
    @Benchmark
    public double constant() {
        return 0.0;
    }

    /**
     * Bad example:
     * The result of the computation is not used, stored, or returned.  If the {@code Math.log}
     * operation is inlined by the compiler, it then becomes dead code and can be eliminated
     * completely. The result will be similar to the {@link #nop()} case.
     * <p>
     * To avoid this and get a proper measurement, always use/return the results from your
     * computations.
     */
    @Benchmark
    public void badIgnoredConst() {
        Math.log(Math.PI);
    }

    /**
     * Bad example:
     * Static input to the computation can result in constant-folding, which effectively
     * reduces this method to a simple return of a constant.
     * <p>
     * To avoid this and get a proper measurement, read the input from a state field.
     */
    @Benchmark
    public double badConstInput() {
        return Math.log(Math.PI);
    }

    /**
     * Bad example:
     * Correctly uses a state field for input to the computation, to avoid constant-folding, but
     * failing to use/return the result of the computation makes it potential dead code.
     * <p>
     * To avoid this and get a proper measurement, always use/return the results from your
     * computations.
     */
    @Benchmark
    public void badIgnoredComputation() {
        Math.log(pi);
    }

    /**
     * Good example:
     * Input comes from a state field, and the computation result is returned to JMH.
     * JMH will prevent the JVM from ignoring the result.
     */
    @Benchmark
    public double useReturn() {
        return Math.log(pi);
    }

    /**
     * Good example:
     * Input comes from a state field, and the computation result is consumed by a JMH-provided
     * {@link Blackhole}.  Unlike a return value, a black hole can be used to consume more
     * than one result value in the same benchmark.
     */
    @Benchmark
    public void useBlackhole(Blackhole bh) {
        bh.consume(Math.log(pi));
    }
}
