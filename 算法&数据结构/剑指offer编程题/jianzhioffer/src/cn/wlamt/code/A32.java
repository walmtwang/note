package cn.wlamt.code;

import java.util.ArrayList;
import java.util.List;

/**
 * 把数组排成最小的数：
 * 输入一个正整数数组，
 * 把数组里所有数字拼接起来排成一个数，
 * 打印能拼接出的所有数字中最小的一个。
 * 例如输入数组{3，32，321}，则打印出这三个数字能排成的最小数字为321323。
 */
public class A32 {

    public String PrintMinNumber(int[] numbers) {
        if (numbers == null || numbers.length == 0) {
            return "";
        }
        List<String> strList = new ArrayList<>(numbers.length);
        for (int i = 0; i < numbers.length; i++) {
            strList.add(String.valueOf(numbers[i]));
        }
        strList.sort((a, b) -> Integer.valueOf(a + b).compareTo(Integer.valueOf(b + a)));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strList.size(); i++) {
            sb.append(strList.get(i));
        }
        return sb.toString();
    }
}
