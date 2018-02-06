package cn.wlamt.code;

/**
 * 斐波那契数列：
 * 大家都知道斐波那契数列，现在要求输入一个整数n，请你输出斐波那契数列的第n项。
 * n<=39
 */
public class A7 {

    public int Fibonacci(int n) {
        if (n <= 0) {
            return 0;
        }
        int[] ints = {1 ,1};
        for (int i = 0; i < n - 2; i++) {
            int temp = ints[1];
            ints[1] = ints[0] + ints[1];
            ints[0] = temp;
        }
        return ints[1];
    }
}
