package parallel_minor.executors;

import java.math.BigInteger;
import java.util.concurrent.*;

public class ParallelExecutor {

    private static int[][] minor(int[][] matrix, int row, int column) {
        int n = matrix.length;
        int[][] minorMatrix = new int[n - 1][n - 1];

        int minorRow = 0;

        for (int i = 0; i < n; i++) {
            if (i == row) { continue; }

            int minorColumn = 0;

            for (int j = 0; j < n; j++) {
                if (j == column) { continue; }

                minorMatrix[minorRow][minorColumn] = matrix[i][j];
                minorColumn++;
            }

            minorRow++;
        }

        return minorMatrix;
    }

    public BigInteger calculateDeterminant(int[][] matrix) {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new DeterminantTask(matrix));
    }

    private static class DeterminantTask extends RecursiveTask<BigInteger> {

        private final int[][] matrix;

        private DeterminantTask(int[][] matrix) {
            this.matrix = matrix;
        }

        @Override
        protected BigInteger compute() {
            int n = matrix.length;

            if (n == 1) {
                return BigInteger.valueOf(matrix[0][0]);
            }

            if (n == 2) {
                return BigInteger.valueOf(matrix[0][0])
                        .multiply(BigInteger.valueOf(matrix[1][1]))
                        .subtract(BigInteger.valueOf(matrix[0][1])
                                .multiply(BigInteger.valueOf(matrix[1][0])));
            }

            BigInteger result = BigInteger.ZERO;

            DeterminantTask[] subtasks = new DeterminantTask[n];
            BigInteger[] resultParts = new BigInteger[n];

            for (int column = 0; column < n; column++) {
                int[][] minorMatrix = minor(matrix, 0, column);
                BigInteger coefficient = (column % 2 == 0) ? BigInteger.valueOf(1) : BigInteger.valueOf(-1);

                subtasks[column] = new DeterminantTask(minorMatrix);
                subtasks[column].fork();

                resultParts[column] = coefficient.multiply(BigInteger.valueOf(matrix[0][column]));
            }

            for (int column = 0; column < n; column++) {
                BigInteger determinant = subtasks[column].join();
                result = result.add(resultParts[column].multiply(determinant));
            }

            return result;
        }
    }
}
