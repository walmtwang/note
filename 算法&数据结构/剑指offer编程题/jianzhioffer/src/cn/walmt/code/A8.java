package cn.walmt.code;

/**
 * 跳台阶：
 * 一只青蛙一次可以跳上1级台阶，也可以跳上2级。
 * 求该青蛙跳上一个n级的台阶总共有多少种跳法。
 */
public class A8 {
    public int JumpFloor(int target) {
        if (target <= 0) {
            return 0;
        }
        if (target == 1) {
            return 1;
        }
        int[] ints = {1 ,2};
        for (int i = 0; i < target - 2; i++) {
            int temp = ints[1];
            ints[1] = ints[0] + ints[1];
            ints[0] = temp;
        }
        return ints[1];
    }
}
