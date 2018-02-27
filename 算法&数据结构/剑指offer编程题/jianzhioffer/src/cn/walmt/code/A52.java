package cn.walmt.code;

/**
 * 正则表达式匹配：
 * 请实现一个函数用来匹配包括'.'和'*'的正则表达式。
 * 模式中的字符'.'表示任意一个字符，而'*'表示它前面的字符可以出现任意次（包含0次）。
 * 在本题中，匹配是指字符串的所有字符匹配整个模式。
 * 例如，字符串"aaa"与模式"a.a"和"ab*ac*a"匹配，但是与"aa.a"和"ab*a"均不匹配
 * Created by walmt on 2018/2/3.
 */
public class A52 {

    public boolean match(char[] str, char[] pattern) {
        if (str == null || pattern == null) {
            return false;
        }
        return matchCore(str, 0, pattern, 0);
    }

    private boolean matchCore(char[] str, int strIndex, char[] pattern, int patternIndex) {
        if (pattern.length <= patternIndex) {
            if (str.length <= strIndex) {
                return true;
            } else {
                return false;
            }
        }
        if (str.length <= strIndex) {
            if (pattern.length == patternIndex + 1) {
                return false;
            }
            if (pattern[patternIndex + 1] == '*') {
                return matchCore(str, strIndex, pattern, patternIndex + 2);
            }
        }
        if (pattern.length > patternIndex + 1 && pattern[patternIndex + 1] == '*') {
            if (str[strIndex] == pattern[patternIndex] || pattern[patternIndex] == '.') {
                return matchCore(str, strIndex + 1, pattern, patternIndex + 2)
                        || matchCore(str, strIndex + 1, pattern, patternIndex)
                        || matchCore(str, strIndex, pattern, patternIndex + 2);
            } else {
                return matchCore(str, strIndex, pattern, patternIndex + 2);
            }
        }
        if (pattern[patternIndex] == '.' || str[strIndex] == pattern[patternIndex]) {
            return matchCore(str, strIndex + 1, pattern, patternIndex + 1);
        }
        return false;
    }
}
