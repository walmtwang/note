package cn.walmt.code;

/**
 * 左旋转字符串：
 * 汇编语言中有一种移位指令叫做循环左移（ROL），
 * 现在有个简单的任务，就是用字符串模拟这个指令的运算结果。
 * 对于一个给定的字符序列S，请你把其循环左移K位后的序列输出。
 * 例如，字符序列S=”abcXYZdef”,要求输出循环左移3位后的结果，即“XYZdefabc”。
 * 是不是很简单？OK，搞定它！
 * Created by walmt on 2018/2/3.
 */
public class A43 {

    public static void main(String[] args) {
        System.out.println(new A43().LeftRotateString("abcXYZdef", 3));
    }

    /**
     * 通过字符串逆序
     * @param str
     * @param n
     * @return
     */
    public String LeftRotateString(String str, int n) {
        if (str == null || str.length() == 0 || n <= 0) {
            return str;
        }
        n %= str.length();
        char[] chars = str.toCharArray();
        reverse(chars, 0, n - 1);
        reverse(chars, n, chars.length - 1);
        reverse(chars, 0, chars.length - 1);
        return new String(chars);
    }

    private void reverse(char[] chars, int left, int right) {
        while (left < right) {
            char temp = chars[left];
            chars[left] = chars[right];
            chars[right] = temp;
            ++left;
            --right;
        }
    }

    /**
     * 字符串拼接
     * @param str
     * @param n
     * @return
     */
    /*public String LeftRotateString(String str, int n) {
        if (str == null || str.length() == 0) {
            return str;
        }
        n %= str.length();
        if (n <= 0) {
            return str;
        }
        StringBuffer result = new StringBuffer(str.substring(n));
        result.append(str.substring(0, n));
        return result.toString();
    }*/
}
