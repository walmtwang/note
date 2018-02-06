package cn.walmt.other;

import java.util.Scanner;

/**
 * 相反数：
 * 为了得到一个数的"相反数",我们将这个数的数字顺序颠倒,然后再加上原先的数得到"相反数"。
 * 例如,为了得到1325的"相反数",
 * 首先我们将该数的数字顺序颠倒,我们得到5231,之后再加上原先的数,
 * 我们得到5231+1325=6556.如果颠倒之后的数字有前缀零,前缀零将会被忽略。
 * 例如n = 100, 颠倒之后是1.
 * Created by walmt on 2018/2/5.
 */
public class A2 {

    // 输入描述:
    // 输入包括一个整数n,(1 ≤ n ≤ 10^5)
    // 输出描述:
    // 输出一个整数,表示n的相反数
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        getOutput(input);
    }

    private static void getOutput(int input) {
        int result = 0, temp = input;
        while (temp != 0) {
            result *= 10;
            result += temp % 10;
            temp /= 10;
        }
        result += input;
        System.out.println(result);
    }
}
