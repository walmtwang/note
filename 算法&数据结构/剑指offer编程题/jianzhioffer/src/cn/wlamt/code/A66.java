package cn.wlamt.code;

/**
 * 机器人的运动范围：
 * 地上有一个m行和n列的方格。
 * 一个机器人从坐标0,0的格子开始移动，
 * 每一次只能向左，右，上，下四个方向移动一格，
 * 但是不能进入行坐标和列坐标的数位之和大于k的格子。
 * 例如，当k为18时，机器人能够进入方格（35,37），因为3+5+3+7 = 18。
 * 但是，它不能进入方格（35,38），因为3+5+3+8 = 19。
 * 请问该机器人能够达到多少个格子？
 * Created by walmt on 2018/2/4.
 */
public class A66 {

    public static void main(String[] args) {
        new A66().movingCount(15, 20, 20);
    }

    boolean[][] flag;

    public int movingCount(int threshold, int rows, int cols) {
        if (rows <= 0 || cols <= 0 || threshold < 0) {
            return 0;
        }
        flag = new boolean[rows][cols];
        int[] result = {0};
        movingCore(threshold, 0, 0, result);
        return result[0];
    }

    private void movingCore(int threshold, int row, int col, int[] result) {
        if (row < 0 || row >= flag.length || col < 0 || col >= flag[0].length) {
            return;
        }
        if (flag[row][col]) {
            return;
        }
        int row2 = row, col2 = col;
        int threshold2 = 0;
        while (row2 != 0) {
            threshold2 += row2 % 10;
            row2 /= 10;
        }
        while (col2 != 0) {
            threshold2 += col2 % 10;
            col2 /= 10;
        }
        if (threshold2 > threshold) {
            return;
        }
        flag[row][col] = true;
        ++result[0];
        movingCore(threshold, row - 1, col, result);
        movingCore(threshold, row + 1, col, result);
        movingCore(threshold, row, col - 1, result);
        movingCore(threshold, row, col + 1, result);
    }
}
