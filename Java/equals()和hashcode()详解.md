# [equals()与hashCode()方法详解](http://www.cnblogs.com/Qian123/p/5703507.html)

### equals()方法详解

- Object类是类继承结构的基础，所以是每一个类的父类。所有的对象，包括数组，都实现了在`Object`类中定义的方法。
-  equals()方法在object类中定义如下：

```Java
public boolean equals(Object obj) {  
    return (this == obj);  
} 
```

- 很明显是对两个对象的地址值进行的比较（即比较引用是否相同）。但是我们知道，String 、Math、Integer、Double等这些封装类在使用equals()方法时，已经覆盖了object类的equals()方法。
- 它的性质有：
  - **自反性**。
    - 对于任意不为null的引用值x，`x.equals(x)`一定是true。
  - **对称性**。
    - 对于任意不为null的引用值x和y，当且仅当`x.equals(y)`是true时，`y.equals(x)`也是true。
  - **传递性**。
    - 对于任意不为null的引用值x、y和z，如果`x.equals(y)`是true，同时`y.equals(z)`是true，那么`x.equals(z)`一定是true。
  - **一致性**。
    - 对于任意不为null的引用值x和y，如果用于equals比较的对象信息没有被修改的话，多次调用时`x.equals(y)`要么一致地返回true要么一致地返回false。
- 对于Object类来说，`equals()`方法在对象上实现的是差别可能性最大的等价关系，即，对于任意非null的引用值x和y，当且仅当x和y引用的是同一个对象，该方法才会返回true。
- **需要注意的是当equals()方法被override时，hashCode()也要被override。按照一般hashCode()方法的实现来说，相等的对象，它们的hashcode一定相等。**

### hashcode() 方法详解

- `hashCode()`方法给对象返回一个hash code值。这个方法被用于hash tables，例如HashMap。
- 它的性质是：
  - 在一个Java应用的执行期间，如果一个对象提供给equals做比较的信息没有被修改的话，该对象多次调用`hashCode()`方法，该方法必须始终如一返回同一个integer。
  - 如果两个对象根据`equals(Object)`方法是相等的，那么调用二者各自的`hashCode()`方法必须产生同一个integer结果。
  - 并不要求根据`equals(java.lang.Object)`方法不相等的两个对象，调用二者各自的`hashCode()`方法必须产生不同的integer结果。然而，程序员应该意识到对于不同的对象产生不同的integer结果，有可能会提高hash table的性能。
- 大量的实践表明，由Object类定义的`hashCode()`方法对于不同的对象返回不同的integer。
- 在object类中，hashCode定义如下：

```java
public native int hashCode();
```

-  说明是一个本地方法，它的实现是根据本地机器相关的。当然我们可以在自己写的类中覆盖hashcode()方法，比如String、Integer、Double等这些类都是覆盖了hashcode()方法的。

###  Hashset、Hashmap、Hashtable与hashcode()和equals()的密切关系

- 在java的集合中，判断两个对象是否相等的规则是：

  1. 判断两个对象的hashCode是否相等：

     - 如果不相等，认为两个对象也不相等，完毕。
     -  如果相等，转入2。

     （这一点只是为了提高存储效率而要求的，其实理论上没有也可以，但如果没有，实际使用时效率会大大降低，所以我们这里将其做为必需的。）

  2. 判断两个对象用equals运算是否相等：

     - 如果不相等，认为两个对象也不相等。
     -  如果相等，认为两个对象相等（equals()是判断两个对象是否相等的关键）。

- 因此：

  - **如果覆盖了`equals()`方法却没有覆盖`hashcode()`方法，将会导致在集合出现相同的对象。**
  - 因为它们的`hashcode()`返回的hashcode不一样，集合认为两个对象也不相等，即使调用`equals()`会分返回true。

### 注意

- Java对象的eqauls方法和hashCode方法是这样规定的：
  1. 相等（相同）的对象必须具有相等的哈希码（或者散列码）。
  2. 如果两个对象的hashCode相同，它们并不一定相同。