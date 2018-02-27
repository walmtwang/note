package cn.walmt.code;

import java.util.ArrayList;

/**
 * 和为S的连续正数序列：
 * 小明很喜欢数学,有一天他在做数学作业时,要求计算出9~16的和,他马上就写出了正确答案是100。
 * 但是他并不满足于此,他在想究竟有多少种连续的正数序列的和为100(至少包括两个数)。
 * 没多久,他就得到另一组连续正数和为100的序列:18,19,20,21,22。
 * 现在把问题交给你,你能不能也很快的找出所有和为S的连续正数序列?
 * Good Luck!
 */
public class A41 {

    public static void main(String[] args) {
        System.out.println(new A41().FindContinuousSequence(2));
    }

    // 输出所有和为S的连续正数序列。序列内按照从小至大的顺序，序列间按照开始数字从小到大的顺序
    public ArrayList<ArrayList<Integer>> FindContinuousSequence(int sum) {
        ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<>();
        if (sum <= 2) {
            return arrayLists;
        }
        int left = 1, right = 2, result = 3;
        while (left < right) {
            if (result < sum) {
                ++right;
                result += right;
            } else if (result > sum) {
                result -= left;
                ++left;
            } else if (result == sum) {
                add(arrayLists, left, right);
                ++right;
                result += right;
                result -= left;
                ++left;
            }
        }
        return arrayLists;
    }

    private void add(ArrayList<ArrayList<Integer>> arrayLists, int left, int right) {
        ArrayList<Integer> arrayList = new ArrayList<>(right - left + 1);
        for (int i = left; i <= right; i++) {
            arrayList.add(i);
        }
        arrayLists.add(arrayList);
    }

}
