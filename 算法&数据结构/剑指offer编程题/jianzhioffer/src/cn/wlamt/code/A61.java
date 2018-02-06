package cn.wlamt.code;

/**
 * 把二叉树打印成多行：
 * 从上到下按层打印二叉树，同一层结点从左至右输出。
 * 每一层输出一行。
 * Created by walmt on 2018/2/4.
 */
public class A61 {

    String Serialize(TreeNode root) {
        StringBuffer sb = new StringBuffer("");
        if (root == null) {
            return sb.toString();
        }
        serializeCore(root, sb);
        return sb.toString();
    }

    private void serializeCore(TreeNode root, StringBuffer sb) {
        if (root == null) {
            sb.append("#");
            return;
        } else {
            sb.append(root.val);
            sb.append(",");
        }
        serializeCore(root.left, sb);
        serializeCore(root.right, sb);
    }

    TreeNode Deserialize(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        int[] index = {0};
        char[] chars = str.toCharArray();
        return deserializeCore(chars, index);
    }

    private TreeNode deserializeCore(char[] chars, int[] index) {
        if (chars[index[0]] == '#') {
            ++index[0];
            return null;
        }
        int val = 0;
        for (; index[0] < chars.length; index[0]++) {
            if (chars[index[0]] == ',') {
                break;
            }
            val *= 10;
            val += chars[index[0]] - '0';
        }
        TreeNode treeNode = new TreeNode(val);
        ++index[0];
        treeNode.left = deserializeCore(chars, index);
        treeNode.right = deserializeCore(chars, index);
        return treeNode;
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
