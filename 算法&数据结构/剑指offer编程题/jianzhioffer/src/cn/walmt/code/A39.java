package cn.walmt.code;

/**
 * 平衡二叉树：
 * 输入一棵二叉树，判断该二叉树是否是平衡二叉树。
 */
public class A39 {

    public boolean IsBalanced_Solution(TreeNode root) {
        if (root == null) {
            return true;
        }
        int[] ints = new int[1];
        return isBalanced_Solution(root, ints);
    }

    private boolean isBalanced_Solution(TreeNode root, int[] depth) {
        if (root == null) {
            return true;
        }
        int[] left = new int[1], right = new int[1];
        if (!isBalanced_Solution(root.left, left) || !isBalanced_Solution(root.right, right)) {
            return false;
        }
        if (Math.abs(left[0] - right[0]) > 1) {
            return false;
        }
        depth[0] = left[0] > right[0] ? left[0] + 1 : right[0] + 1;
        return true;
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
