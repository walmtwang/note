package cn.walmt.code;

import java.util.ArrayList;

/**
 * 顺时针打印矩阵：
 * 输入一个矩阵，按照从外向里以顺时针的顺序依次打印出每一个数字，
 * 例如，如果输入如下矩阵：
 *  1  2  3  4
 *  5  6  7  8
 *  9 10 11 12
 * 13 14 15 16
 * 则依次打印出数字：1,2,3,4,8,12,16,15,14,13,9,5,6,7,11,10.
 * [[1, 2, 3, 4, 5],
 * [ 6, 7, 8, 9,10],
 * [11,12,13,14,15],
 * [16,17,18,19,20],
 * [21,22,23,24,25]]
 *
 * [[1, 2, 3, 4, 5],
 * [ 6, 7, 8, 9,10],
 * [11,12,13,14,15]]
 */
public class A19 {

    public static void main(String[] args) {
//        int[][] ints = {{1, 2}, {3, 4}, {5, 6}, {7, 8}, {9 ,10}};
//        int[][] ints = {{1}, {2}, {3}, {4}, {5}};
        int[][] ints = {{1,2,3,4,5},{6,7,8,9,10},{11,12,13,14,15}};
        A19 a19 = new A19();
        System.out.println(a19.printMatrix(ints));
    }

    public ArrayList<Integer> printMatrix(int [][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return new ArrayList<>(0);
        }

        return coreCode(matrix);
    }

    public ArrayList<Integer> coreCode(int[][] matrix) {
        ArrayList<Integer> integers = new ArrayList<>(matrix.length * matrix[0].length);
        if (matrix.length == 1) {
            for (int i = 0; i < matrix[0].length; i++) {
                integers.add(matrix[0][i]);
            }
            return integers;
        }
        if (matrix[0].length == 1) {
            for (int i = 0; i < matrix.length; i++) {
                integers.add(matrix[i][0]);
            }
            return integers;
        }
        for (int i = 0; i < matrix.length / 2 && i < matrix[0].length / 2; i++) {
            for (int j = i; j < matrix[i].length - 1 - i; j++) {
                integers.add(matrix[i][j]);
            }
            for (int j = i; j < matrix.length - 1 - i; j++) {
                integers.add(matrix[j][matrix[j].length - i - 1]);
            }
            for (int j = matrix[i].length - 1 - i; j > i; j--) {
                integers.add(matrix[matrix.length - 1 - i][j]);
            }
            for (int j = matrix.length - 1 - i; j > i ; j--) {
                integers.add(matrix[j][i]);
            }
        }
        if ((matrix.length & 1) == 1 && (matrix[0].length & 1) == 1) {
            if (matrix.length == matrix[0].length) {
                integers.add(matrix[matrix.length / 2][matrix[0].length / 2]);
            } else if (matrix.length < matrix[0].length) {
                for (int i = 0; i <= matrix[0].length - matrix.length; i++) {
                    integers.add(matrix[matrix.length / 2][i + matrix.length / 2]);
                }
            } else {
                for (int i = 0; i <= matrix.length - matrix[0].length; i++) {
                    integers.add(matrix[i + matrix[0].length / 2][matrix[0].length / 2]);
                }
            }
        }
        return integers;
    }
}
