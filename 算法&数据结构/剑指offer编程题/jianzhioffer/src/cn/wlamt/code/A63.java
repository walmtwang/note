package cn.wlamt.code;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 数据流中的中位数：
 * 如何得到一个数据流中的中位数？
 * 如果从数据流中读出奇数个数值，那么中位数就是所有数值排序之后位于中间的数值。
 * 如果从数据流中读出偶数个数值，那么中位数就是所有数值排序之后中间两个数的平均值。
 * Created by walmt on 2018/2/4.
 */
public class A63 {

    public static void main(String[] args) {
        A63 a63 = new A63();

        a63.Insert(5);
        a63.Insert(2);
        a63.Insert(3);
        a63.Insert(4);
        a63.Insert(1);
        a63.Insert(6);
        a63.Insert(7);
        System.out.println(a63.GetMedian());
    }

    private PriorityQueue<Integer> minHeap = new PriorityQueue<>(Comparator.naturalOrder());
    private PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
    private int index;

    public void Insert(Integer num) {
        ++index;
        minHeap.add(num);
        if ((index & 1) == 1) {
            if (maxHeap.size() != 0 && minHeap.peek() < maxHeap.peek()) {
                maxHeap.add(minHeap.remove());
                minHeap.add(maxHeap.remove());
            }
        } else {
            maxHeap.add(minHeap.remove());
        }
    }

    public Double GetMedian() {
        if (index == 0) {
            return null;
        }
        if ((index & 1) == 1) {
            return Double.valueOf(minHeap.peek());
        } else {
            return (Double.valueOf(maxHeap.peek()) + Double.valueOf(minHeap.peek())) / 2;
        }
    }

}
