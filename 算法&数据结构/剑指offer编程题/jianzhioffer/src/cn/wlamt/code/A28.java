package cn.wlamt.code;

public class A28 {

    public int MoreThanHalfNum_Solution(int[] array) {
        int result = 0;
        int num = 0;
        for (int i = 0; i < array.length; i++) {
            if (num == 0) {
                result = array[i];
                ++num;
                continue;
            }
            if (result == array[i]) {
                ++num;
                continue;
            }
            if (result != array[i]) {
                --num;
                if (num == 0) {
                    result = 0;
                }
                continue;
            }
        }
        if (result == 0) {
            return result;
        }
        num = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == result) {
                ++num;
            }
        }
        if (num > array.length / 2) {
            return result;
        }
        return 0;
    }

}
