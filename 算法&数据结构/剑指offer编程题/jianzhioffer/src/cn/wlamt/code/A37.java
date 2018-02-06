package cn.wlamt.code;

/**
 * 数字在排序数组中出现的次数：
 * 统计一个数字在排序数组中出现的次数。
 */
public class A37 {

    /**
     * 使用二分查找找到其中一个位置等于k的下标，然后从下标开始向前后者向后遍历，计算出次数
     * @param array
     * @param k
     * @return
     */
    /*public int GetNumberOfK(int[] array, int k) {
        if (array == null || array.length == 0) {
            return 0;
        }
        int index = search(array, k);
        if (index == -1) {
            return 0;
        }
        int result = 1;
        for (int i = index - 1; i >= 0 && array[i] == k; i--) {
            ++result;
        }
        for (int i = index + 1; i < array.length && array[i] == k; i++) {
            ++result;
        }
        return result;
    }

    private int search(int[] array, int k) {
        int left = 0, right = array.length - 1, mid = (array.length - 1) / 2;
        while (left <= right) {
            if (array[mid] == k) {
                return mid;
            }
            if (array[mid] < k) {
                left = mid + 1;
            }
            if (array[mid] > k) {
                right = mid - 1;
            }
            mid = (left + right) / 2;
        }
        return -1;
    }*/

    /**
     * 使用二分查找找出第一个k和后一个k的下标，然后求差值
     * @param array
     * @param k
     * @return
     */
    public int GetNumberOfK(int [] array , int k) {
        if (array == null || array.length == 0) {
            return 0;
        }

        int left = getTheFirstIndex(array, 0, array.length - 1, k);
        if (left == -1) {
            return 0;
        }
        int right = getTheLastIndex(array, 0, array.length - 1, k);
        return right - left + 1;
    }

    private int getTheFirstIndex(int[] array, int left, int right, int target) {
        int mid = (left + right) >> 1;
        while (left <= right) {
            if (array[mid] > target) {
                right = mid - 1;
            } else if (array[mid] < target) {
                left = mid + 1;
            } else if (mid > 0 && array[mid - 1] == target) {
                right = mid - 1;
            } else {
                return mid;
            }
            mid = (left + right) >> 1;
        }
        return -1;
    }

    private int getTheLastIndex(int[] array, int left, int right, int target) {
        int mid = (left + right) >> 1;
        while (left <= right) {
            if (array[mid] > target) {
                right = mid - 1;
            } else if (array[mid] < target) {
                left = mid + 1;
            } else if (mid < array.length - 1 && array[mid + 1] == target) {
                left = mid + 1;
            } else {
                return mid;
            }
            mid = (left + right) >> 1;
        }
        return -1;
    }
}
