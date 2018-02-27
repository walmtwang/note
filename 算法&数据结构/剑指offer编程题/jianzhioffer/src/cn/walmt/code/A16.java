package cn.walmt.code;

/**
 * 合并两个排序的链表：
 * 输入两个单调递增的链表，输出两个链表合成后的链表，
 * 当然我们需要合成后的链表满足单调不减规则。
 */
public class A16 {
    public ListNode Merge(ListNode list1,ListNode list2) {
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }
        ListNode head;
        boolean oneOrTwo;
        if (list1.val < list2.val) {
            head = list1;
            oneOrTwo = false;
        } else {
            head = list2;
            oneOrTwo = true;
        }
        while (list2 != null) {
            if (!oneOrTwo) {
                while (list1.next != null && list1.next.val <= list2.val) {
                    list1 = list1.next;
                }
                ListNode temp = list1.next;
                list1.next = list2;
                list1 = temp;
            }
            if (list1 == null) {
                return head;
            }
            while (list2.next != null && list2.next.val <= list1.val) {
                list2 = list2.next;
            }
            ListNode temp = list2.next;
            list2.next = list1;
            list2 = temp;
            oneOrTwo = false;
        }
        return head;
    }

    public class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }
}
