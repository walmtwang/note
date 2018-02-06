package cn.wlamt.code;

/**
 * 替换空格：
 * 请实现一个函数，将一个字符串中的空格替换成“%20”。
 * 例如，当字符串为We Are Happy.则经过替换之后的字符串为We%20Are%20Happy。
 */
public class A2 {
    public String replaceSpace(StringBuffer str) {
        int num = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                ++num;
            }
        }
        int oldIndex = str.length() - 1, newIndex = str.length() + num * 2;
        str.setLength(newIndex);
        --newIndex;
        while (oldIndex != newIndex && oldIndex >= 0) {
            if (str.charAt(oldIndex) == ' ') {
                str.setCharAt(newIndex--, '0');
                str.setCharAt(newIndex--, '2');
                str.setCharAt(newIndex--, '%');
            } else {
                str.setCharAt(newIndex--, str.charAt(oldIndex));
            }
            --oldIndex;
        }
        return str.toString();
    }
}
