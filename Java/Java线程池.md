# Java线程池

### 线程池介绍

- 线程的创建和销毁需要占用CPU资源，频繁的创建和销毁线程会浪费大量的CPU资源，降低了CPU的吞吐率。
- 因此，线程池的诞生就是为了减少线程频繁的创建和销毁，提高CPU的利用率。
- 假设一个服务器完成一项任务所需时间为：T1 创建线程时间，T2 在线程中执行任务的时间，T3 销毁线程时间。
- 如果：T1 + T3 远大于 T2，则可以采用线程池，以提高服务器性能。

### ThreadPoolExecutor参数

- Java提供了Executor框架来实现线程池。
- Executor框架最核心的类是ThreadPoolExecutor，下面是它的构造方法及其各参数的意义。

```Java
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue,
                          ThreadFactory threadFactory,
                          RejectedExecutionHandler handler) {
    if (corePoolSize < 0 ||
        maximumPoolSize <= 0 ||
        maximumPoolSize < corePoolSize ||
        keepAliveTime < 0)
        throw new IllegalArgumentException();
    if (workQueue == null || threadFactory == null || handler == null)
        throw new NullPointerException();
    this.acc = System.getSecurityManager() == null ?
            null :
            AccessController.getContext();
    this.corePoolSize = corePoolSize;
    this.maximumPoolSize = maximumPoolSize;
    this.workQueue = workQueue;
    this.keepAliveTime = unit.toNanos(keepAliveTime);
    this.threadFactory = threadFactory;
    this.handler = handler;
}
```

| 参数名                   | 作用                                                         |
| ------------------------ | ------------------------------------------------------------ |
| corePoolSize             | 核心线程池大小                                               |
| maximumPoolSize          | 最大线程池大小                                               |
| keepAliveTime            | 线程池中超过corePoolSize数目的空闲线程最大存活时间；<br />可以使用allowCoreThreadTimeOut(true)使得核心线程也会因为空闲而终止 |
| TimeUnit                 | keepAliveTime时间单位                                        |
| workQueue                | 阻塞任务队列                                                 |
| threadFactory            | 用于创建线程的线程工厂                                       |
| RejectedExecutionHandler | 当提交任务数超过maxmumPoolSize+workQueue之和时，任务会交给RejectedExecutionHandler来处理 |

### ThreadPoolExecutor工作原理

1. 当线程池小于corePoolSize时，新提交任务将创建一个新线程执行任务，即使此时线程池中存在空闲线程。 
2. 当线程池达到corePoolSize时，新提交任务将被放入workQueue中，等待线程池中任务调度执行。
3. 当workQueue已满，且maximumPoolSize>corePoolSize时，新提交任务会创建新线程执行任务。
4. 当提交任务数超过maximumPoolSize时，新提交任务由RejectedExecutionHandler处理。
5. 当线程池中超过corePoolSize线程，空闲时间达到keepAliveTime时，关闭空闲线程。
6. 当设置allowCoreThreadTimeOut(true)时，线程池中corePoolSize线程空闲时间达到keepAliveTime也将关闭。

### 常用的四种线程池

##### FixedThreadPool详解

- FixedThreadPool被称为可重用固定线程数的线程池。下面是FixedThreadPool的源代码实现。

```Java
public static ExecutorService newFixedThreadPool(int nThreads) {
    return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
                                 new LinkedBlockingQueue<Runnable>());
}
```

- FixedThreadPool的corePoolSize和maximumPoolSize都被设置为创建FixedThreadPool时指定的参数nThreads。
- 当线程池中的线程数大于corePoolSize时，keepAliveTime为多余的空闲线程等待新任务的最长时间，超过这个时间后多余的线程将被终止。这里把keepAliveTime设置为0L，意味着多余的空闲线程会被立即终止。
- FixedThreadPool的execute()方法的运行示意图如图所示。![](img/6.png?raw=true)
- 工作过程：
  1. 如果当前运行的线程数少于corePoolSize，则创建新线程来执行任务。
  2. 在线程池完成预热之后（当前运行的线程数等于corePoolSize），将任务加入LinkedBlockingQueue。
  3. 线程执行完1中的任务后，会在循环中反复从LinkedBlockingQueue获取任务来执行。
- FixedThreadPool使用无界队列LinkedBlockingQueue作为线程池的工作队列（队列的容量为Integer.MAX_VALUE）。使用无界队列作为工作队列会对线程池带来如下影响。
  1. 当线程池中的线程数达到corePoolSize后，新任务将在无界队列中等待，因此线程池中的线程数不会超过corePoolSize。
  2. 由于1，使用无界队列时maximumPoolSize将是一个无效参数。
  3. 由于1和2，使用无界队列时keepAliveTime将是一个无效参数。
  4. 由于使用无界队列，运行中的FixedThreadPool（未执行方法shutdown()或shutdownNow()）不会拒绝任务（不会调用RejectedExecutionHandler.rejectedExecution方法）。

##### SingleThreadExecutor详解

- SingleThreadExecutor是使用单个worker线程的Executor。下面是SingleThreadExecutor的源代码实现。

```Java
public static ExecutorService newSingleThreadExecutor() {
    return new FinalizableDelegatedExecutorService(
      new TheadPoolExecutor(1, 1,
                            0L,
                            TimeUnit.MILLISECONDS,
                            new LinkedBlockingQueue<Runnable>()));
}
```

- SingleThreadExecutor的corePoolSize和maximumPoolSize被设置为1。其他参数与FixedThreadPool相同。
- SingleThreadExecutor的运行示意图如图所示。![](img/7.png?raw=true)
- SingleThreadExecutor的工作原理与FixedThreadPool一样，唯一区别就是SingleThreadExecutor只会创建一个线程去执行任务。

##### CachedThreadPool详解

- CachedThreadPool是一个会根据需要创建新线程的线程池。下面是创建CachedThreadPool的源代码。

```Java
public static ExecutorService newCachedThreadPool() {
    return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 
                                  60L, TimeUnit.SECONEDS, 
                                  new SynchronousQueue<Runnable>());
}
```

- CachedThreadPool的corePoolSize被设置为0，即corePool为空；
- maximumPoolSize被设置为Integer.MAX_VALUE，即maximumPool是无界的。
- 这里把keepAliveTime设置为60L，意味着CachedThreadPool中的空闲线程等待新任务的最长时间为60秒，空闲线程超过60秒后将会被终止。
- FixedThreadPool和SingleThreadExecutor使用无界队列LinkedBlockingQueue作为线程池的工作队列，CachedThreadPool使用没有容量的SynchronousQueue作为线程池的工作队列，但CachedThreadPool的maximumPool是无界的。
- 这意味着，如果主线程提交任务的速度高于maximumPool中线程处理任务的速度时，CachedThreadPool会不断创建新线程。
- 极端情况下，CachedThreadPool会因为创建过多线程而耗尽CPU和内存资源。
- CachedThreadPool的execute()方法的执行示意图如图所示。![](img/8.png?raw=true)
- 工作原理：
  1. 首先执行`SynchronousQueue.offer(Runnable task)`。
     - 如果当前maximumPool中有空闲线程正在执行`SynchronousQueue.poll(keepAliveTime，TimeUnit.NANOSECONDS)`，那么主线程执行offer操作与空闲线程执行的poll操作配对成功，主线程把任务交给空闲线程执行，execute()方法执行完成；
     - 否则执行下面的步骤2。
  2. 当初始maximumPool为空，或者maximumPool中当前没有空闲线程时，将没有线程执行`SynchronousQueue.poll(keepAliveTime，TimeUnit.NANOSECONDS)`。
     - 这种情况下，步骤1将失败。
     - 此时CachedThreadPool会创建一个新线程执行任务，execute()方法执行完成。
  3. 在步骤2中新创建的线程将任务执行完后，会执行`SynchronousQueue.poll(keepAliveTime，TimeUnit.NANOSECONDS)` ，这个poll操作会让空闲线程最多在SynchronousQueue中等待60秒钟。
     - 如果60秒钟内主线程提交了一个新任务（主线程执行步骤1）），那么这个空闲线程将执行主线程提交的新任务；
     - 否则，这个空闲线程将终止。
     - 由于空闲60秒的空闲线程会被终止，因此长时间保持空闲的CachedThreadPool不会使用任何资源。
- 前面提到过，SynchronousQueue是一个没有容量的阻塞队列。每个插入操作必须等待另一个线程的对应移除操作，反之亦然。CachedThreadPool使用SynchronousQueue，把主线程提交的任务传递给空闲线程执行。CachedThreadPool中任务传递的示意图如图所示。![](img/9.png?raw=true)

##### ScheduledThreadPoolExecutor详解

- ScheduledThreadPoolExecutor继承自ThreadPoolExecutor。
- 它主要用来在给定的延迟之后运行任务，或者定期执行任务。
- ScheduledThreadPoolExecutor的功能与Timer类似，但ScheduledThreadPoolExecutor功能更强大、更灵活。
- Timer对应的是单个后台线程，而ScheduledThreadPoolExecutor可以在构造函数中指定多个对应的后台线程数。

##### ScheduledThreadPoolExecutor的运行机制

- cheduledThreadPoolExecutor的执行示意图（本文基于JDK 6）如图所示。![](img/10.png?raw=true)
- DelayQueue是一个无界队列，所以ThreadPoolExecutor的maximumPoolSize在ScheduledThreadPoolExecutor中没有什么意义（设置maximumPoolSize的大小没有什么效果）。
- ScheduledThreadPoolExecutor的执行主要分为两大部分。
  1. 当调用ScheduledThreadPoolExecutor的`scheduleAtFixedRate()`方法或者`scheduleWithFixedDelay()`方法时，会向ScheduledThreadPoolExecutor的DelayQueue添加一个实现了RunnableScheduledFutur接口的ScheduledFutureTask。
  2. 线程池中的线程从DelayQueue中获取ScheduledFutureTask，然后执行任务。
- ScheduledThreadPoolExecutor为了实现周期性的执行任务，对ThreadPoolExecutor做了如下的修改。
  - 使用DelayQueue作为任务队列。
  - 获取任务的方式不同（后文会说明）。
  - 执行周期任务后，增加了额外的处理（后文会说明）。

###### ScheduledThreadPoolExecutor的实现

- 前面我们提到过，ScheduledThreadPoolExecutor会把待调度的任务（ScheduledFutureTask）放到一个DelayQueue中。
- ScheduledFutureTask主要包含3个成员变量，如下。
  - long型成员变量time，表示这个任务将要被执行的具体时间。
  - long型成员变量sequenceNumber，表示这个任务被添加到ScheduledThreadPoolExecutor中的序号。
  - long型成员变量period，表示任务执行的间隔周期。
- DelayQueue封装了一个PriorityQueue，这个PriorityQueue会对队列中的ScheduledFutureTask进行排序。
  - 排序时，time小的排在前面（时间早的任务将被先执行）。
  - 如果两个ScheduledFutureTask的time相同，就比较sequenceNumber，sequenceNumber小的排在前面（也就是说，如果两个任务的执行时间相同，那么先提交的任务将被先执行）。
- 首先，让我们看看ScheduledThreadPoolExecutor中的线程执行周期任务的过程。
- 下图是ScheduledThreadPoolExecutor中的线程1执行某个周期任务的4个步骤。![](img/11.png?raw=true)
- 下面是对这4个步骤的说明。
  1. 线程1从DelayQueue中获取已到期的ScheduledFutureTask（DelayQueue.take()）。到期任务是指ScheduledFutureTask的time大于等于当前时间。
  2. 线程1执行这个ScheduledFutureTask。
  3. 线程1修改ScheduledFutureTask的time变量为下次将要被执行的时间。
  4. 线程1把这个修改time之后的ScheduledFutureTask放回DelayQueue中（`DelayQueue.add()`）。