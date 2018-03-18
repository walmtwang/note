# [队列同步器](http://blog.csdn.net/u010723709/article/details/50357247)

- 队列同步器AbstractQueuedSynchronizer(AQS)，是用来构建锁或者其他同步组件的基础框架，它使用了一个int成员变量表示同步状态，通过内置的FIFO队列来完成资源获取线程的排队工作。
- 下图显示了java.concurrent包的实现示意图：

![](img/4.jpg)

- 同步器的主要使用方式是继承，一般作为同步器组件的静态内部类，在同步器中仅定义了与状态相关的方法，且这个状态既可以独占地获取又可以共享的获取，这样就可以实现不同类型的同步组件（ReetrantLock、ReetrantReadWriteLock和CountDownLatch等）。
- 同步器是同步组件实现锁的关键，我们通常使用同步组件来实现各种锁的功能，而其内部实际上是利用同步器进行锁的实现。**它简化了锁的实现方式，屏蔽了同步状态管理、线程的排队、等待与唤醒等底层操作。**

---

## 队列同步器的接口

- 同步器的设计是基于模板的。使用者需要重写同步器指定的方法，然后将同步器组合在自定义同步组件的视线中，并调用同步器提供的模板方法。而这些模板方法就是调用同步器使用者重写的方法。

### 三个核心方法

- 以下三个方法是与同步状态有关的方法，重写同步器指定的方法时，需要使用同步器提供的如下3个方法来获取或修改同步状态：
  - `getState()`：获取当前同步状态。
  - `setState(int newState)`：设置当前同步状态。
  - `compareAndSetState(int expect, int update)`：使用CAS设置当前状态，该方法能保证状态设置的原子性。

### 可重写的方法

- `tryAcquire(int arg) `：独占式获取同步状态，该方法需要查询当前状态并判断同步状态是否符合预期，然后再进行CAS设置同步状态。

- `tryRelease(int arg)` ：独占式释放同步状态，等待获取同步状态的线程将有机会获取同步状态。


- `tryAcquireShared(int arg)`：共享式获取同步状态，返回大于等于0的值，表示获取成功，否则失败。


- `tryReleaseShared(int arg)`：共享式释放同步状态。


- `isHeldExclusively()`：当前同步器是否在独占模式下被线程占用，一般该方法表示是否被前当线程多独占。

### 模板方法

- `acquire(int arg)`：独占式获取同步状态，如果当前线程获取同步状态成功，则由该方法返回，否则，将会进入同步队列等待，该方法将会调用重写的tryAcquire(int arg) 方法。
- `acquireInterruptibly(int arg)`：与`acquire(int arg)`相同，但是该方法响应中断，当前线程未获取到同步状态而进入同步队列中，如果当前被中断，则该方法会抛出InterruptedException并返回。
- `tryAcquireNanos(int arg,long nanos)`：在acquireInterruptibly基础上增加了超时限制，如果当前线程在超时时间内没有获取到同步状态，那么将会返回false，获取到了返回true。
- `release(int arg)`：独占式的释放同步状态，该方法会在释放同步状态之后，将同步队列中第一个节点包含的线程唤醒。
- `acquireShared(int arg)`：共享式获取同步状态，如果当前线程未获取到同步状态，将会进入同步队列等待。与独占式的不同是同一时刻可以有多个线程获取到同步状态。
- `acquireSharedInterruptibly(int arg)`：与acquire(int arg) 相同，但是该方法响应中断，当前线程未获取到同步状态而进入同步队列中，如果当前被中断，则该方法会抛出InterruptedException并返回。
- `tryAcquireSharedNanos(int arg,long nanos)`：在acquireSharedInterruptibly基础上增加了超时限制，如果当前线程在超时时间内没有获取到同步状态，那么将会返回false，获取到了返回true。
- `releaseShared(int arg)`：共享式释放同步状态。

- `getQueuedThreads()`：获取等待在同步队列上的线程集合。

---

## 同步队列

- 队列同步器的实现依赖内部的同步队列来完成同步状态的管理。它是一个FIFO的双向队列，当线程获取同步状态失败时，同步器会将当前线程和等待状态等信息包装成一个节点并将其加入同步队列，同时会阻塞当前线程。当同步状态释放时，会把首节点中的线程唤醒，使其再次尝试获取同步状态。
- 节点是构成同步队列的基础，同步器拥有首节点和尾节点，没有成功获取同步状态的线程会成为节点加入该队列的尾部，其结构如下图所示：

![](img/45.png)

- 同步器包含了两个节点类型的引用，一个指向头节点，而另一个指向尾节点。如果一个线程没有获得同步队列，那么包装它的节点将被加入到队尾，显然这个过程应该是线程安全的。因此同步器提供了一个基于CAS的设置尾节点的方法：compareAndSetTail(Node expect,Node update),它需要传递一个它认为的尾节点和当前节点，只有设置成功，当前节点才被加入队尾。这个过程如下所示：

![](img/46.png)

- 同步队列遵循FIFO，首节点是获取同步状态成功的节点，首节点线程在释放同步状态时，将会唤醒后继节点，而后继节点将会在获取同步状态成功时将自己设置为首节点，这一过程如下：

![](img/47.png)

---

## 独占式同步状态的获取与释放

### 同步状态的获取

- 在前面的部分已经提到，独占式获取同步状态的方法是`acquried(int arg)`，该方法对中断不敏感，也就是由于线程获取同步状态失败后进入同步队列中，后续对线程进行中断操作时，线程不会从同步队列移除，其源代码如下：

```Java
public final void acquire(int arg) {
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}
```

- 这里面主要完成的工作是同步状态获取、节点构造、加入同步队列以及在同步队列中自旋等操作，其主要逻辑是：
  1. 调用自定义同步器的`tryAcquire(int arg)`方法，该方法保证线程安全的获取同步状态。
  2. 如果获取失败，就构造一个独占式（Node.EXCLUSIVE）的同步节点，并通过addWaiter方法加入到同步节点的尾部。
  3. 最后调用acquiredQueued方法，是的该节点以“死循环”的方式获取同步状态。如果获取不到则阻塞节点中的线程，而被阻塞线程的唤醒主要依靠前驱节点的出队或阻塞线程中断来实现。

##### addWaiter

```Java
private Node addWaiter(Node mode) {
    // 为当前线程构造一个结点
    Node node = new Node(Thread.currentThread(), mode);
    // Try the fast path of enq; backup to full enq on failure
    // 使用CAS尝试插入尾部
    Node pred = tail;
    if (pred != null) {
        node.prev = pred;
        if (compareAndSetTail(pred, node)) {
            pred.next = node;
            return node;
        }
    }
    // 前面第一次插入失败，调用该函数进行插入尾部
    enq(node);
    return node;
}
```

##### enq

```Java
private Node enq(final Node node) {
    // 死循环插入尾部，直到插入成功为止
    for (;;) {
        Node t = tail;
        if (t == null) { // 如果尾部为空，则进行初始化
            if (compareAndSetHead(new Node()))
                tail = head;
        } else {
            node.prev = t;
            // 使用CAS设置尾结点
            if (compareAndSetTail(t, node)) {
                t.next = node;
                return t;
            }
        }
    }
}
```

- 上述两个方法是在保证线程安全的情况下，利用死循环不断地尝试设置尾节点。那么节点进入同步队列以后，就要进入一个等待阶段。这是一个自旋的过程，每个节点都在不停地观察，看看有没有机会获取同步状态。如果获取到同步状态，就可以从自旋过程中退出。

##### acquireQueued方法

```Java
final boolean acquireQueued(final Node node, int arg) {
    boolean failed = true;
    try {
        boolean interrupted = false;
        for (;;) {
            final Node p = node.predecessor();
            // 只有前驱结点是头结点才尝试获取同步状态
            if (p == head && tryAcquire(arg)) {
                setHead(node);
                p.next = null; // help GC
                failed = false;
                return interrupted;
            }
            // 使用线程进入阻塞状态，被唤醒后判断是否被中断了
            if (shouldParkAfterFailedAcquire(p, node) &&
                parkAndCheckInterrupt())
                interrupted = true;
        }
    } finally {
        if (failed)
            cancelAcquire(node);
    }
}
```

- 在这个方法中可以看到，线程在死循环中尝试获取同步状态，并且只有前驱节点为头节点的时候才会获取，这是因为头节点是获取了同步状态的节点，之后它释放了同步状态才会唤醒后继节点。
- 下图描述了节点自旋获取同步状态的情况：

![](img/48.png)

- 在上图中，由于非首节点线程前驱节点出队或者被中断而从等待返回，随后检查自己的前驱是否不是首节点，如果是则尝试获取同步状态。可以到节点间并没有通讯，只是在不断地检查自己的前驱是否为头节点。对于一个锁来说，获取到同步状态就相当于获取到了锁。

![](img/49.png)

### 同步状态的释放

- 队里通过调用同步器的release的方法进行同步状态的释放，该方法释放了同步状态后，就会唤醒其后继节点。其源代码如下：

```Java
public final boolean release(int arg) {
    if (tryRelease(arg)) {
        Node h = head;
        if (h != null && h.waitStatus != 0)
            unparkSuccessor(h);
        return true;
    }
    return false;
}
```

- 该方法执行时，会唤醒头节点的后继节点线程，unparkSuccessor通过使用LockSupport唤醒处于等待状态的线程。

### 总结

- 在获取同步状态时，同步器维护这一个同步队列，并持有对头节点和尾节点的引用。获取状态失败的线程会被包装成节点加入到尾节点后面称为新的尾节点，在进入同步队列后开始自旋，停止自旋的条件就是前驱节点为头节点并且成功获取到同步状态。在释放同步状态时，同步器调用tryRelease方法释放同步状态，然后唤醒头节点的后继节点。

---

## 共享式同步状态获取与释放

- 共享式获取与独占式获取的区别就是同一时刻是否可以多个线程同时获取到同步状态。以文件的读写来说，读操作的话同一时刻可以有很多线程在进行并阻塞写操作，但是写操作只能有一个线程在写并阻塞所有读操作。

### 同步状态获取

- 通过调用同步器的`acquireShare(int arg)`方法可以共享式地获取同步状态。

```Java
public final void acquireShared(int arg) {
    if (tryAcquireShared(arg) < 0)
        doAcquireShared(arg);
}

private void doAcquireShared(int arg) {
    final Node node = addWaiter(Node.SHARED);
    boolean failed = true;
    try {
        boolean interrupted = false;
        for (;;) {
            final Node p = node.predecessor();
            if (p == head) {
                int r = tryAcquireShared(arg);
                if (r >= 0) {
                    setHeadAndPropagate(node, r);
                    p.next = null; // help GC
                    if (interrupted)
                        selfInterrupt();
                    failed = false;
                    return;
                }
            }
            if (shouldParkAfterFailedAcquire(p, node) &&
                parkAndCheckInterrupt())
                interrupted = true;
        }
    } finally {
        if (failed)
            cancelAcquire(node);
    }
}
```

- 在`acquireShared(int arg)`方法中，同步器调用`tryAcquireShared(int arg)`方法尝试获取同步状态，`tryAcquireShared(int arg)`方法返回值为int类型，当返回值大于等于0时，表示能够获取到同步状态。
- 因此，在共享式获取的自旋过程中，成功获取到同步状态并退出自旋的条件就是`tryAcquireShared(int arg)`方法返回值大于等于0。

### 同步状态释放

```Java
public final boolean releaseShared(int arg) {
    if (tryReleaseShared(arg)) {
        doReleaseShared();
        return true;
    }
    return false;
}

private void doReleaseShared() {
    for (;;) {
        Node h = head;
        if (h != null && h != tail) {
            int ws = h.waitStatus;
            if (ws == Node.SIGNAL) {
                if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0))
                    continue;            // loop to recheck cases
                unparkSuccessor(h);
            }
            else if (ws == 0 &&
                     !compareAndSetWaitStatus(h, 0, Node.PROPAGATE))
                continue;                // loop on failed CAS
        }
        if (h == head)                   // loop if head changed
            break;
    }
}
```

- 由于这是共享式地，因此释放同步状态时可能有多个线程在进行释放的操作。因此这里面使用了CAS来保证线程安全。