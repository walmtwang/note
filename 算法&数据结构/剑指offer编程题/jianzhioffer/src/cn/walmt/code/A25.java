package cn.walmt.code;

/**
 * 复杂链表的复制：
 * 输入一个复杂链表（每个节点中有节点值，以及两个指针，一个指向下一个节点，另一个特殊指针指向任意一个节点），
 * 返回结果为复制后复杂链表的head。
 * （注意，输出结果中请不要返回参数中的节点引用，否则判题程序会直接返回空）
 */
public class A25 {

    public static void main(String[] args) {
        RandomListNode head = new RandomListNode(1);
        head.next = new RandomListNode(2);
        head.next.next = new RandomListNode(3);
        head.random = head.next.next;
        head.next.random = head;
        head.next.next.random = head.next;
        A25 a25 = new A25();
        a25.Clone(head);
    }

    public RandomListNode Clone(RandomListNode pHead) {
        if (pHead == null) {
            return null;
        }
        createDouble(pHead);
        fixRandom(pHead);
        return splitList(pHead);
    }

    private RandomListNode splitList(RandomListNode pHead) {
        RandomListNode pHead2 = pHead.next, node = pHead, node1 = pHead.next;
        while (node != null) {
            node.next = node1.next;
            if (node.next == null) {
                node1.next = null;
            } else {
                node1.next = node.next.next;
            }
            node = node.next;
            node1 = node1.next;
        }
        return pHead2;
    }

    private void fixRandom(RandomListNode pHead) {
        RandomListNode node = pHead;
        while (node != null) {
            RandomListNode node1 = node.next;
            if (node.random == null) {
                node1.random = null;
            } else {
                node1.random = node.random.next;
            }
            node = node1.next;
        }
    }

    private void createDouble(RandomListNode pHead) {
        RandomListNode node = pHead;
        while (node != null) {
            RandomListNode randomListNode = new RandomListNode(node.label);
            randomListNode.next = node.next;
            node.next = randomListNode;
            node = node.next.next;
        }
    }

    public static class RandomListNode {
        int label;
        RandomListNode next = null;
        RandomListNode random = null;

        RandomListNode(int label) {
            this.label = label;
        }
    }
}
