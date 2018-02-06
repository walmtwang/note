package cn.wlamt.code;

/**
 * 构建乘积数组：
 * 给定一个数组A[0,1,...,n-1],
 * 请构建一个数组B[0,1,...,n-1],
 * 其中B中的元素B[i]=A[0]*A[1]*...*A[i-1]*A[i+1]*...*A[n-1]。
 * 不能使用除法。
 * Created by walmt on 2018/2/3.
 */
public class A51 {

    public int[] multiply(int[] A) {
        if (A == null || A.length == 0) {
            return null;
        }
        int[] B = new int[A.length];
        B[0] = 1;
        for (int i = 1; i < B.length; i++) {
            B[i] = B[i - 1] * A[i - 1];
        }
        int temp = 1;
        for (int i = B.length - 2; i >= 0; --i) {
            temp *= A[i + 1];
            B[i] *= temp;
        }
        return B;
    }

}
