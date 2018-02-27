package cn.walmt.code;

/**
 * 树的子结构：
 * 输入两棵二叉树A，B，判断B是不是A的子结构。
 * （ps：我们约定空树不是任意一个树的子结构）
 */
public class A17 {

    public boolean HasSubtree(TreeNode root1,TreeNode root2) {
        if (root1 == null || root2 == null) {
            return false;
        }
        return testAllNode(root1, root2);
    }

    private boolean testAllNode(TreeNode root1, TreeNode root2) {
        if (root1 == null) {
            return false;
        }
        if (root1.val == root2.val) {
            boolean result = isSubTree(root1, root2);
            if (result) {
                return true;
            }
        }
        boolean result = testAllNode(root1.left, root2);
        if (result) {
            return true;
        } else {
            return testAllNode(root1.right, root2);
        }
    }

    private boolean isSubTree(TreeNode root1, TreeNode root2) {
        if (root2 == null) {
            return true;
        }
        if (root1 == null) {
            return false;
        }
        if (root1.val == root2.val) {
            return isSubTree(root1.left, root2.left) && isSubTree(root1.right, root2.right);
        }
        return false;
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
