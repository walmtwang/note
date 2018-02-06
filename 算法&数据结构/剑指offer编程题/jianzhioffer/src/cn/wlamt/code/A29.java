package cn.wlamt.code;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 最小的K个数：
 * 输入n个整数，找出其中最小的K个数。
 * 例如输入4,5,1,6,2,7,3,8这8个数字，则最小的4个数字是1,2,3,4,。
 */
public class A29 {

    public static void main(String[] args) {
        int[] ints = {4, 5, 1, 6, 2, 7, 3, 8};
        System.out.println(new A29().GetLeastNumbers_Solution(ints, 4));
    }

    public ArrayList<Integer> GetLeastNumbers_Solution(int[] input, int k) {
        if (input == null || k <= 0 || k > input.length) {
            return new ArrayList<>();
        }
//        return methodOne(input, k - 1);
        return methodTwo(input, k);
    }

    /**
     * 使用最大堆
     * @param input
     * @param k
     * @return
     */
    private ArrayList<Integer> methodTwo(int[] input, int k) {
        Queue<Integer> queue = new PriorityQueue<>(k, Comparator.reverseOrder());
        for (int i = 0; i < k; i++) {
            queue.add(input[i]);
        }
        for (int i = k; i < input.length; i++) {
            if (input[i] < queue.peek()) {
                queue.remove();
                queue.add(input[i]);
            }
        }
        return new ArrayList<>(queue);
    }


    /**
     * 类似于快排的方法，会改变原数组
     *
     * @param input
     * @param k
     * @return
     */
    private ArrayList<Integer> methodOne(int[] input, int k) {
        core(input, 0, input.length - 1, k);
        ArrayList<Integer> arrayList = new ArrayList<>(k + 1);
        for (int i = 0; i <= k; i++) {
            arrayList.add(input[i]);
        }
        return arrayList;
    }

    private void core(int[] input, int left, int right, int index) {
        int leftIndex = 0;
        while (leftIndex - 1 != index) {
            int temp = input[left];
            leftIndex = left;
            int rightIndex = right + 1;
            while (leftIndex < rightIndex) {
                while (++leftIndex < rightIndex && input[leftIndex] <= temp) ;
                while (--rightIndex > leftIndex && input[rightIndex] >= temp) ;
                if (leftIndex < rightIndex) {
                    swap(input, leftIndex, rightIndex);
                }
            }
            swap(input, left, leftIndex - 1);
            if (leftIndex - 1 < index) {
                left = leftIndex;
            } else if (leftIndex - 1 > index) {
                right = leftIndex - 2;
            }
        }
    }

    private void swap(int[] input, int leftIndex, int rightIndex) {
        int temp = input[leftIndex];
        input[leftIndex] = input[rightIndex];
        input[rightIndex] = temp;
    }

}
