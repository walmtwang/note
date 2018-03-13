package cn.walmt.other;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by walmt on 2018/2/28.
 */
public class A5 {

    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        if (root == null) {
            return list;
        }
        Stack<TreeNode> stack = new Stack<>();
        TreeNode treeNode = root;
        while (!stack.isEmpty()) {
            while (treeNode != null) {
                stack.push(treeNode);
                treeNode = treeNode.right;
            }
            if (!stack.isEmpty()) {
                treeNode = stack.pop();
                list.add(treeNode.val);
                treeNode = treeNode.left;
            }
        }
        return list;
    }


    class TreeNode {
        public int val;
        public TreeNode left, right;

        public TreeNode(int val) {
            this.val = val;
            this.left = this.right = null;
        }
    }
}

