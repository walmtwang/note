package cn.wlamt.code;

/**
 * 链表中环的入口结点：
 * 一个链表中包含环，请找出该链表的环的入口结点。
 * Created by walmt on 2018/2/4.
 */
public class A55 {

    public static void main(String[] args) {
        int index = 0;
        ListNode pHead = new ListNode(++index);
        pHead.next = new ListNode(++index);
        pHead.next.next = new ListNode(++index);
        pHead.next.next.next = new ListNode(++index);
        pHead.next.next.next.next = new ListNode(++index);
        pHead.next.next.next.next.next = pHead.next.next.next;
        ListNode listNode = new A55().EntryNodeOfLoop(pHead);

    }

    public ListNode EntryNodeOfLoop(ListNode pHead) {
        if (pHead == null) {
            return null;
        }
        ListNode p1 = pHead, p2 = pHead, node;
        p1 = p1.next;
        if (p2.next == null) {
            return null;
        }
        p2 = p2.next.next;
        while (p1 != p2) {
            if (p1 == null) {
                return null;
            }
            p1 = p1.next;
            if (p2 == null || p2.next == null) {
                return null;
            }
            p2 = p2.next.next;
        }
        node = p1;
        p1 = pHead;
        p2 = p2.next;
        int length1 = 0, length2 = 0;
        while (p1 != node) {
            p1 = p1.next;
            ++length1;
        }
        while (p2 != node) {
            p2 = p2.next;
            ++length2;
        }
        int length = Math.abs(length2 - length1);
        if (length1 > length2) {
            p1 = pHead;
            p2 = node.next;
        } else {
            p1 = node.next;
            p2 = pHead;
        }
        for (int i = 0; i < length; i++) {
            p1 = p1.next;
        }
        while (p1 != p2) {
            p1 = p1.next;
            p2 = p2.next;
        }
        return p1;
    }

    public static class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }

}
