package cn.wlamt.code;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 二叉树中和为某一值的路径：
 * 输入一颗二叉树和一个整数，打印出二叉树中结点值的和为输入整数的所有路径。
 * 路径定义为从树的根结点开始往下一直到叶结点所经过的结点形成一条路径。
 */
public class A24 {

    private ArrayList<Integer> path = new ArrayList<>();

    public ArrayList<ArrayList<Integer>> FindPath(TreeNode root, int target) {
        ArrayList<ArrayList<Integer>> allPath = new ArrayList<>();
        if (root == null) {
            return allPath;
        }
        findPath(root, target, allPath);
        return allPath;
    }

    private void findPath(TreeNode root, int target, ArrayList<ArrayList<Integer>> allPath) {
        path.add(root.val);
        if (root.left != null) {
            findPath(root.left, target, allPath);
        }
        if (root.right != null) {
            findPath(root.right, target, allPath);
        }
        if (root.left == null && root.right == null) {
            int sum = path.stream().mapToInt(Integer::intValue).sum();
            if (sum == target) {
                ArrayList<Integer> arrayList = new ArrayList<>(path);
                allPath.add(arrayList);
            }
        }
        path.remove(path.size() - 1);
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
