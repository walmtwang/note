package cn.wlamt.code;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * 按之字形顺序打印二叉树：
 * 请实现一个函数按照之字形打印二叉树，
 * 即第一行按照从左到右的顺序打印，
 * 第二层按照从右至左的顺序打印，
 * 第三行按照从左到右的顺序打印，
 * 其他行以此类推。
 * Created by walmt on 2018/2/4.
 */
public class A59 {

    public ArrayList<ArrayList<Integer>> Print(TreeNode pRoot) {
        ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<>();
        if (pRoot == null) {
            return arrayLists;
        }
        LinkedList<TreeNode> linkedList = new LinkedList<>();
        linkedList.add(pRoot);
        printCore(arrayLists, linkedList, 1);
        return arrayLists;
    }

    private void printCore(ArrayList<ArrayList<Integer>> arrayLists, LinkedList<TreeNode> linkedList, int index) {
        ArrayList<Integer> arrayList = new ArrayList<>(linkedList.size());
        arrayLists.add(arrayList);
        Iterator<TreeNode> iterator;
        if ((index & 1) == 1) {
            iterator = linkedList.listIterator();
        } else {
            iterator = linkedList.descendingIterator();
        }
        while (iterator.hasNext()) {
            arrayList.add(iterator.next().val);
        }
        LinkedList<TreeNode> linkedList1 = new LinkedList<>();
        while (linkedList.size() != 0) {
            TreeNode treeNode = linkedList.pollFirst();
            if (treeNode.left != null) {
                linkedList1.add(treeNode.left);
            }
            if (treeNode.right != null) {
                linkedList1.add(treeNode.right);
            }
        }
        if (linkedList1.size() == 0) {
            return;
        }
        printCore(arrayLists, linkedList1, index ^ 1);
    }

    public class TreeNode {
        int val = 0;
        TreeNode left = null;
        TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;
        }
    }

}
