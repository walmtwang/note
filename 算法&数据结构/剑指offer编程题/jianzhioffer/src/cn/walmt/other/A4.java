package cn.walmt.other;

import java.util.*;

/**
 * 游历魔法王国：
 * 魔法王国一共有n个城市,编号为0~n-1号,n个城市之间的道路连接起来恰好构成一棵树。
 * 小易现在在0号城市,每次行动小易会从当前所在的城市走到与其相邻的一个城市,小易最多能行动L次。
 * 如果小易到达过某个城市就视为小易游历过这个城市了,
 * 小易现在要制定好的旅游计划使他能游历最多的城市,
 * 请你帮他计算一下他最多能游历过多少个城市(注意0号城市已经游历了,游历过的城市不重复计算)。
 * Created by walmt on 2018/2/5.
 */
public class A4 {


    private static List<Integer> list;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(), l = scanner.nextInt();
        int[] num = new int[n - 1];
        for (int i = 0; i < num.length; i++) {
            num[i] = scanner.nextInt();
        }
        list = new ArrayList<>(l);
        int[] times = new int[1];
        getOutput(0, 0, l, num, 0, times);
        System.out.println(times[0] + 1);
    }

    /*public static void main(String[] args) {
        int[] num = new int[2];
        list = new ArrayList<>(3);
        int[] times = new int[1];
        getOutput(0, 0, 3, num, 0, times);
    }*/

    private static void getOutput(int current, int city, int l, int[] num, int time, int[] times) {
        if (time > times[0]) {
            times[0] = time;
        }
        if (city == l) {
            return;
        }
        list.add(current);
        for (int i = 0; i < num.length; i++) {
            if (current == num[i]) {
                if (list.contains(i + 1)) {
                    getOutput(i + 1, city + 1, l, num, time, times);
                } else {
                    getOutput(i + 1, city + 1, l, num, time + 1, times);
                }
            }
        }
        if (current != 0) {
            if (list.contains(num[current - 1])) {
                getOutput(num[current - 1], city + 1, l, num, time, times);
            } else {
                getOutput(num[current - 1], city + 1, l, num, time + 1, times);
            }
        }
        list.remove(list.size() - 1);
    }
}
