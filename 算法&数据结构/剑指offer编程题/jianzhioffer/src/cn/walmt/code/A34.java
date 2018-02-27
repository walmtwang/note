package cn.walmt.code;

import java.util.HashMap;

/**
 * 第一个只出现一次的字符：
 * 在一个字符串(1<=字符串长度<=10000，全部由字母组成)中找到第一个只出现一次的字符,并返回它的位置
 */
public class A34 {

    public int FirstNotRepeatingChar(String str) {
        HashMap<Character, Integer> map = new HashMap<>(26);
        for (int i = 0; i < str.length(); i++) {
            map.merge(str.charAt(i), 1, (a, b) -> a + b);
        }
        for (int i = 0; i < str.length(); i++) {
            Integer value = map.get(str.charAt(i));
            if (value == 1) {
                return i;
            }
        }
        return -1;
    }

}
