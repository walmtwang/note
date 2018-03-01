package cn.walmt.other;

import java.util.Arrays;

/**
 * Created by walmt on 2018/3/1.
 */
public class A6 {

    public static void main(String[] args) {
        int[] arr = {2, 4, 2, 8, 22, 5, 11, 343, 2, 123, 43, 23, 87, 56, 6};
        A6.sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void sort(int[] arr) {
        int[] temp = new int[arr.length];//在排序前，先建好一个长度等于原数组长度的临时数组，避免递归中频繁开辟空间
        for (int i = 0; i < arr.length; i++) {
            temp[i] = arr[i];
        }
        sort(temp, 0, arr.length - 1, arr);
    }

    private static void sort(int[] arr, int left, int right, int[] temp) {
        if (left < right) {
            int mid = (left + right) / 2;
            sort(temp, left, mid, arr);//左边归并排序，使得左子序列有序
            sort(temp, mid + 1, right, arr);//右边归并排序，使得右子序列有序
            merge(arr, left, mid, right, temp);//将两个有序子数组合并操作
        }
    }

    private static void merge(int[] arr, int left, int mid, int right, int[] temp) {
        int i = left;//左序列指针
        int j = mid + 1;//右序列指针
        int t = left;//临时数组指针
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[t++] = arr[i++];
            } else {
                temp[t++] = arr[j++];
            }
        }
        while (i <= mid) {//将左边剩余元素填充进temp中
            temp[t++] = arr[i++];
        }
        while (j <= right) {//将右序列剩余元素填充进temp中
            temp[t++] = arr[j++];
        }
    }

}
