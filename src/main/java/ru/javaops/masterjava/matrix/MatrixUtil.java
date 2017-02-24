package ru.javaops.masterjava.matrix;

import ru.javaops.masterjava.service.MailService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    @SuppressWarnings("Duplicates")
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final CompletionService<Boolean> completionService = new ExecutorCompletionService<>(executor);

        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];



        List<Future<Boolean>> futureList = new ArrayList<>();

        for (int j = 0; j < matrixSize; j++) {
            int finalJ = j;
            Future<Boolean> future = completionService.submit(() -> {
                int[] thatColumn = new int[matrixSize];
                for (int k = 0; k < matrixSize; k++) {
                    thatColumn[k] = matrixB[k][finalJ];
                }
                for (int i = 0; i < matrixSize; i++) {
                    int[] thisRow = matrixA[i];
                    int sum = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        sum += thisRow[k] * thatColumn[k];
                    }
                    matrixC[i][finalJ] = sum;
                }
            }, true);
            futureList.add(future);

        }
        while (!futureList.isEmpty()) {
            Future<Boolean> future = completionService.poll();
            if (future != null) {
                futureList.remove(future);
            }
        }
        return matrixC;
    }


    // TODO optimize by https://habrahabr.ru/post/114797/
    @SuppressWarnings("Duplicates")
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        int[] thatColumn = new int[matrixSize];


        for (int j = 0; j < matrixSize; j++) {
            for (int k = 0; k < matrixSize; k++) {
                thatColumn[k] = matrixB[k][j];
            }
            for (int i = 0; i < matrixSize; i++) {
                int[] thisRow = matrixA[i];
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += thisRow[k] * thatColumn[k];
                }
                matrixC[i][j] = sum;
            }
        }
        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
