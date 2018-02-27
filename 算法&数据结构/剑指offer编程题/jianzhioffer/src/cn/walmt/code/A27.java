package cn.walmt.code;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * 字符串的排列：
 * 输入一个字符串,按字典序打印出该字符串中字符的所有排列。
 * 例如输入字符串abc,则打印出由字符a,b,c所能排列出来的所有字符串abc,acb,bac,bca,cab和cba。
 */
public class A27 {

    /*private StringBuffer stringBuffer = new StringBuffer("");
    private ArrayList<Integer> indexs = new ArrayList<>();

    public ArrayList<String> Permutation(String str) {
        ArrayList<String> strList = new ArrayList<>();
        if (str == null || str.length() == 0) {
            return strList;
        }
        permutation(str, 0, strList);
        return strList;
    }

    private void permutation(String str, int index, ArrayList<String> strList) {
        if (index == str.length()) {
            String temp = stringBuffer.toString();
            if (!strList.contains(temp)) {
                strList.add(temp);
            }
        }
        for (int i = 0; i < str.length(); i++) {
            if (indexs.contains(i)) {
                continue;
            }
            indexs.add(i);
            stringBuffer.append(str.charAt(i));
            permutation(str, index + 1, strList);
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            indexs.remove(indexs.size() - 1);
        }
    }*/

    /**
     * 优化版本
     *
     * @param str
     * @return
     */
    public ArrayList<String> Permutation(String str) {
        ArrayList<String> strList = new ArrayList<>();
        TreeSet<String> strTreeSet = new TreeSet<>();
        if (str == null || str.length() == 0) {
            return strList;
        }
        permutation(str.toCharArray(), 0, strTreeSet);
        strList.addAll(strTreeSet);
        return strList;
    }

    private void permutation(char[] chars, int index, TreeSet<String> strTreeSet) {
        if (index == chars.length - 1) {
            strTreeSet.add(String.valueOf(chars));
            return;
        }
        for (int i = index; i < chars.length; i++) {
            swap(chars, index, i);
            permutation(chars, index + 1, strTreeSet);
            swap(chars, index, i);
        }
    }

    private void swap(char[] chars, int a, int b) {
        char temp = chars[a];
        chars[a] = chars[b];
        chars[b] = temp;
    }

}
