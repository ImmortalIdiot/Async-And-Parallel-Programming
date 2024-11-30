package parallel_minor;

import parallel_minor.executors.ParallelExecutor;
import parallel_minor.executors.SingleThreadExecutor;
import parallel_minor.util.FindingDeterminantResultSaver;
import parallel_minor.util.MatrixGenerator;
import parallel_minor.util.MatrixReader;

import java.math.BigInteger;

public class EntryPoint {

    private static final int matrixOrder = 11;
    private static final MatrixGenerator matrixGenerator = new MatrixGenerator(matrixOrder);

    public static void main(String[] args) {
        matrixGenerator.createAndSaveMatrix();
        singleThreadExecution();
        recursiveParallelismExecution();
    }

    public static void singleThreadExecution() {
        SingleThreadExecutor singleThreadExecutor = new SingleThreadExecutor();
        int[][] matrix = getMatrix();

        long start = System.currentTimeMillis();
        BigInteger result = singleThreadExecutor.calculateDeterminant(matrix);
        long end = System.currentTimeMillis();

        FindingDeterminantResultSaver.save(
                "SingleThread.txt",
                end - start,
                result
        );
    }

    public static void recursiveParallelismExecution() {
        ParallelExecutor parallelExecutor = new ParallelExecutor();
        int[][] matrix = getMatrix();

        long start = System.currentTimeMillis();
        BigInteger result = parallelExecutor.calculateDeterminant(matrix);
        long end = System.currentTimeMillis();

        FindingDeterminantResultSaver.save("RecursiveParallelism.txt",
                end - start,
                result);
    }

    private static int[][] getMatrix() {
        String filePathWithMatrix = matrixGenerator.getFilePath();
        return MatrixReader.readMatrixFromFile(filePathWithMatrix);
    }
}
