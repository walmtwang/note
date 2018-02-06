package cn.wlamt.code;

/**
 * 求1+2+3+...+n：
 * 求1+2+3+...+n，
 * 要求不能使用乘除法、for、while、if、else、switch、case
 * 等关键字及条件判断语句（A?B:C）。
 * Created by walmt on 2018/2/3.
 */
public class A47 {

    public int Sum_Solution(int n) {
        boolean result = (n != 0) && (n += Sum_Solution(n - 1)) > 0;
        return n;
    }

}
