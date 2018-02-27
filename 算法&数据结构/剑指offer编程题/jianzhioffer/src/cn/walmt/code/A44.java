package cn.walmt.code;

/**
 * 翻转单词顺序列：
 * 牛客最近来了一个新员工Fish，每天早晨总是会拿着一本英文杂志，写些句子在本子上。
 * 同事Cat对Fish写的内容颇感兴趣，有一天他向Fish借来翻看，但却读不懂它的意思。
 * 例如，“student. a am I”。
 * 后来才意识到，这家伙原来把句子单词的顺序翻转了，正确的句子应该是“I am a student.”。
 * Cat对一一的翻转这些单词顺序可不在行，你能帮助他么？
 * Created by walmt on 2018/2/3.
 */
public class A44 {

    public static void main(String[] args) {
        System.out.println(new A44().ReverseSentence("student. a am I"));
    }

    public String ReverseSentence(String str) {
        if (str == null || str.length() <= 1) {
            return str;
        }
        char[] chars = str.toCharArray();
        reverse(chars, 0, str.length() - 1);
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != ' ') {
                int left = i;
                while (i < chars.length && chars[i] != ' ') {
                    ++i;
                }
                int right = i - 1;
                reverse(chars, left, right);
            }
        }
        return new String(chars);
    }

    private void reverse(char[] chars, int left, int right) {
        while (left < right) {
            char temp = chars[left];
            chars[left] = chars[right];
            chars[right] = temp;
            ++left;
            --right;
        }
    }
}
