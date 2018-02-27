package cn.walmt.code;

/**
 * 变态跳台阶：
 * 一只青蛙一次可以跳上1级台阶，也可以跳上2级……它也可以跳上n级。
 * 求该青蛙跳上一个n级的台阶总共有多少种跳法。
 */
public class A9 {
    public int JumpFloorII(int target) {
        if (target <= 0) {
            return 0;
        }
//        return (int) Math.pow(2, target - 1);
        return pow(2, target - 1);
    }

    public int pow(int a, int n) {
        if (n == 0) {
            return 1;
        }
        if (n == 1) {
            return a;
        }
        int temp = pow(a, n / 2);
        if ((n & 1) == 1) {
            return temp * temp * a;
        } else {
            return temp * temp;
        }
    }
}
