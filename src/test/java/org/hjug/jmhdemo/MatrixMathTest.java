/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.hjug.jmhdemo;

import org.junit.Test;
import static org.junit.Assert.*;

/** Let's be sure that the three approaches actually generate the same results.  :-) */
public class MatrixMathTest {

    private static final double TOLERANCE = 0.000001;

    @Test
    public void rmaSameResultAsBaseline() {
        MatrixMath mm = new MatrixMath(2048);
        assertEquals("old vs. new", mm.sumUpperTriangle(), mm.sumUpperTriangleRMA(), TOLERANCE);
    }


    @Test
    public void cmaSameResultAsBaseline() {
        MatrixMath mm = new MatrixMath(2048);
        assertEquals("old vs. new", mm.sumUpperTriangle(), mm.sumUpperTriangleCMA(), TOLERANCE);
    }
}
