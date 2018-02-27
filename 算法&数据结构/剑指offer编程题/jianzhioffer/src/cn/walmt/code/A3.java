package cn.walmt.code;

import java.util.ArrayList;
import java.util.Stack;

/**
 * 从尾到头打印链表：
 * 输入一个链表，从尾到头打印链表每个节点的值。
 */
public class A3 {
//    /**
//     * 第一种方法,用栈
//     * @param listNode
//     * @return
//     */
//    public ArrayList<Integer> printListFromTailToHead(ListNode listNode) {
//        Stack<Integer> stack = new Stack<>();
//        int length = 0;
//        while (listNode != null) {
//            stack.push(listNode.val);
//            listNode = listNode.next;
//            ++length;
//        }
//        ArrayList<Integer> list = new ArrayList<>(length);
//        for (int i = 0; i < length; i++) {
//            list.add(stack.pop());
//        }
//        return list;
//    }

    /**
     * 第二种方法,递归
     * @param listNode
     * @return
     */
    public ArrayList<Integer> printListFromTailToHead(ListNode listNode) {
        ArrayList<Integer> list = new ArrayList<>();
        getVal(listNode, list);
        return list;
    }

    private void getVal(ListNode listNode, ArrayList<Integer> arrayList) {
        if (listNode == null) {
            return;
        }
        getVal(listNode.next, arrayList);
        arrayList.add(listNode.val);
    }

    class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }
}

 
