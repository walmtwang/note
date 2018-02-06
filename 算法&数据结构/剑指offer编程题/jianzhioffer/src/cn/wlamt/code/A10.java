package cn.wlamt.code;

/**
 * 矩形覆盖：
 * 我们可以用2*1的小矩形横着或者竖着去覆盖更大的矩形。
 * 请问用n个2*1的小矩形无重叠地覆盖一个2*n的大矩形，总共有多少种方法？
 */
public class A10 {
    public int RectCover(int target) {
        if (target <= 0) {
            return 0;
        }
        if (target == 1) {
            return 1;
        }
        int[] ints = {1, 2};
        for (int i = 0; i < target - 2; i++) {
            int temp = ints[1];
            ints[1] = ints[1] + ints[0];
            ints[0] = temp;
        }
        return ints[1];
    }
}
