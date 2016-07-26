/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.hjug.jmhdemo;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;

/**
 * Java 2D arrays perform better with <em>row-major</em> access patterns.  It matters!
 * See the {@link MatrixMath} class in the main code tree for what happens <em>inside</em>
 * the loops that iterate over a large block of data.
 *
 * <pre>
 * # Intel(R) Core(TM) i7-3520M CPU @ 2.90GHz
 * # Linux 4.4.8-300.fc23.x86_64 #1 SMP x86_64 GNU/Linux
 * # JMH 1.12
 * # VM version: JDK 1.8.0_51, VM 25.51-b03
 * # VM options: -Xmx8g -XX:+UseG1GC
 * # Warmup: 3 iterations, 1 s each
 * # Measurement: 5 iterations, 1 s each
 * # Threads: 1 thread, will synchronize iterations
 * # Run complete. Total time: 00:34:00
 *
 * Benchmark                   (size)   Mode  Cnt      Score     Error  Units
 * MatrixMathPerf.baseline        256  thrpt   50  18817.379 ± 268.756  ops/s
 * MatrixMathPerf.baseline       1024  thrpt   50   1076.577 ±  12.828  ops/s
 * MatrixMathPerf.baseline      20480  thrpt   50      3.171 ±   0.045  ops/s
 * MatrixMathPerf.columnMajor     256  thrpt   50  30938.842 ± 294.760  ops/s
 * MatrixMathPerf.columnMajor    1024  thrpt   50    164.639 ±   4.610  ops/s
 * MatrixMathPerf.columnMajor   20480  thrpt   50      0.090 ±   0.002  ops/s
 * MatrixMathPerf.rowMajor        256  thrpt   50  35414.640 ± 446.914  ops/s
 * MatrixMathPerf.rowMajor       1024  thrpt   50   1851.282 ±  20.052  ops/s
 * MatrixMathPerf.rowMajor      20480  thrpt   50      5.295 ±   0.072  ops/s
 * </pre>
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(jvmArgsPrepend = {"-Xmx8g", "-XX:+UseG1GC"})
@Threads(1)
public class MatrixMathPerf {

    @Param({ "256", "1024", "20480" })
    private int size;

    private MatrixMath mm;

    @Setup
    public void setUp() {
        mm = new MatrixMath(size);
    }

    @Benchmark
    public double baseline() {
        return mm.sumUpperTriangle();
    }

    @Benchmark
    public double columnMajor() {
        return mm.sumUpperTriangleCMA();
    }

    @Benchmark
    public double rowMajor() {
        return mm.sumUpperTriangleRMA();
    }
}
