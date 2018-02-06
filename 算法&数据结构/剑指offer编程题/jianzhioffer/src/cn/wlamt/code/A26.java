package cn.wlamt.code;

/**
 * 二叉搜索树与双向链表：
 * 输入一棵二叉搜索树，将该二叉搜索树转换成一个排序的双向链表。
 * 要求不能创建任何新的结点，只能调整树中结点指针的指向。
 */
public class A26 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(10);
        root.left = new TreeNode(6);
        root.right = new TreeNode(14);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(8);
        root.right.left = new TreeNode(12);
        root.right.right = new TreeNode(16);
        A26 a26 = new A26();
        TreeNode node = a26.Convert(root);
        System.out.println(node);
    }

    public TreeNode Convert(TreeNode pRootOfTree) {
        if (pRootOfTree == null) {
            return null;
        }
        coreConvert(pRootOfTree);
        while (pRootOfTree.left != null) {
            pRootOfTree = pRootOfTree.left;
        }
        return pRootOfTree;
    }

    private void coreConvert(TreeNode pRootOfTree) {
        if (pRootOfTree == null) {
            return;
        }
        if (pRootOfTree.left != null) {
            coreConvert(pRootOfTree.left);
            TreeNode leftNode = pRootOfTree.left;
            while (leftNode.right != null) {
                leftNode = leftNode.right;
            }
            pRootOfTree.left = leftNode;
            leftNode.right = pRootOfTree;
        }
        if (pRootOfTree.right != null) {
            coreConvert(pRootOfTree.right);
            TreeNode rightNode = pRootOfTree.right;
            while (rightNode.left != null) {
                rightNode = rightNode.left;
            }
            pRootOfTree.right = rightNode;
            rightNode.left = pRootOfTree;
        }
    }

    public static class TreeNode {
        int val = 0;
        TreeNode left = null;
        TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;
        }
    }
}
