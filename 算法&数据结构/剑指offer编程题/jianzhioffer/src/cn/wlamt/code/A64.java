package cn.wlamt.code;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 滑动窗口的最大值：
 * 给定一个数组和滑动窗口的大小，找出所有滑动窗口里数值的最大值。
 * 例如，如果输入数组{2,3,4,2,6,2,5,1}及滑动窗口的大小3，
 * 那么一共存在6个滑动窗口，他们的最大值分别为{4,4,6,6,6,5}；
 * 针对数组{2,3,4,2,6,2,5,1}的滑动窗口有以下6个：
 * {[2,3,4],2,6,2,5,1}，
 * {2,[3,4,2],6,2,5,1}，
 * {2,3,[4,2,6],2,5,1}，
 * {2,3,4,[2,6,2],5,1}，
 * {2,3,4,2,[6,2,5],1}，
 * {2,3,4,2,6,[2,5,1]}。
 * Created by walmt on 2018/2/4.
 */
public class A64 {

    public static void main(String[] args) {
        int[] ints = {2,3,4,2,6,2,5,1};
        new A64().maxInWindows(ints, 3);
    }

    public ArrayList<Integer> maxInWindows(int[] num, int size) {
        if (num == null || num.length == 0 || size <= 0 || size > num.length) {
            return new ArrayList<>(0);
        }
        ArrayList<Integer> arrayList = new ArrayList<>(num.length - size + 1);
        if (size == 1) {
            for (int i : num) {
                arrayList.add(i);
            }
            return arrayList;
        }
        LinkedList<Integer> integers = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            while (integers.size() != 0 && num[integers.peekLast()] < num[i]) {
                integers.pollLast();
            }
            integers.addLast(i);
        }
        for (int i = size; i < num.length; i++) {
            arrayList.add(num[integers.getFirst()]);
            int index = integers.peekFirst();
            if (i - index >= size) {
                integers.pollFirst();
            }
            while (integers.size() != 0 && num[integers.peekLast()] < num[i]) {
                integers.pollLast();
            }
            integers.addLast(i);
        }
        arrayList.add(num[integers.getFirst()]);
        return arrayList;
    }
}
