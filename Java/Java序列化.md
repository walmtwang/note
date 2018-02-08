# [Java序列化与反序列化](http://blog.csdn.net/wangloveall/article/details/7992448)

### Java序列化与反序列化

-  **Java序列化**是指把Java对象转换为字节序列的过程；而**Java反序列化**是指把字节序列恢复为Java对象的过程。

### 为什么需要序列化与反序列化

- 实现Java进程通信间的对象传递。
- 发送方把Java对象转换为字节序列，然后在网络上传送；另一方面，接收方从字节序列中恢复出Java对象。

### 如何实现Java序列化与反序列化

1. JDK类库中序列化API

   -  **java.io.ObjectOutputStream：表示对象输出流**
     - 它的writeObject(Object obj)方法可以对参数指定的obj对象进行序列化，把得到的字节序列写到一个目标输出流中。
   - **java.io.ObjectInputStream：表示对象输入流**
     - 它的readObject()方法可以从源输入流中读取字节序列，再把它们反序列化成为一个对象，并将其返回。

2. 实现序列化的要求

   - 只有实现了Serializable或Externalizable接口的类的对象才能被序列化，否则抛出异常。

3. 实现Java对象序列化与反序列化的方法

   - 假定一个Student类，它的对象需要序列化，可以有如下三种方法：

   1. 若Student类仅仅实现了**Serializable接口**，则可以按照以下方式进行序列化和反序列化。
      - ObjectOutputStream采用默认的序列化方式，对Student对象的非transient的实例变量进行序列化。
      - ObjcetInputStream采用默认的反序列化方式，对Student对象的非transient的实例变量进行反序列化。
   2. 若Student类不仅实现了Serializable接口，并且还定义了readObject(ObjectInputStream in)和writeObject(ObjectOutputSteam out)，则采用以下方式进行序列化与反序列化。
      - ObjectOutputStream调用Student对象的writeObject(ObjectOutputStream out)的方法进行序列化。
      - ObjectInputStream会调用Student对象的readObject(ObjectInputStream in)的方法进行反序列化。
   3. 若Student类实现了**Externalnalizable接口**，且Student类必须实现readExternal(ObjectInput in)和writeExternal(ObjectOutput out)方法，则按照以下方式进行序列化与反序列化。
      - ObjectOutputStream调用Student对象的writeExternal(ObjectOutput out))的方法进行序列化。
      - ObjectInputStream会调用Student对象的readExternal(ObjectInput in)的方法进行反序列化。

4. JDK类库中序列化的步骤

   1. 创建一个对象输出流，它可以包装一个其它类型的目标输出流，如文件输出流：`ObjectOutputStream out = new ObjectOutputStream(new fileOutputStream(“D:\\objectfile.obj”));`。

   2. 通过对象输出流的writeObject()方法写对象：

      ```Java
      out.writeObject(“Hello”);
      out.writeObject(new Date());
      ```

5. JDK类库中反序列化的步骤

   1. 创建一个对象输入流，它可以包装一个其它类型输入流，如文件输入流：`ObjectInputStream in = new ObjectInputStream(new fileInputStream(“D:\\objectfile.obj”));`。

   2. 通过对象输出流的readObject()方法读取对象：

      ```Java
      String obj1 = (String)in.readObject();
      Date obj2 = (Date)in.readObject();
      ```

- 说明：为了正确读取数据，完成反序列化，必须保证向对象输出流写对象的顺序与从对象输入流中读对象的顺序一致。

### [序列化的注意事项](http://blog.csdn.net/chenleixing/article/details/43833805)

1. 如果子类实现Serializable接口而父类未实现时，父类不会被序列化，但此时父类必须有个无参构造方法，否则会抛InvalidClassException异常。
2. 静态变量不会被序列化，那是类的“菜”，不是对象的。
3. transient关键字修饰变量可以限制序列化。
4. 虚拟机是否允许反序列化，不仅取决于类路径和功能代码是否一致，一个非常重要的一点是两个类的序列化 ID 是否一致，就是 private static final long serialVersionUID = 1L。
5. Java 序列化机制为了节省磁盘空间，具有特定的存储规则，当写入文件的为同一对象时，并不会再将对象的内容进行存储，而只是再次存储一份引用。反序列化时，恢复引用关系。
6. 序列化到同一个文件时，如第二次修改了相同对象属性值再次保存时候，虚拟机根据引用关系知道已经有一个相同对象已经写入文件，因此只保存第二次写的引用，所以读取时，都是第一次保存的对象。读者在使用一个文件多次 writeObject 需要特别注意这个问题(基于第5点)。