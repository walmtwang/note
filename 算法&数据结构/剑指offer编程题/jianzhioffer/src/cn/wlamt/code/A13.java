package cn.wlamt.code;

import java.util.ArrayList;
import java.util.List;

/**
 * 调整数组顺序使奇数位于偶数前面：
 * 输入一个整数数组，实现一个函数来调整该数组中数字的顺序，
 * 使得所有的奇数位于数组的前半部分，所有的偶数位于位于数组的后半部分，
 * 并保证奇数和奇数，偶数和偶数之间的相对位置不变。
 */
public class A13 {

    public static void main(String[] args) {
        int[] ints = {2, 4, 6, 1, 3, 5, 7};
        A13 a13 = new A13();
        a13.reOrderArray(ints);
    }

    /**
     * 使用两个数组的方法
     * @param array
     */
//    public void reOrderArray(int[] array) {
//        if (array == null || array.length == 0) {
//            return;
//        }
//        int odd = 0;
//        int even = 0;
//        List<Integer> odds = new ArrayList<>();
//        List<Integer> evens = new ArrayList<>();
//        while (odd < array.length) {
//            if ((array[odd] & 1) == 1) {
//                odds.add(array[odd]);
//            }
//            ++odd;
//        }
//        while (even < array.length) {
//            if ((array[even] & 1) == 0) {
//                evens.add(array[even]);
//            }
//            ++even;
//        }
//        for (int i = 0; i < odds.size(); i++) {
//            array[i] = odds.get(i);
//        }
//        for (int i = 0; i < evens.size(); i++) {
//            array[i + odds.size()] = evens.get(i);
//        }
//    }

    /**
     * 使用类似于插入排序的方法
     *
     * @param array
     */
    public void reOrderArray(int[] array) {
        if (array == null || array.length == 0) {
            return;
        }
        int odd = nextOdd(0, array);
        int even = nextEven(0, array);
        if (odd < even) {
            odd = nextOdd(even + 1, array);
        }
        while (odd < array.length) {
            int temp = array[odd];
            even = odd - 1;
            while (even >= 0 && (array[even] & 1) == 0) {
                array[even + 1] = array[even];
                --even;
            }
            array[even + 1] = temp;
            odd = nextOdd(odd + 1, array);
        }
    }

    private int nextEven(int index, int[] array) {
        while (index < array.length) {
            if ((array[index] & 1) == 1) {
                ++index;
            } else {
                break;
            }
        }
        return index;
    }

    private int nextOdd(int index, int[] array) {
        while (index < array.length) {
            if ((array[index] & 1) == 0) {
                ++index;
            } else {
                break;
            }
        }
        return index;
    }
}
