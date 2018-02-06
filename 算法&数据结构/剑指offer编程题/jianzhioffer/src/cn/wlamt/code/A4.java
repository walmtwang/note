package cn.wlamt.code;

/**
 * 重建二叉树：
 * 输入某二叉树的前序遍历和中序遍历的结果，请重建出该二叉树。
 * 假设输入的前序遍历和中序遍历的结果中都不含重复的数字。
 * 例如输入前序遍历序列{1,2,4,7,3,5,6,8}和中序遍历序列{4,7,2,1,5,3,8,6}，则重建二叉树并返回。
 */
public class A4 {
    public TreeNode reConstructBinaryTree(int[] pre, int[] in) {
        return rebuildBinaryTree(pre, 0, pre.length - 1, in, 0, in.length - 1);
    }

    public TreeNode rebuildBinaryTree(int[] pre, int preStart, int preEnd, int[] in, int inStart, int inEnd) {
        if (preStart > preEnd) {
            return null;
        }
        TreeNode treeNode = new TreeNode(pre[preStart]);
        int i;
        for (i = inStart; i <= inEnd; i++) {
            if (in[i] == pre[preStart]) {
                break;
            }
        }
        treeNode.left = rebuildBinaryTree(pre, preStart + 1, preStart + i - inStart, in, inStart, i);
        treeNode.right = rebuildBinaryTree(pre, preStart + i - inStart + 1, preEnd, in, i + 1, inEnd);
        return treeNode;
    }


    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }
}
