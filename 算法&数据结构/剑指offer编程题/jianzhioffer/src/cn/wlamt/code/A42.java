package cn.wlamt.code;

import java.util.ArrayList;

/**
 * 和为S的两个数字：
 * 输入一个递增排序的数组和一个数字S，
 * 在数组中查找两个数，是的他们的和正好是S，
 * 如果有多对数字的和等于S，输出两个数的乘积最小的。
 */
public class A42 {

    public static void main(String[] args) {
        int[] array = {1, 2, 4, 7, 11, 15};
        new A42().FindNumbersWithSum(array, 15);
    }

    // 对应每个测试案例，输出两个数，小的先输出。
    public ArrayList<Integer> FindNumbersWithSum(int[] array, int sum) {
        ArrayList<Integer> arrayList = new ArrayList<>(2);
        if (array == null || array.length < 2) {
            return arrayList;
        }
        int left = 0, right = array.length - 1;
        int multi = 0;
        while (left < right) {
            int result = array[left] + array[right];
            if (result == sum) {
                int temp = array[left] * array[right];
                if (multi == 0) {
                    arrayList.add(array[left]);
                    arrayList.add(array[right]);
                    multi = temp;
                } else if (multi > temp) {
                    multi = temp;
                    arrayList.set(0, array[left]);
                    arrayList.set(1, array[right]);
                }
                ++left;
                --right;
            } else if (result < sum) {
                ++left;
            } else {
                --right;
            }
        }
        return arrayList;
    }

}
