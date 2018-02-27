package cn.walmt.code;

/**
 * 二叉搜索树的第k个结点：
 * 给定一颗二叉搜索树，请找出其中的第k大的结点。
 * 例如，
 *      5
 *     /\
 *   3  7
 *  /\ /\
 * 2 4 6 8
 * 中，按结点数值大小顺序第三个结点的值为4。
 * Created by walmt on 2018/2/4.
 */
public class A62 {

    TreeNode KthNode(TreeNode pRoot, int k) {
        int[] index = {0};
        return getTheNVal(pRoot, index, k);
    }

    private TreeNode getTheNVal(TreeNode node, int[] index, int k) {
        if (node == null) {
            return null;
        }
        TreeNode node1 = getTheNVal(node.left, index, k);
        if (node1 != null) {
            return node1;
        }
        if (++index[0] == k) {
            return node;
        }
        return getTheNVal(node.right, index, k);
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
