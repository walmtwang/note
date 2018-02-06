package cn.wlamt.code;

/**
 * 表示数值的字符串：
 * 请实现一个函数用来判断字符串是否表示数值（包括整数和小数）。
 * 例如，字符串"+100","5e2","-123","3.1416"和"-1E-16"都表示数值。
 * 但是"12e","1a3.14","1.2.3","+-5"和"12e+4.3"都不是。
 * Created by walmt on 2018/2/3.
 */
public class A53 {

    public static void main(String[] args) {
        String str = "";
        System.out.println(new A53().isNumeric(str.toCharArray()));;
    }
    public boolean isNumeric(char[] str) {
        if (str == null || str.length == 0) {
            return false;
        }
        return new String(str).matches("[+-]?[0-9]*(\\.[0-9]*)?([eE][+-]?[0-9]+)?");
    }

    /*public boolean isNumeric(char[] str) {
        if (str == null || str.length == 0) {
            return false;
        }
        boolean hasSign = false, hasE = false, hasPoint = false;
        for (int i = 0; i < str.length; i++) {
            if (isNum(str[i])) {
            } else if (isSign(str[i])) {
                if (hasSign) {
                    if (!isE(str[i - 1])) {
                        return false;
                    }
                } else {
                    if (i == 0 || isE(str[i - 1])) {
                        hasSign = true;
                    } else {
                        return false;
                    }
                }
            } else if (isPoint(str[i])) {
                if (hasE || hasPoint) {
                    return false;
                }
                hasPoint = true;
            } else if (isE(str[i])) {
                if (hasE || i == 0 || i + 1 == str.length) {
                    return false;
                } else if (i == 1 && isSign(str[0])) {
                    return false;
                }
                hasE = true;
            } else {
                return false;
            }
        }
        return true;
    }*/

    private boolean isNum(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isE(char c) {
        return c == 'e' || c == 'E';
    }

    private boolean isPoint(char c) {
        return c == '.';
    }

    private boolean isSign(char c) {
        return c == '+' || c == '-';
    }

}
