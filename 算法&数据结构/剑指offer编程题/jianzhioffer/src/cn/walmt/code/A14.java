package cn.walmt.code;

/**
 * 链表中倒数第k个结点：
 * 输入一个链表，输出该链表中倒数第k个结点。
 */
public class A14 {
    /**
     * 遍历两次
     * @param head
     * @param k
     * @return
     */
//    public ListNode FindKthToTail(ListNode head,int k) {
//        int length = 0;
//        ListNode listNode = head;
//        while (listNode != null) {
//            ++length;
//            listNode = listNode.next;
//        }
//        if (length < k) {
//            return null;
//        }
//        for (int i = 0; i < length - k; i++) {
//            head = head.next;
//        }
//        return head;
//    }

    /**
     * 遍历一次
     * @param head
     * @param k
     * @return
     */
    public ListNode FindKthToTail(ListNode head,int k) {
        ListNode listNode = head, listNode1 = head;
        for (int i = 0; i < k; i++) {
            if (listNode == null) {
                return null;
            }
            listNode = listNode.next;
        }
        while (true) {
            if (listNode == null) {
                return listNode1;
            }
            listNode = listNode.next;
            listNode1 = listNode1.next;
        }
    }

    class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }
}
