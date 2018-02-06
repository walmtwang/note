package cn.wlamt.code;

/**
 * 删除链表中重复的结点：
 * 在一个排序的链表中，存在重复的结点，
 * 请删除该链表中重复的结点，重复的结点不保留，返回链表头指针。
 * 例如，链表1->2->3->3->4->4->5 处理后为 1->2->5
 * Created by walmt on 2018/2/4.
 */
public class A56 {

    public static void main(String[] args) {
        ListNode pHead = new ListNode(1);
        pHead.next = new ListNode(2);
        pHead.next.next = new ListNode(3);
        pHead.next.next.next = new ListNode(3);
        pHead.next.next.next.next = new ListNode(4);
        pHead.next.next.next.next.next = new ListNode(4);
        pHead.next.next.next.next.next.next = new ListNode(5);
        System.out.println(new A56().deleteDuplication(pHead));
    }

    public ListNode deleteDuplication(ListNode pHead) {
        if (pHead == null) {
            return null;
        }
        while (pHead != null && pHead.next != null && pHead.val == pHead.next.val) {
            int val = pHead.val;
            pHead = pHead.next.next;
            while (pHead != null && pHead.val == val) {
                pHead = pHead.next;
            }
        }
        if (pHead == null || pHead.next == null) {
            return pHead;
        }
        ListNode p1 = pHead, p2 = pHead.next;
        while (p2 != null && p2.next != null) {
            if (p2.val == p2.next.val) {
                int val = p2.val;
                p1.next = p2.next.next;
                p2 = p1.next;
                while (p2 != null && p2.val == val) {
                    p1.next = p2.next;
                    p2 = p1.next;
                }
                if (p2 == null) {
                    return pHead;
                }
                continue;
            }
            p1 = p1.next;
            p2 = p1.next;
        }
        return pHead;
    }

    public static class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }

}
