package cn.walmt.code;

/**
 * 两个链表的第一个公共结点：
 * 输入两个链表，找出它们的第一个公共结点。
 */
public class A36 {

    public ListNode FindFirstCommonNode(ListNode pHead1, ListNode pHead2) {
        if (pHead1 == null || pHead2 == null) {
            return null;
        }
        int length1 = 0, length2 = 0;
        ListNode p1 = pHead1, p2 = pHead2;
        while (p1 != null) {
            ++length1;
            p1 = p1.next;
        }
        while (p2 != null) {
            ++length2;
            p2 = p2.next;
        }
        p1 = pHead1;
        p2 = pHead2;
        if (length1 > length2) {
            for (int i = 0; i < length1 - length2; i++) {
                p1 = p1.next;
            }
        }
        if (length1 < length2) {
            for (int i = 0; i < length2 - length1; i++) {
                p2 = p2.next;
            }
        }
        while (p1 != p2) {
            p1 = p1.next;
            p2 = p2.next;
        }
        return p1;
    }

    public class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }
}
