package cn.wlamt.code;

/**
 * 反转链表：
 * 输入一个链表，反转链表后，输出链表的所有元素。
 */
public class A15 {

    public ListNode ReverseList(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode a = null, b;
        while (head != null) {
            b = head.next;
            head.next = a;
            a = head;
            head = b;
        }
        return a;
    }

    public class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }
}
