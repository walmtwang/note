package cn.walmt.code;

/**
 * 字符流中第一个不重复的字符：
 * 请实现一个函数用来找出字符流中第一个只出现一次的字符。
 * 例如，当从字符流中只读出前两个字符"go"时，第一个只出现一次的字符是"g"。
 * 当从该字符流中读出前六个字符“google"时，第一个只出现一次的字符是"l"。
 * Created by walmt on 2018/2/3.
 */
public class A54 {

    public static void main(String[] args) {
        A54 a54 = new A54();
        String str = "google";
        for (int i = 0; i < 3; i++) {
            a54.Insert(str.charAt(i));
        }
        System.out.println(a54.FirstAppearingOnce());
    }

    private int[] ints = new int[256];
    int index = 1;

    // 如果当前字符流没有存在出现一次的字符，返回#字符。
    // Insert one char from stringstream
    public void Insert(char ch) {
        if (ints[ch] == 0) {
            ints[ch] = index++;
        } else {
            ints[ch] = -1;
        }
    }

    // return the first appearence once char in current stringstream
    public char FirstAppearingOnce() {
        int min = Integer.MAX_VALUE;
        char c = '#';
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] > 0) {
                if (min > ints[i]) {
                    min = ints[i];
                    c = (char) i;
                }
            }
        }
        return c;
    }

    /*private StringBuffer sb = new StringBuffer("");
    private int[] ints = new int[256];

    // 如果当前字符流没有存在出现一次的字符，返回#字符。
    // Insert one char from stringstream
    public void Insert(char ch) {
        ++ints[ch];
        sb.append(ch);
    }

    // return the first appearence once char in current stringstream
    public char FirstAppearingOnce() {
        String s = sb.toString();
        for (int i = 0; i < s.length(); i++) {
            if (ints[s.charAt(i)] == 1) {
                return s.charAt(i);
            }
        }
        return '#';
    }*/

    /*private LinkedHashMap<Character, Integer> linkedHashMap = new LinkedHashMap<>();

    // 如果当前字符流没有存在出现一次的字符，返回#字符。
    // Insert one char from stringstream
    public void Insert(char ch) {
        linkedHashMap.merge(ch, 1, (a, b) -> a + b);
    }

    // return the first appearence once char in current stringstream
    public char FirstAppearingOnce() {
        for (Map.Entry<Character, Integer> entry : linkedHashMap.entrySet()) {
            if (entry.getValue() == 1) {
                return entry.getKey();
            }
        }
        return '#';
    }*/
}
