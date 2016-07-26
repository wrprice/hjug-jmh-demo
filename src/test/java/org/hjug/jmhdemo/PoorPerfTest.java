/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.hjug.jmhdemo;

import org.junit.Ignore;
import org.junit.Test;

/**
 * A collection of naive 'performance' tests that fail to accurately measure the performance
 * of computing {@code log(PI)}.  These are examples of what <em>not</em> to do.
 * <p>
 * These tests are placed in a single file for convenience. However, to eliminate side-effects from
 * different test approaches, please mark <em>all but one</em> test with {@code @Ignore} and run
 * only a single test at a time.
 */
public class PoorPerfTest {

    @Ignore
    @Test
    public void singleShotMillis() {
        long start = System.currentTimeMillis();
        Math.log(Math.PI);
        long end = System.currentTimeMillis();
        System.out.printf("%,d ms per computation%n", end - start);
        // 0 ms per computation
    }

    @Ignore
    @Test
    public void singleShotNanos() {
        long start = System.nanoTime();
        Math.log(Math.PI);
        long end = System.nanoTime();
        System.out.printf("%,d ns per computation%n", end - start);
        // 220,394 ns per computation
    }

    @Ignore
    @Test
    public void loopItMillis() {
        int iterations = 9_000;
        long start = System.currentTimeMillis();
        for (int i = iterations; i > 0; i--) {
            Math.log(Math.PI);
        }
        long end = System.currentTimeMillis();
        System.out.printf(
            "%,d milliseconds for %,d iterations = %4.1f ms per computation%n",
            end - start,
            iterations,
            (end - start * 1.0) / iterations
        );
        // 1 milliseconds for 9,000 iterations = 0.0 ms per computation
    }

    @Ignore
    @Test
    public void loopItNanos() {
        int iterations = 9_000;
        long start = System.nanoTime();
        for (int i = iterations; i > 0; i--) {
            Math.log(Math.PI);
        }
        long end = System.nanoTime();
        System.out.printf(
            "%,d nanoseconds for %,d iterations = %4.1f ns per computation%n",
            end - start,
            iterations,
            (end - start * 1.0) / iterations
        );
        // 442,613 nanoseconds for 9,000 iterations = 49.2 ns per computation
    }

    @Ignore
    @Test
    public void loopAmortized() {
        int iterations = 9_000;
        long totalNanos = doLoopOf15(iterations);
        System.out.printf(
            "%,d nanoseconds for %,d iterations of 15 ops/ea = %4.1f ns per computation%n",
            totalNanos,
            iterations,
            totalNanos * 1.0 / iterations / 15
        );
        // 4,904,175 nanoseconds for 9,000 iterations of 15 ops/ea = 36.3 ns per computation
    }

    private long doLoopOf15(int iterations) {
        long start = System.nanoTime();
        for (int i = iterations; i > 0; i--) {
            // 15 invocations per iteration amortizes the loop cost down to 1/15th (~7%) per op
            Math.log(Math.PI);  Math.log(Math.PI);  Math.log(Math.PI);
            Math.log(Math.PI);  Math.log(Math.PI);  Math.log(Math.PI);
            Math.log(Math.PI);  Math.log(Math.PI);  Math.log(Math.PI);
            Math.log(Math.PI);  Math.log(Math.PI);  Math.log(Math.PI);
            Math.log(Math.PI);  Math.log(Math.PI);  Math.log(Math.PI);
        }
        long end = System.nanoTime();
        return end - start;
    }

    @Ignore
    @Test
    public void loopTheLoop() {
        int runs = 30;
        int iterationsPerRun = 9_000;
        long totalRunNanos = 0;
        for (int r = runs; r > 0; r--) {
            totalRunNanos += doLoopOf15(iterationsPerRun);
        }
        double averageIterNs = totalRunNanos * 1.0 / runs / iterationsPerRun;
        System.out.printf(
            "%,d nanoseconds for %,d runs of %,d iterations of 15 ops/ea = %4.1f ns per computation%n",
            totalRunNanos,
            runs,
            iterationsPerRun,
            averageIterNs / 15
        );
        // 27,108,442 nanoseconds for 30 runs of 9,000 iterations of 15 ops/ea =  6.7 ns per computation
    }

//    @Ignore
    @Test
    public void loopTheLoopALot() {
        int runs = 1_000_000;
        int iterationsPerRun = 1_000;
        long totalRunNs = 0;
        for (int r = runs; r > 0; r--) {
            totalRunNs += doLoopOf15(iterationsPerRun);
        }
        double averageIterNs = totalRunNs * 1.0 / runs / iterationsPerRun;
        System.out.printf(
            "%,d nanoseconds for %,d runs of %,d iterations of 15 ops/ea = %6.3f ns per computation%n",
            totalRunNs,
            runs,
            iterationsPerRun,
            averageIterNs / 15
        );
        // 4,173,984 nanoseconds for 100,000 runs of 3 iterations of 15 ops/ea =  0.9 ns per computation
    }
}
