# [ThreadLocal详解](https://www.cnblogs.com/dolphin0520/p/3920407.html)

### 对ThreadLocal的理解

- ThreadLocal翻译成中文比较准确的叫法应该是：线程局部变量。
- ThreadLocal为变量在每个线程中都创建了一个副本，那么每个线程可以访问自己内部的副本变量。
- ThreadLocal在每个线程中对该变量会创建一个副本，即每个线程内部都会有一个该变量，且在线程内部任何地方都可以使用，线程之间互不影响，这样一来就不存在线程安全问题，也不会严重影响程序执行性能。
- 但是要注意，虽然ThreadLocal能够解决上面说的问题，但是由于在每个线程中都创建了副本，所以要考虑它对资源的消耗，比如内存的占用会比不使用ThreadLocal要大。

### 深入解析ThreadLocal类

- 先了解一下ThreadLocal类提供的几个方法：

```Java
public T get() { }
public void set(T value) { }
public void remove() { }
protected T initialValue() { }
```

- get()方法是用来获取ThreadLocal在当前线程中保存的变量副本，set()用来设置当前线程中变量的副本，remove()用来移除当前线程中变量的副本，initialValue()是一个protected方法，一般是用来在使用时进行重写的，它是一个延迟加载方法，下面会详细说明。

##### get()方法

```Java
public T get() {
    // 取得当前线程
    Thread t = Thread.currentThread();
    // 获取当前线程的ThreadLocalMap
    ThreadLocalMap map = getMap(t);
    // map不为空
    if (map != null) {
        // 获取ThreadLocal对应的entry，（ThreadLocal为key）
        ThreadLocalMap.Entry e = map.getEntry(this);
        // entry不为空
        if (e != null) {
            // 获取value
            @SuppressWarnings("unchecked")
            T result = (T)e.value;
            return result;
        }
    }
    // 如果map为空，则调用setInitialValue方法返回value。
    return setInitialValue();
}

ThreadLocalMap getMap(Thread t) {
    return t.threadLocals;
}
```

- 在getMap中，是调用当期线程t，返回当前线程t中的一个成员变量threadLocals。
- threadLocals实际上就是一个ThreadLocalMap，这个类型是ThreadLocal类的一个内部类，我们继续取看ThreadLocalMap的实现：

```Java
static class ThreadLocalMap {
    static class Entry extends WeakReference<ThreadLocal<?>> {
        /** The value associated with this ThreadLocal. */
        Object value;

        Entry(ThreadLocal<?> k, Object v) {
            super(k);
            value = v;
        }
    }
}
```

- 可以看到ThreadLocalMap的Entry继承了WeakReference，并且使用ThreadLocal作为键值。
- 然后再继续看setInitialValue方法的具体实现：

```Java
private T setInitialValue() {
    T value = initialValue();
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    // 如果map不为空，就设置键值对，为空，再创建Map
    if (map != null)
        map.set(this, value);
    else
        createMap(t, value);
    return value;
}

protected T initialValue() {
    return null;
}

void createMap(Thread t, T firstValue) {
    t.threadLocals = new ThreadLocalMap(this, firstValue);
}
```

- 至此，ThreadLocal的get方法大致解析完。

1. 首先，在每个线程Thread内部有一个ThreadLocal.ThreadLocalMap类型的成员变量threadLocals，这个threadLocals就是用来存储实际的变量副本的，键值为当前ThreadLocal变量，value为变量副本（即T类型的变量）。
2. 初始时，在Thread里面，threadLocals为空，当通过ThreadLocal变量调用get()方法或者set()方法，就会对Thread类中的threadLocals进行初始化，并且以当前ThreadLocal变量为键值，以ThreadLocal要保存的副本变量为value，存到threadLocals。
3. 然后在当前线程里面，如果要使用副本变量，就可以通过get方法在threadLocals里面查找。

- 下面给出一个ThreadLocal的使用例子：

```Java
public class ThreadLocalsTest {

    private ThreadLocal<Long> longLocal = new ThreadLocal<>();
    private ThreadLocal<String> strLocal = new ThreadLocal<>();

    public void set() {
        longLocal.set(Thread.currentThread().getId());
        strLocal.set(Thread.currentThread().getName());
    }

    public Long getLong() {
        return longLocal.get();
    }

    public String getStr() {
        return strLocal.get();
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadLocalsTest test = new ThreadLocalsTest();
        test.set();
        System.out.println(test.getLong());
        System.out.println(test.getStr());

        Thread thread = new Thread(() -> {
           test.set();
            System.out.println(test.getLong());
            System.out.println(test.getStr());
        });

        thread.start();
        thread.join();
    }
}
```

- 输出结果：

```control
1
main
11
Thread-0
```

- 从这段代码的输出结果可以看出，在main线程中和thread1线程中，longLocal保存的副本值和strLocal保存的副本值都不一样。最后一次在main线程再次打印副本值是为了证明在main线程中和thread1线程中的副本值确实是不同的。

##### 总结一下：

1. 实际的通过ThreadLocal创建的副本是存储在每个线程自己的threadLocals中的；
2. 为何threadLocals的类型ThreadLocalMap的键值为ThreadLocal对象，因为每个线程中可有多个threadLocal变量，就像上面代码中的longLocal和stringLocal；
3. 在进行get之前，必须先set，否则默认会返回null；
   - **如果想在get之前不需要调用set就能正常访问的话，必须重写initialValue()方法。**
   - 因为在上面的代码分析过程中，我们发现如果没有先set的话，即在map中查找不到对应的存储，则会通过调用setInitialValue方法返回，而在setInitialValue方法中，有一个语句是`T value = initialValue()`， 而默认情况下，initialValue方法返回的是null。

### ThreadLocal的应用场景

- 最常见的ThreadLocal使用场景为 用来解决 数据库连接、Session管理等。

```Java
private static ThreadLocal<Connection> connectionHolder
= new ThreadLocal<Connection>() {
public Connection initialValue() {
    return DriverManager.getConnection(DB_URL);
}
};
 
public static Connection getConnection() {
return connectionHolder.get();
}
```

```Java
private static final ThreadLocal threadSession = new ThreadLocal();
 
public static Session getSession() throws InfrastructureException {
    Session s = (Session) threadSession.get();
    try {
        if (s == null) {
            s = getSessionFactory().openSession();
            threadSession.set(s);
        }
    } catch (HibernateException ex) {
        throw new InfrastructureException(ex);
    }
    return s;
}
```

- ThreadLocal不是用来解决对象共享访问问题的，而主要是提供了线程保持对象的方法和避免参数传递的方便的对象访问方式。
- 其优势是线程安全，使用ThreadLocal可以防止其他线程修改对象里的内容。