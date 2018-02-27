package cn.walmt.code;

/**
 * 二叉搜索树的后序遍历序列：
 * 输入一个整数数组，判断该数组是不是某二叉搜索树的后序遍历的结果。
 * 如果是则输出Yes,否则输出No。假设输入的数组的任意两个数字都互不相同。
 */
public class A23 {

    public static void main(String[] args) {
        int[] ints = {7,4,6,5};
        A23 a23 = new A23();
        a23.VerifySquenceOfBST(ints);
    }

    public boolean VerifySquenceOfBST(int[] sequence) {
        if (sequence == null || sequence.length == 0) {
            return false;
        }
        return verifySquenceOfBST(sequence, 0, sequence.length - 1);
    }

    private boolean verifySquenceOfBST(int[] sequence, int left, int right) {
        if (left >= right) {
            return true;
        }
        int root = sequence[right];
        int splitLeft;
        for (splitLeft = left; splitLeft < right; splitLeft++) {
            if (sequence[splitLeft] > root) {
                break;
            }
        }
        int splitRight;
        for (splitRight = right - 1; splitRight >= left; splitRight--) {
            if (sequence[splitRight] < root) {
                break;
            }
        }
        if (splitLeft - splitRight != 1) {
            return false;
        }
        boolean result = verifySquenceOfBST(sequence, left, splitLeft - 1);
        if (result) {
            return verifySquenceOfBST(sequence, splitLeft, right - 1);
        }
        return false;
    }
}
