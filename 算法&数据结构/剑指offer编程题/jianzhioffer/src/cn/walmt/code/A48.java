package cn.walmt.code;

/**
 * 不用加减乘除做加法：
 * 写一个函数，求两个整数之和，
 * 要求在函数体内不得使用+、-、*、/四则运算符号。
 * Created by walmt on 2018/2/3.
 */
public class A48 {

    public static void main(String[] args) {
        System.out.println(new A48().Add(5, -5));
    }

    public int Add(int num1,int num2) {
        int result = num1 ^ num2, temp1 = result, temp2 = num1 & num2;
        while (temp2 != 0) {
            temp2 <<= 1;
            result = temp1 ^ temp2;
            temp2 = temp1 & temp2;
            temp1 = result;
        }
        return result;
    }
}
