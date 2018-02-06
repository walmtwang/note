package cn.wlamt.code;

import java.util.TreeSet;

/**
 * 丑数：
 * 把只包含因子2、3和5的数称作丑数（Ugly Number）。
 * 例如6、8都是丑数，但14不是，因为它包含因子7。
 * 习惯上我们把1当做是第一个丑数。
 * 求按从小到大的顺序的第N个丑数。
 */
public class A33 {

    public int GetUglyNumber_Solution(int index) {
        TreeSet<Integer> treeSet = new TreeSet<>();
        treeSet.add(1);
        int result = 0;
        for (int i = 0; i < index; i++) {
            result = treeSet.first();
            treeSet.remove(result);
            int temp = result * 2;
            if (temp < 0) {
                continue;
            }
            treeSet.add(temp);
            temp = result * 3;
            if (temp < 0) {
                continue;
            }
            treeSet.add(temp);
            temp = result * 5;
            if (temp < 0) {
                continue;
            }
            treeSet.add(temp);
        }
        return result;
    }


}
