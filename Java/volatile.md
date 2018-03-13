# Volatile

### Volatile的特征

- 禁止指令重排（有例外）
- 可见性

### Volatile的内存语义

- 当写一个Volatile变量时，JMM会把线程对应的本地内存中的共享变量值刷新到主内存。
- 当读一个Volatile变量时，JMM会把线程对应的本地内存置为无效，线程接下来将从主内存中读取共享变量。

### Volatile的重排序

1. 当第二个操作为Volatile写操做时,不管第一个操作是什么(普通读写或者Volatile读写),都不能进行重排序。
   - 这个规则确保Volatile写之前的所有操作都不会被重排序到Volatile之后。
2. 当第一个操作为Volatile读操作时,不管第二个操作是什么,都不能进行重排序。
   - 这个规则确保Volatile读之后的所有操作都不会被重排序到Volatile之前。
3. 当第一个操作是Volatile写操作时,第二个操作是Volatile读操作,不能进行重排序。
4. 这个规则和前面两个规则一起构成了：两个Volatile变量操作不能够进行重排序。

- 除以上三种情况以外可以进行重排序：

  比如：

  1. 第一个操作是普通变量读/写，第二个是Volatile变量的读。
  2. 第一个操作是Volatile变量的写,第二个是普通变量的读/写。

### 内存屏障插入策略

- JMM基于保守策略的JMM内存屏障插入策略：
  1. 在每个volatile写操作的前面插入一个StoreStore屏障。
  2. 在每个volatile写操作的后面插入一个StoreLoad屏障。
  3. 在每个volatile读操作的后面插入一个LoadLoad屏障。
  4. 在每个volatile读操作的后面插入一个LoadStore屏障。

![](img\12.png)![](img\13.png)![](img\14.png)

- x86处理器仅仅会对写-读操作做重排序。
  - 因此会省略掉读-读、读-写和写-写操作做重排序的内存屏障。
  - 在x86中，JMM仅需在volatile后面插入一个StoreLoad屏障即可正确实现volatile写-读的内存语义。
  - 这意味着在x86处理器中，volatile写的开销比volatile读的大，因为StoreLoad屏障开销比较大。

