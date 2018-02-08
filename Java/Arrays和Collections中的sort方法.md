# Arrays和Collections中的sort方法

### Collections.sort()方法

```Java
public static <T extends Comparable<? super T>> void sort(List<T> list) {
    list.sort(null);
}

public static <T> void sort(List<T> list, Comparator<? super T> c) {
    list.sort(c);
}
```

- 第一个重载的定义是：\<T extends Comparable\<? super T\>\> 表示该方法中传递的泛型参数必须实现了Comparable中的compareTo(T o)方法，否则进行不了sort排序。
- 第二个是提供Comparator（比较手段）。

### list.sort()方法

```Java
default void sort(Comparator<? super E> c) {
    Object[] a = this.toArray();
    Arrays.sort(a, (Comparator) c);
    ListIterator<E> i = this.listIterator();
    for (Object e : a) {
        i.next();
        i.set((E) e);
    }
}
```

- 把这个方法细分为3个步骤：
  1. 将list装换成一个对象数组。
  2. 将这个对象数组传递给Arrays类的sort方法（也就是说collections的sort其实本质是调用了Arrays.sort）。
  3. 完成排序之后，再一个一个地，把Arrays的元素复制到List中。

### Arrays.sort(Object[] a)方法

```Java
public static <T> void sort(T[] a, Comparator<? super T> c) {
    if (c == null) {
        sort(a);
    } else {
        if (LegacyMergeSort.userRequested)
            legacyMergeSort(a, c);
        else
            TimSort.sort(a, 0, a.length, c, null, 0, 0);
    }
}

public static void sort(Object[] a) {
    if (LegacyMergeSort.userRequested)
        legacyMergeSort(a);
    else
        ComparableTimSort.sort(a, 0, a.length, null, 0, 0);
}
```

- sort有一个分支判断，当LegacyMergeSort.userRequested为true的情况下，采用legacyMergeSort，否则采用ComparableTimSort。
- LegacyMergeSort.userRequested的字面意思大概就是“用户请求传统归并排序”的意思，这个分支调用的是与jdk1.5相同的方法来实现功能。
- ComparableTimSort是改进后的归并排序，对归并排序在已经反向排好序的输入时表现为O(n^2)的特点做了特别优化。对已经正向排好序的输入减少回溯。对两种情况（一会升序，一会降序）的输入处理比较好。（但对`compare()`或`compareTo()`要求更严格，必须符合自反性、对称性和传递性，不然可能会抛出异常）。


- legacyMergeSort方法最终调用的是mergeSort方法。

```Java
private static void legacyMergeSort(Object[] a) {
    Object[] aux = a.clone();
    mergeSort(aux, a, 0, a.length, 0);
}
```

### mergeSort(Object[] src, Object[] dest, int low, int high, int off)

```Java
private static void mergeSort(Object[] src,
                              Object[] dest,
                              int low,
                              int high,
                              int off) {
    int length = high - low;

    // Insertion sort on smallest arrays
  	// 如果小于阀值INSERTIONSORT_THRESHOLD = 8
  	// 使用插入排序
    if (length < INSERTIONSORT_THRESHOLD) {
        for (int i=low; i<high; i++)
            for (int j=i; j>low &&
                     ((Comparable) dest[j-1]).compareTo(dest[j])>0; j--)
                swap(dest, j, j-1);
        return;
    }

    // Recursively sort halves of dest into src
  	// 将需要进行递归排序的这部分分成两部分进行递归
    int destLow  = low;
    int destHigh = high;
    low  += off;
    high += off;
    int mid = (low + high) >>> 1;
    mergeSort(dest, src, low, mid, -off);
    mergeSort(dest, src, mid, high, -off);

    // If list is already sorted, just copy from src to dest.  This is an
    // optimization that results in faster sorts for nearly ordered lists.
  	// 如果数组已经排好序，仅仅将它从src复制到dest即可。
  	// 这是一种优化，可以使几乎有序的列表更快排序。
    if (((Comparable)src[mid-1]).compareTo(src[mid]) <= 0) {
        System.arraycopy(src, low, dest, destLow, length);
        return;
    }

    // Merge sorted halves (now in src) into dest
  	// 将已排序的部分(现在在src中)合并在dest。
    for(int i = destLow, p = low, q = mid; i < destHigh; i++) {
        if (q >= high || p < mid && ((Comparable)src[p]).compareTo(src[q])<=0)
            dest[i] = src[p++];
        else
            dest[i] = src[q++];
    }
}
```

1. 当阀值小于7的时候使用插入排序，这里的插入排序个人认为可以稍微优化一下，但一般情况下对性能影响不大。
2. 将需要进行归并排序的这部分分成两部分进行递归。
3. 如果数组已经排好序，仅仅将它从src复制到dest即可。这是一种优化，可以使几乎有序的列表更快排序。
4. 将已排序的部分(现在在src中)合并在dest。

### ComparableTimSort

- ComparableTimSort是改进后的归并排序，对归并排序在已经反向排好序的输入时表现为O(n^2)的特点做了特别优化。
- 对已经正向排好序的输入减少回溯。对两种情况（一会升序，一会降序）的输入处理比较好（摘自百度百科）。

```Java
static void sort(Object[] a, int lo, int hi, Object[] work, int workBase, int workLen) {
    assert a != null && lo >= 0 && lo <= hi && hi <= a.length;

    int nRemaining  = hi - lo;
    if (nRemaining < 2)
        return;  // Arrays of size 0 and 1 are always sorted 当数组大小为0和1的情况为已经排好序

    // If array is small, do a "mini-TimSort" with no merges
  	// 如果数组比较小，使用一个不需要合并的“迷你版的TimSort”。
    if (nRemaining < MIN_MERGE) {
        int initRunLen = countRunAndMakeAscending(a, lo, hi);
        binarySort(a, lo, hi, lo + initRunLen);
        return;
    }

    /**
     * March over the array once, left to right, finding natural runs,
     * extending short natural runs to minRun elements, and merging runs
     * to maintain stack invariant.
     */
  	/**
  	 * 从左到右，找出自然排好序的序列（natural run），把的自然升序序列通过二叉查找排序
  	 * 扩展到minRun长度的升序序列。最后合并栈中的所有升序序列，保证规则不变。
  	 */
    ComparableTimSort ts = new ComparableTimSort(a, work, workBase, workLen); //新建TimSort对象，保存栈的状态
    int minRun = minRunLength(nRemaining);
    do {
        // Identify next run
     	// 跟二叉查找插入排序一样，先找自然升序序列
        int runLen = countRunAndMakeAscending(a, lo, hi);

        // If run is short, extend to min(minRun, nRemaining)
      	// 如果自然升序的长度不够minRun，就把 min(minRun,nRemaining)长度的范围内的数列排好序
        if (runLen < minRun) {
            int force = nRemaining <= minRun ? nRemaining : minRun;
            binarySort(a, lo, lo + force, lo + runLen);
            runLen = force;
        }

        // Push run onto pending-run stack, and maybe merge
      	// 把已经排好序的数列压入栈中
        ts.pushRun(lo, runLen);
      	// 检查是不是需要合并
        ts.mergeCollapse();

        // Advance to find next run
      	// 继续寻找下一个run
        lo += runLen;
        nRemaining -= runLen;
    } while (nRemaining != 0);

    // Merge all remaining runs to complete sort
    assert lo == hi;
    ts.mergeForceCollapse();
    assert ts.stackSize == 1;
}

private static int countRunAndMakeAscending(Object[] a, int lo, int hi) {
    assert lo < hi;
    int runHi = lo + 1;
    if (runHi == hi)
        return 1;

    // Find end of run, and reverse range if descending
  	// 找出run的尾部，如果是降序则进行反转
    if (((Comparable) a[runHi++]).compareTo(a[lo]) < 0) { // Descending
        while (runHi < hi && ((Comparable) a[runHi]).compareTo(a[runHi - 1]) < 0)
            runHi++;
        reverseRange(a, lo, runHi);
    } else {                              // Ascending
        while (runHi < hi && ((Comparable) a[runHi]).compareTo(a[runHi - 1]) >= 0)
            runHi++;
    }

    return runHi - lo;
}

/**
 * 被优化的二分插入排序
 *
 * 使用二分插入排序算法给指定一部分数组排序。这是给小数组排序的最佳方案。最差情况下
 * 它需要 O(n log n) 次比较和 O(n^2)次数据移动。
 *
 * 如果开始的部分数据是有序的那么我们可以利用它们。这个方法默认数组中的位置lo(包括在内)到
 * start(不包括在内)的范围内是已经排好序的。
 *
 * @param a     被排序的数组
 * @param lo    待排序范围内的首个元素的位置
 * @param hi    待排序范围内最后一个元素的后一个位置
 * @param start 待排序范围内的第一个没有排好序的位置，确保 (lo <= start <= hi)
 * @param c     本次排序的比较器
 */
private static void binarySort(Object[] a, int lo, int hi, int start) {
    assert lo <= start && start <= hi;
    if (start == lo)
        start++;
    for ( ; start < hi; start++) {
        Comparable pivot = (Comparable) a[start];

        // Set left (and right) to the index where a[start] (pivot) belongs
        int left = lo;
        int right = start;
        assert left <= right;
        /*
         * Invariants:
         *   pivot >= all in [lo, left).
         *   pivot <  all in [right, start).
         */
        while (left < right) {
            int mid = (left + right) >>> 1;
            if (pivot.compareTo(a[mid]) < 0)
                right = mid;
            else
                left = mid + 1;
        }
        assert left == right;

        /*
         * The invariants still hold: pivot >= all in [lo, left) and
         * pivot < all in [left, start), so pivot belongs at left.  Note
         * that if there are elements equal to pivot, left points to the
         * first slot after them -- that's why this sort is stable.
         * Slide elements over to make room for pivot.
         */
        int n = start - left;  // The number of elements to move
        // Switch is just an optimization for arraycopy in default case
        switch (n) {
            case 2:  a[left + 2] = a[left + 1];
            case 1:  a[left + 1] = a[left];
                     break;
            default: System.arraycopy(a, left, a, left + 1, n);
        }
        a[left] = pivot;
    }
}

private static int minRunLength(int n) {
    assert n >= 0;
    int r = 0;      // Becomes 1 if any 1 bits are shifted off
    while (n >= MIN_MERGE) {
        r |= (n & 1);
        n >>= 1;
    }
    return n + r;
}

private static void reverseRange(Object[] a, int lo, int hi) {
    hi--;
    while (lo < hi) {
        Object t = a[lo];
        a[lo++] = a[hi];
        a[hi--] = t;
    }
}

private void pushRun(int runBase, int runLen) {
    this.runBase[stackSize] = runBase;
    this.runLen[stackSize] = runLen;
    stackSize++;
}

private void mergeCollapse() {
    while (stackSize > 1) {
        int n = stackSize - 2;
        if (n > 0 && runLen[n-1] <= runLen[n] + runLen[n+1]) {
            if (runLen[n - 1] < runLen[n + 1])
                n--;
            mergeAt(n);
        } else if (runLen[n] <= runLen[n + 1]) {
            mergeAt(n);
        } else {
            break; // Invariant is established
        }
    }
}

private void mergeForceCollapse() {
    while (stackSize > 1) {
        int n = stackSize - 2;
        if (n > 0 && runLen[n - 1] < runLen[n + 1])
            n--;
        mergeAt(n);
    }
}
```

- TimSort代码十分复杂，尤其是合并操作。合并操作由于过于复杂，我放弃了合并部分的源码解析...。
- 下面介绍TimSort排序的原理。

1. 传入的待排序数组若小于阈值MIN_MERGE（32）时，直接使用binarySort（二分排序）。
   - 二分排序原理十分简单，算是插入排序的一种优化而已。
     1. 通过`countRunAndMakeAscending()`函数从数组开头找到一组升序或严格降序（找到后翻转）的数，如果是严格降序的数，反转其使之为升序。（一定要严格降序是为了保证该排序是稳定的）
        - 升序：a≤b≤c。
        - 严格降序：a>b>c。（不可以是a≥b≥c）
     2. 使用二分查找的方法将后续的数找到要插入的位置，将后续的数插入之前的已排序数组。
2. 选取minRun大小，之后待排序数组将被分成以minRun大小为区块的一块块子数组。
   1. 设要排序的数组长度为n，通过对n不断进行二进制右移，使n<32，即保留最高的五位。如其余低位（被右移而消去的低位）存在1，则使终于结果+1。
   2. 最终minRun的取值范围保证为[16, 32]。
3. 通过`countRunAndMakeAscending()`方法找到一个自然升序的run。
4.  如果自然升序的长度不够minRun，就把min(minRun, nRemaining)长度的范围内的数列排好序（使用binarySort）。
5. 将当前的run压入栈，并判断栈内元素是否符合规定，不符合则进行merge。
   1. 规定如下：
      - 设栈的第二个run为n。
      - 如果 栈长度≥3 且 n-1的run长度≤（n的长度 + n+1的长度），则进行合并。
        - 如果n-1的 run长度<n+1的长度，合并n-1和n这两个run。
        - 否则，合并n和n+1这两个run。
      - 如果栈长度为2 且 n的长度≤n+1的长度，则合并n和n+1这两个run。
   2. merge原理：
      1. [参考该博客1.4](http://blog.csdn.net/ztkhhhhhd/article/details/53235384)
6. 重复步骤3、4、5，直到将待排序数组分成一块块run并压入栈。
7. 如果栈的大小≥2，则不断进行合并。
   - 合并规则，设n为栈的第二个元素：
     - 如果栈的大小≥3且n-1的run长度≤（n+1的长度），合并n-1和n这两个run。
     - 否则，合并n和n+1这两个run。

### 再谈Arrays.sort()

- `Arrays.sort()`对于基本类型（如int[], long[]）数组使用的是优化过的快排，而不是归并排序或者TimSort，源码不再解析。

### 总结

- `Arrays.sort()`大体分为三种排序，快排，归并和TimSort。
  - 因为基本类型无须保持稳定，因此可直接使用快排。
  - 而归并排序是JDK 7之前默认的Object[]对象数组排序方法，但因为很多时候需要排序的数组大多情况下都有某些部分是排好序的，归并排序无法充分利用，对于降序数组也没有针对性的优化。因此使用TimSort。
  - 然而TimSort由于其过于复杂，及其容易出错。在Java初期的TimSort中，还有着一些潜在的[BUG](http://www.freebuf.com/vuls/62129.html)。
- 注意：
  - 使用Arrays.sort()排序对象数组时，必须保证比较方法符合自反性、对称性和递归性，否则可能会出现BUG。[详情](http://blog.csdn.net/u010834071/article/details/50907117)