package cn.wlamt.code;

/**
 * 数值的整数次方：
 * 给定一个double类型的浮点数base和int类型的整数exponent。
 * 求base的exponent次方。
 */
public class A12 {
    /**
     * 递归
     * @param base
     * @param exponent
     * @return
     */
//    public double Power(double base, int exponent) {
//        if (base == 0) {
//            return 0;
//        }
//        if (exponent == 0) {
//            return 1;
//        }
//        if (exponent > 0) {
//            double temp = Power(base, exponent >> 1);
//            if ((exponent & 1) == 1) {
//                return temp * temp * base;
//            } else {
//                return temp * temp;
//            }
//        }
//        exponent = Math.abs(exponent);
//        double temp = Power(base, exponent >> 1);
//        if ((exponent & 1) == 1) {
//            return 1 / (temp * temp * base);
//        } else {
//            return 1 / (temp * temp);
//        }
//    }

    /**
     * 循环
     * @param base
     * @param exponent
     * @return
     */
    public double Power(double base, int exponent) {
        if (base == 0) {
            return 0;
        }
        if (exponent == 0) {
            return 1;
        }
        int abs = Math.abs(exponent);
        double result = 1.0;
        while (abs != 0) {
            if ((abs & 1) == 1) {
                result *= base;
            }
            abs >>= 1;
            base *= base;
        }
        if (exponent > 0) {
            return result;
        } else {
            return 1 / result;
        }
    }
}
