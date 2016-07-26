/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.hjug.jmhdemo;

import java.util.Random;

/**
 * Example 'production' code that contains three slightly different approaches to summing the
 * upper-triangle of a square matrix.  (Usually this type of operation would be applied to a
 * symmetric matrix, but in this case the matrix is simply filled with random numbers.)
 * <p>
 * This example is based on a real-world scenario previously demonstrated by Jim B.
 */
class MatrixMath {

    private final double[][] matrix;

    /**
     * Initializes a square matrix, where each dimension is equal to the specified size.
     * We're not interested in the performance of this initialization routine.
     *
     * @param size height (and also width) of the generated 2-D matrix
     */
    MatrixMath(int size) {
        matrix = new double[size][];
        Random rand = new Random();
        for (int r = size - 1; r >= 0; r--) {
            matrix[r] = new double[size];
            for (int c = size - 1; c >= 0; c--) {
                matrix[r][c] = rand.nextDouble();
            }
        }
    }

    /**
     * This version contains a simple nested loop and an innermost conditional.
     *
     * @return sum of the element values appearing in the matrix's upper triangle
     */
    double sumUpperTriangle() {
        int size = matrix.length;
        double sum = 0.0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i <= j) {
                    sum += matrix[i][j];
                }
            }
        }
        return sum;
    }

    /**
     * This version eliminates the nested {@code if} statement, instead relying on
     * a different loop condition.
     *
     * @return sum of the element values appearing in the matrix's upper triangle
     */
    double sumUpperTriangleCMA() {
        int size = matrix.length;
        double sum = 0.0;

        for (int j = 0; j < size; j++) {
            for (int i = 0; i <= j; i++) {
                sum += matrix[i][j];
            }
        }
        return sum;
    }

    /**
     * This version eliminates the nested {@code if} statement, but keeps the original
     * loop conditions; however, the initialization of the inner loop variable has changed.
     *
     * @return sum of the element values appearing in the matrix's upper triangle
     */
    double sumUpperTriangleRMA() {
        int size = matrix.length;
        double sum = 0.0;

        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                sum += matrix[i][j];
            }
        }
        return sum;
    }
}
