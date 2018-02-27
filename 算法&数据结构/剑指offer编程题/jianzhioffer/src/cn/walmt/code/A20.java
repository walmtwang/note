package cn.walmt.code;

import java.util.Stack;

/**
 * 包含min函数的栈：
 * 定义栈的数据结构，请在该类型中实现一个能够得到栈最小元素的min函数。
 */
public class A20 {

    private Stack<Integer> stack = new Stack<>();

    public void push(int node) {
        if (stack.isEmpty()) {
            stack.push(node);
            stack.push(node);
        } else {
            int top = stack.pop();
            stack.push(node - top);
            stack.push(node - top > 0 ? top : node);
        }
    }

    public void pop() {
        int min = stack.pop();
        int temp = stack.pop();
        if (temp >= 0) {
            stack.push(min);
        } else {
            stack.push(min - temp);
        }
    }

    public int top() {
        int min = stack.pop();
        int temp = stack.peek();
        stack.push(min);
        if (temp >= 0) {
            return temp + min;
        } else {
            return min;
        }
    }

    public int min() {
        return stack.peek();
    }
}
