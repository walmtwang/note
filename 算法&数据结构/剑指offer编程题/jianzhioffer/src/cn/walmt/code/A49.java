package cn.walmt.code;

/**
 * 把字符串转换成整数：
 * 将一个字符串转换成一个整数，要求不能使用字符串转换整数的库函数。
 * 数值为0或者字符串不是一个合法的数值则返回0
 * Created by walmt on 2018/2/3.
 */
public class A49 {

    // 输入一个字符串,包括数字字母符号,可以为空
    // 如果是合法的数值表达则返回该数字，否则返回0
    public int StrToInt(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        boolean flag = false;
        int i = 0;
        if (str.charAt(0) == '+' || str.charAt(0) == '-') {
            i = 1;
            if (str.charAt(0) == '-') {
                flag = true;
            }
            if (str.length() == 1) {
                return 0;
            }
        }
        int result = 0;
        for (; i < str.length(); i++) {
            result *= 10;
            if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                result += str.charAt(i) - '0';
            } else {
                return 0;
            }
        }
        if (flag) {
            result *= -1;
        }
        return result;
    }
}
