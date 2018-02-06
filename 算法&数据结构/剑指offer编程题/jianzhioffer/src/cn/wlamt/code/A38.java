package cn.wlamt.code;

/**
 * 二叉树的深度：
 * 输入一棵二叉树，求该树的深度。
 * 从根结点到叶结点依次经过的结点（含根、叶结点）形成树的一条路径，最长路径的长度为树的深度。
 */
public class A38 {

    public int TreeDepth(TreeNode root) {
        if (root == null)
            return 0;
        return Math.max(TreeDepth(root.left) + 1, TreeDepth(root.right) + 1);
    }

    /*public int TreeDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int[] depth = new int[1];
        searchTreeDepth(root, 1, depth);
        return depth[0];
    }

    private void searchTreeDepth(TreeNode root, int currentDepth, int[] depth) {
        if (root.left == null && root.right == null) {
            if (depth[0] < currentDepth) {
                depth[0] = currentDepth;
            }
            return;
        }

        if (root.left != null) {
            searchTreeDepth(root.left, currentDepth + 1, depth);
        }
        if (root.right != null) {
            searchTreeDepth(root.right, currentDepth + 1, depth);
        }
    }*/

    public class TreeNode {
        int val = 0;
        TreeNode left = null;
        TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;
        }
    }

}
