package cn.wlamt.code;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 把二叉树打印成多行：
 * 从上到下按层打印二叉树，同一层结点从左至右输出。
 * 每一层输出一行。
 * Created by walmt on 2018/2/4.
 */
public class A60 {

    ArrayList<ArrayList<Integer>> Print(TreeNode pRoot) {
        ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<>();
        if (pRoot == null) {
            return arrayLists;
        }
        Queue<TreeNode> queue1 = new LinkedList<>(), queue2 = new LinkedList<>();
        queue1.add(pRoot);
        while (queue1.size() != 0 || queue2.size() != 0) {
            if (queue1.size() != 0) {
                printCore(arrayLists, queue1, queue2);
            } else {
                printCore(arrayLists, queue2, queue1);
            }
        }
        return arrayLists;
    }

    private void printCore(ArrayList<ArrayList<Integer>> arrayLists, Queue<TreeNode> queue1, Queue<TreeNode> queue2) {
        ArrayList<Integer> arrayList = new ArrayList<>(queue1.size());
        arrayLists.add(arrayList);
        while (queue1.size() != 0) {
            TreeNode treeNode = queue1.poll();
            arrayList.add(treeNode.val);
            if (treeNode.left != null) {
                queue2.add(treeNode.left);
            }
            if (treeNode.right != null) {
                queue2.add(treeNode.right);
            }
        }
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
