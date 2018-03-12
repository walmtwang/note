package cn.walmt.code;

/**
 * 二进制中1的个数：
 * 输入一个整数，输出该数二进制表示中1的个数。
 * 其中负数用补码表示。
 */
public class A11 {
    public int NumberOf1(int n) {
        int result = 0;
          // 最简单的方法
//        String str = Integer.toBinaryString(n);
//        for (int i = 0; i < str.length(); i++) {
//            if (str.charAt(i) == '1') {
//                ++result;
//            }
//        }

          // 使用无符号右移
//        while (n != 0) {
//            if ((n & 1) == 1) {
//                ++result;
//            }
//            n >>>= 1;
//        }

        // n & (n - 1) 可以消去最左边的1
        while (n != 0) {
            ++result;
            n &= (n - 1);
        }
        return result;
    }
}
