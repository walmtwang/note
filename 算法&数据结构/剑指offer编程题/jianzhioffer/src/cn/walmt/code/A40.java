package cn.walmt.code;

/**
 * 数组中只出现一次的数字：
 * 一个整型数组里除了两个数字之外，其他的数字都出现了两次。
 * 请写程序找出这两个只出现一次的数字。
 */
public class A40 {

    public static void main(String[] args) {
        int[] array = {1, 2, 3, 3};
        new A40().FindNumsAppearOnce(array, new int[1], new int[1]);
    }

    //num1,num2分别为长度为1的数组。传出参数
    //将num1[0],num2[0]设置为返回结果
    public void FindNumsAppearOnce(int[] array, int num1[], int num2[]) {
        if (array == null || array.length < 2) {
            return;
        }
        int result = 0;
        for (int i = 0; i < array.length; i++) {
            result ^= array[i];
        }

        int index = getFirstIndexBit(result);

        num1[0] = 0;
        num2[0] = 0;
        for (int i = 0; i < array.length; i++) {
            if (isBit(array[i], index)) {
                num1[0] ^= array[i];
            } else {
                num2[0] ^= array[i];
            }
        }
    }

    private boolean isBit(int num, int index) {
        return ((num >> index) & 1) == 0;
    }

    private int getFirstIndexBit(int num) {
        if (num == 0) {
            return 0;
        }
        int index = 0;
        while ((num & 1) == 0 && index < 32) {
            ++index;
            num >>= 1;
        }
        return index;
    }

    /*public void FindNumsAppearOnce(int[] array, int num1[], int num2[]) {
        if (array == null || array.length == 0) {
            return;
        }
        Map<Integer, Integer> map = new HashMap<>(array.length - 2);
        for (int i = 0; i < array.length; i++) {
            map.merge(array[i], 1, (a, b) -> a + b);
        }
        boolean one = false;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                if (one) {
                    num2[0] = entry.getKey();
                    return;
                } else {
                    num1[0] = entry.getKey();
                    one = true;
                }
            }
        }
    }*/
}
