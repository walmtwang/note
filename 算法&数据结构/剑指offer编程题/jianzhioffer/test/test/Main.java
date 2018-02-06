package test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by walmt on 2018/2/6.
 */
public class Main {

    private Class aClass = new Class(1);
    public static Main main = new Main();

    public static void main(String[] args) {
        /*Set<Class> set = new HashSet<>();
        set.add(new Class(1));
        set.add(new Class(1));
        System.out.println(set);*/
        new Thread(() -> System.out.println(main.get())).start();
        new Thread(() -> main.set(new Class(2))).start();
    }

    public synchronized Class get() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return aClass;
    }

    public synchronized void set(Class aClass) {
        this.aClass = aClass;
        System.out.println("set " + aClass);
    }
}
