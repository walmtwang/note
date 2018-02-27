package cn.walmt.code;

/**
 * 旋转数组的最小数字：
 * 把一个数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。
 * 输入一个非递减排序的数组的一个旋转，输出旋转数组的最小元素。
 * 例如数组{3,4,5,1,2}为{1,2,3,4,5}的一个旋转，该数组的最小值为1。
 * NOTE：给出的所有元素都大于0，若数组大小为0，请返回0。
 */
public class A6 {

    public static void main(String[] args) {
        int[] ints = {6501,6828,6963,7036,7422,7674,8146,8468,8704,8717,9170,9359,9719,9895,9896,9913,9962,154,293,334,492,1323,1479,1539,1727,1870,1943,2383,2392,2996,3282,3812,3903,4465,4605,4665,4772,4828,5142,5437,5448,5668,5706,5725,6300,6335};
        A6 a6 = new A6();
        System.out.println(a6.minNumberInRotateArray(ints));
    }

    public int minNumberInRotateArray(int [] array) {
        if (array == null || array.length == 0) {
            return 0;
        }

        int start = 0, end = array.length - 1, mid = (start + end) / 2;
        while (end - start > 1) {
            if (array[mid] > array[start]) {
                start = mid;
            } else if (array[mid] < array[start]) {
                end = mid;
            } else {
                if (array[mid] < array[end]) {
                    end = mid;
                } else if (array[mid] > array[end]) {
                    start = mid;
                } else {
                    int min = array[start];
                    for (int i = end; i >= start; i--) {
                        if (array[i] > min) {
                            return min;
                        } else {
                            min = array[i];
                        }
                    }
                    return min;
                }
            }
            mid = (start + end) / 2;
        }
        return array[end];
    }
}
