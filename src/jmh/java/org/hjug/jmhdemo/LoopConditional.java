/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.hjug.jmhdemo;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;

/**
 * Reducing iterations on the inner loop and eliminating a conditional should go faster, right?
 * So why did the production code slow down?  (It's a good idea to avoid loops inside benchmarks.)
 * For the answer, see {@link MatrixMathPerf}.
 *
 * <pre>
 * # Intel(R) Core(TM) i7-3520M CPU @ 2.90GHz
 * # Linux 4.4.8-300.fc23.x86_64 #1 SMP x86_64 GNU/Linux
 * # JMH 1.12
 * # VM version: JDK 1.8.0_51, VM 25.51-b03
 * # Warmup: 3 iterations, 1 s each
 * # Measurement: 5 iterations, 1 s each
 * # Threads: 1 thread, will synchronize iterations
 * # Run complete. Total time: 00:08:33
 *
 * Benchmark                  (limit)   Mode  Cnt      Score      Error  Units
 * LoopConditional.original       256  thrpt   50  19317.790 ±  330.508  ops/s
 * LoopConditional.original      1024  thrpt   50   1293.680 ±   19.716  ops/s
 * LoopConditional.original     20480  thrpt   50      3.240 ±    0.105  ops/s
 * LoopConditional.optimized      256  thrpt   50  93700.808 ± 1404.486  ops/s
 * LoopConditional.optimized     1024  thrpt   50   6069.809 ±   83.714  ops/s
 * LoopConditional.optimized    20480  thrpt   50     15.609 ±    0.207  ops/s
 * </pre>
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.SECONDS)
public class LoopConditional {

    @Param({ "256", "1024", "20480" })
    private int limit;

    @Benchmark
    public int original() {
        int sum = 0;
        for (int i = 0; i < limit; i++) {
            for (int j = 0; j < limit; j++) {
                if (i < j) {
                    sum += i + j; // do some "work"
                }
            }
        }
        return sum;
    }

    @Benchmark
    public int optimized() {
        int sum = 0;
        for (int j = 0; j < limit; j++) {
            for (int i = 0; i < j; i++) {
                sum += i + j; // do some "work"
            }
        }
        return sum;
    }
}
