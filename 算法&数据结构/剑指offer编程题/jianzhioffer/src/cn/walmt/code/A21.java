package cn.walmt.code;

import java.util.Stack;

/**
 * 栈的压入、弹出序列：
 * 输入两个整数序列，第一个序列表示栈的压入顺序，请判断第二个序列是否为该栈的弹出顺序。
 * 假设压入栈的所有数字均不相等。
 * 例如序列1,2,3,4,5是某栈的压入顺序，
 * 序列4，5,3,2,1是该压栈序列对应的一个弹出序列，
 * 但4,3,5,1,2就不可能是该压栈序列的弹出序列。
 * （注意：这两个序列的长度是相等的）
 */
public class A21 {

    public boolean IsPopOrder(int [] pushA,int [] popA) {
        if (pushA == null || popA == null || pushA.length != popA.length) {
            return false;
        }
        Stack<Integer> stack = new Stack<>();
        int i = 0, j = 0;
        while (i < pushA.length) {
            while (i < pushA.length && (stack.isEmpty() || stack.peek() != popA[j])) {
                stack.push(pushA[i++]);
            }
            while (!stack.isEmpty() && stack.peek() == popA[j]) {
                stack.pop();
                ++j;
            }
        }
        if (j != popA.length) {
            return false;
        }
        return true;
    }
}
