# List接口、Set接口和Map接口

### List和Set接口自Collection接口，而Map不是继承的Collection接口

- Collection表示一组对象,这些对象也称为collection的元素;
  - 一些 collection允许有重复的元素,而另一些则不允许;
  - 一些collection是有序的,而另一些则是无序的;
  - JDK中不提供此接口的任何直接实现,它提供更具体的子接口(如Set 和List)实现;
- Map没有继承Collection接口,Map提供key到value的映射;
  - 一个Map中不能包含相同key,每个key只能映射一个value;
  - Map接口提供3种集合的视图,Map的内容可以被当做一组key集合,一组value集合,或者一组key-value映射;

### List接口

- **元素有放入顺序，元素可重复**。
- List接口有三个实现类：LinkedList，ArrayList，Vector。
  1. **LinkedList**：
     - 底层基于双向链表实现，链表内存是散乱的，每一个元素存储本身内存地址的同时还存储下一个元素的地址。链表增删快，查找慢。
  2. **ArrayList和Vector的区别（都是基于数组实现）**：
     - ArrayList是非线程安全的，效率高；Vector是基于线程安全的，效率低。
- List是一种有序的Collection，可以通过索引访问集合中的数据,List比Collection多了10个方法，主要是有关索引的方法。
  1. 所有的索引返回的方法都有可能抛出一个IndexOutOfBoundsException异常。
  2. `.subList(int fromIndex, int toIndex)`返回的是包括fromIndex，不包括toIndex的视图，该列表的size()=toIndex-fromIndex。
- 基于Array的List（Vector，ArrayList）适合查询，而LinkedList（链表）适合添加，删除操作。

### Set接口

- **元素无放入顺序，元素不可重复**（注意：元素虽然无放入顺序，但是元素在set中的位置是有该元素的HashCode决定的，其位置其实是固定的）
- Set接口有三个实现类：HashSet(底层由HashMap实现)，LinkedHashSet，TreeSet（底层由平衡二叉树实现）。
- SortedSet接口有一个实现类：TreeSet（底层由平衡二叉树实现）。
- Set : 存入Set的每个元素都必须是唯一的，因为Set不保存重复元素。加入Set的元素必须定义equals()方法以确保对象的唯一性。Set与Collection有完全一样的接口。
- HashSet : 为快速查找设计的Set。存入HashSet的对象必须定义hashCode()。
- TreeSet : 保存次序的Set, 底层为树结构。使用它可以从Set中提取有序的序列。
- LinkedHashSet : 
  - 具有HashSet的查询速度，且内部使用链表维护元素的顺序(插入的次序)。
  - 于是在使用迭代器遍历Set时，结果会按元素插入的次序显示。

### Map接口

- **以键值对的方式出现的。**
- Map接口有三个实现类：HashMap，HashTable，LinkeHashMap。
- HashMap非线程安全，高效，支持null；
- HashTable线程安全，低效，不支持null；
- SortedMap有一个实现类：TreeMap。