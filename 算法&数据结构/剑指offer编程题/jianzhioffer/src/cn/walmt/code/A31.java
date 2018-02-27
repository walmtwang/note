package cn.walmt.code;

/**
 * 整数中1出现的次数（从1到n整数中1出现的次数）：
 * 求出1~13的整数中1出现的次数,并算出100~1300的整数中1出现的次数？
 * 为此他特别数了一下1~13中包含1的数字有1、10、11、12、13因此共出现6次,但是对于后面问题他就没辙了。
 * ACMer希望你们帮帮他,并把问题更加普遍化,可以很快的求出任意非负整数区间中1出现的次数。
 */
public class A31 {

    public int NumberOf1Between1AndN_Solution(int n) {
        if (n <= 0) {
            return 0;
        }
        int sum = calOne(n);
        sum += calOther(n);
        return sum;
    }

    private int calOther(int n) {
        int temp = n, time = 1, result = 0;
        while (temp != 0) {
            temp /= 10;
            time *= 10;
            result += (temp / 10)  * time;
            int last = temp % 10;
            if (last == 1) {
                result += n % time + 1;
            } else if (last > 1) {
                result += time;
            }
        }
        return result;
    }

    private int calOne(int n) {
        int result = n / 10;
        if (n % 10 > 0) {
            ++result;
        }
        return result;
    }

}
