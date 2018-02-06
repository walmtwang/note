package cn.wlamt.code;

/**
 * 矩阵中的路径：
 * 请设计一个函数，用来判断在一个矩阵中是否存在一条包含某字符串所有字符的路径。
 * 路径可以从矩阵中的任意一个格子开始，每一步可以在矩阵中向左，向右，向上，向下移动一个格子。
 * 如果一条路径经过了矩阵中的某一个格子，则该路径不能再进入该格子。
 * 例如
 * a b c e
 * s f c s
 * a d e e
 * 矩阵中包含一条字符串"bcced"的路径，
 * 但是矩阵中不包含"abcb"路径，
 * 因为字符串的第一个字符b占据了矩阵中的第一行第二个格子之后，
 * 路径不能再次进入该格子。
 * Created by walmt on 2018/2/4.
 */
public class A65 {

    public static void main(String[] args) {
        String str = "AAAAAA";
        new A65().hasPath(str.toCharArray(), 3, 2, new String("AAAAAA").toCharArray());
    }

    private boolean[][] flag;

    public boolean hasPath(char[] matrix, int rows, int cols, char[] str) {
        if (matrix == null || matrix.length == 0 || str == null || str.length == 0) {
            return false;
        }
        flag = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (search(matrix, i, j, str, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean search(char[] matrix, int row, int col, char[] str, int index) {
        if (row < 0 || row >= flag.length || col < 0 || col >= flag[0].length) {
            return false;
        }
        if (flag[row][col]) {
            return false;
        }
        if (str[index] == matrix[row * flag[0].length + col] && index == str.length - 1) {
            return true;
        }
        if (str[index] != matrix[row * flag[0].length + col]) {
            return false;
        }
        flag[row][col] = true;
        boolean result = search(matrix, row - 1, col, str, index + 1)
                || search(matrix, row + 1, col, str, index+ 1)
                || search(matrix, row, col - 1, str, index+ 1)
                || search(matrix, row, col + 1, str, index+ 1);
        flag[row][col] = false;
        return result;
    }

}
