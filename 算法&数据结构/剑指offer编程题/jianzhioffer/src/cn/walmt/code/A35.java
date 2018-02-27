package cn.walmt.code;

import java.util.Arrays;

/**
 * 数组中的逆序对：
 * 在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。
 * 输入一个数组,求出这个数组中的逆序对的总数P。
 * 并将P对1000000007取模的结果输出。 即输出P%1000000007
 */
public class A35 {

    public static void main(String[] args) {
//        int[] ints = {364, 637, 341, 406, 747, 995, 234, 971, 571, 219, 993, 407, 416, 366, 315, 301, 601, 650, 418, 355, 460, 505, 360, 965, 516, 648, 727, 667, 465, 849, 455, 181, 486, 149, 588, 233, 144, 174, 557, 67, 746, 550, 474, 162, 268, 142, 463, 221, 882, 576, 604, 739, 288, 569, 256, 936, 275, 401, 497, 82, 935, 983, 583, 523, 697, 478, 147, 795, 380, 973, 958, 115, 773, 870, 259, 655, 446, 863, 735, 784, 3, 671, 433, 630, 425, 930, 64, 266, 235, 187, 284, 665, 874, 80, 45, 848, 38, 811, 267, 575};
        int[] ints = {1, 2, 3, 4, 5, 6, 7, 0, 1};
        System.out.println(new A35().InversePairs(ints));
    }

    public int InversePairs(int[] array) {
        if (array == null || array.length == 0) {
            return 0;
        }
        int[] temp = Arrays.copyOf(array, array.length);
        return inversePairsCore(array, temp, 0, array.length - 1);
    }

    private int inversePairsCore(int[] array, int[] temp, int left, int right) {
        if (left >= right) {
            return 0;
        }
        int mid = (left + right) / 2;
        int result = inversePairsCore(temp, array, left, mid);
        result += inversePairsCore(temp, array, mid + 1, right);
        result %= 1000000007;
        result += MergeArrays(array, temp, left, right, mid);
        result %= 1000000007;
        return result;
    }

    private int MergeArrays(int[] array, int[] temp, int left, int right, int mid) {
        int leftIndex = mid, rightIndex = right, end = right;
        int result = 0;
        while (leftIndex >= left && rightIndex > mid) {
            if (array[leftIndex] > array[rightIndex]) {
                result += rightIndex - mid;
                result %= 1000000007;
                temp[end--] = array[leftIndex--];
            } else if (array[leftIndex] <= array[rightIndex]) {
                temp[end--] = array[rightIndex--];
            }
        }
        while (leftIndex >= left) {
            temp[end--] = array[leftIndex--];
        }
        while (rightIndex > mid) {
            temp[end--] = array[rightIndex--];
        }
        return result % 1000000007;
    }

}
