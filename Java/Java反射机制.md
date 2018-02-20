# [Java反射机制](http://blog.csdn.net/zjf280441589/article/details/50453776)

### JAVA反射机制

- 指的是可以于运行时**加载，探知和使用**编译期间完全未知的类。
- 程序在运行状态中，可以动态加载一个只有名称的类，对于任意一个已经加载的类,都能够知道这个类的所有属性和方法；对于任意一个对象,都能调用他的任意一个方法和属性；
- 加载完类之后, 在堆内存中会产生一个Class类型的对象（一个类只有一个Class对象）， 这个对象包含了完整的类的结构信息，而且这个Class对象就像一面镜子，透过这个镜子看到类的结构，所以被称之为：**反射**。
- 每个类被加载进入内存之后,系统就会为该类生成一个对应的java.lang.Class对象,通过该Class对象就可以访问到JVM中的这个类。

### 主要提供的功能

- 在运行时判断任意一个对象所属的类；
- 在运行时构造任意一个类的对象；
- 在运行时判断任意一个类所具有的成员变量和方法；
- 在运行时调用任意一个对象的方法；
- 生成动态代理。

### Class对象

- Java有个Object 类，是所有Java 类的继承根源，其内声明了数个应该在所有Java 类中被改写的方法：hashCode()、equals()、clone()、toString()、getClass()等。其中getClass()返回一个Class 对象。
  - Class 类十分特殊。
  - 它和一般类一样继承自Object，其实体用以表达Java程序运行时的classes和interfaces，也用来表达enum、array、primitive Java types(boolean, byte, char, short, int, long, float, double)以及关键词void。
  - 当一个class被加载，或当加载器（class loader）的defineClass()被JVM调用，JVM 便自动产生一个Class 对象。
- 如果您想借由“修改Java标准库源码”来观察Class对象的实际生成时机（例如在Class的constructor内添加一个println()），这样是行不通的！因为Class并没有public constructor。
- 针对任何您想探勘的类，唯有先为它产生一个Class 对象，接下来才能经由后者唤起为数十多个的Reflection APIs。
- Reflection机制允许程序在正在执行的过程中，利用Reflection APIs取得任何已知名称的类的内部信息，包括：package、 type parameters、 superclass、 implemented interfaces、 inner classes、 outer classes、 fields、 constructors、 methods、 modifiers等，并可以在执行的过程中，动态生成instances、变更fields内容或唤起methods。

### Class对象的获取

- 对象的`getClass()`方法；
- 类的`.class`(最安全/性能最好)属性；
- 运用`Class.forName(String className)`动态加载类，`className`需要是类的全限定名（最常用）。

| 获取Class对象的方法            | 示例                                       |
| ----------------------- | ---------------------------------------- |
| getClass()  （每个对象都有此函数） | String str = "abc"; Class c1 = str.getClass(); |
| Class.getSuperClass()   | Button b = new Button(); Class c1 = b.getClass(); Class c2 = c1.getSuperClass(); |
| 静态方法Class.forName()     | Class c1 = Class.forName("java.lang.String"); |
| .class属性                | Class c1 = String.class;                 |
| 基本类型包装类的TYPE语法          | Class c1 = Boolean.TYPE; Class c2 = Byte.TYPE; Class c3 = Character.TYPE; Class c4 = Short.TYPE; Class c5 = Integer.TYPE; Class c6 = Long.TYPE; Class c7 = Float.TYPE; Class c8 = Double.TYPE; Class c9 = Void.TYPE; |

### 从Class中获取信息

- Class类提供了大量的实例方法来获取该Class对象所对应的详细信息，Class类大致包含如下方法，其中每个方法都包含多个重载版本，因此我们只是做简单的介绍，详细请参考[JDK文档](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)。

| 获取内容            | 方法签名                                     |
| --------------- | ---------------------------------------- |
| 构造器             | `Constructor<T> getConstructor(Class<?>... parameterTypes)` |
| 包含的方法           | `Method getMethod(String name, Class<?>... parameterTypes)` |
| 包含的属性           | `Field getField(String name)`            |
| 包含的`Annotation` | `<A extends Annotation> A getAnnotation(Class<A> annotationClass)` |
| 内部类             | `Class<?>[] getDeclaredClasses()`        |
| 外部类             | `Class<?> getDeclaringClass()`           |
| 所实现的接口          | `Class<?>[] getInterfaces()`             |
| 修饰符             | `int getModifiers()`                     |
| 所在包             | `Package getPackage()`                   |
| 类名              | `String getName()`                       |
| 简称              | `String getSimpleName()`                 |

- 一些判断类本身信息的。

| 判断内容                | 方法签名                                     |
| ------------------- | ---------------------------------------- |
| 注解类型?               | `boolean isAnnotation()`                 |
| 使用了该`Annotation`修饰? | `boolean isAnnotationPresent(Class<? extends Annotation> annotationClass)` |
| 匿名类?                | `boolean isAnonymousClass()`             |
| 数组?                 | `boolean isArray()`                      |
| 枚举?                 | `boolean isEnum()`                       |
| 原始类型?               | `boolean isPrimitive()`                  |
| 接口?                 | `boolean isInterface()`                  |
| `obj`是否是该`Class`的实例 | `boolean isInstance(Object obj)`         |

- 使用反射生成并操作对象：

> `Method` `Constructor` `Field`这些类都实现了`java.lang.reflect.Member`接口，程序可以通过`Method`对象来执行相应的方法，通过`Constructor`对象来调用对应的构造器创建实例,通过`Filed`对象直接访问和修改对象的成员变量值。

### 创建对象

- 通过反射来生成对象的方式有两种：
  - 使用Class对象的`newInstance()`方法来创建该Class对象对应类的实例(这种方式要求该Class对象的对应类有默认构造器)。
  - 先使用Class对象获取指定的Constructor对象，再调用Constructor对象的`newInstance()`方法来创建该Class对象对应类的实例(通过这种方式可以选择指定的构造器来创建实例)。

- 通过第一种方式来创建对象比较常见，像Spring这种框架都需要根据配置文件(如applicationContext.xml)信息来创建Java对象，从配置文件中读取的只是某个类的全限定名字符串，程序需要根据该字符串来创建对应的实例，就必须使用默认的构造器来反射对象。

- 下面我们就模拟Spring实现一个简单的对象池，该对象池会根据文件读取key-value对，然后创建这些对象， 并放入Map中。

- 配置文件：

  ```json
  {
    "objects": [
      {
        "id": "id1",
        "class": "cn.walmt.bean.User"
      },
      {
        "id": "id2",
        "class": "cn.walmt.bean.Bean"
      }
    ]
  }
  ```

- ObjectPool：

  ```
  public class ObjectPool {

      private Map<String, Object> pool;

      private ObjectPool(Map<String, Object> pool) {
          this.pool = pool;
      }

      private static Object getInstance(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
          return Class.forName(className).newInstance();
      }

      private static JSONArray getObjects(String config) throws IOException {
          Reader reader = new InputStreamReader(ClassLoader.getSystemResourceAsStream(config));
          return JSONObject.parseObject(CharStreams.toString(reader)).getJSONArray("objects");
      }

      public static ObjectPool init(String config) {
          try {
              JSONArray objects = getObjects(config);
              ObjectPool pool = new ObjectPool(new HashMap<>());
              if (objects != null && objects.size() != 0) {
                  for (int i = 0; i < objects.size(); i++) {
                      JSONObject object = objects.getJSONObject(i);
                      if (object == null || object.size() == 0) {
                          continue;
                      }
                      String id = object.getString("id");
                      String className = object.getString("class");

                      pool.putObject(id, getInstance(className));
                  }
              }
              return pool;
          } catch (IOException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
              throw new RuntimeException(e);
          }
      }

      public Object getObject(String id) {
          return pool.get(id);
      }

      public void putObject(String id, Object object) {
          pool.put(id, object);
      }

      public void clear() {
          pool.clear();
      }
  }
  ```

- Bean：

  ```Java
  public class Bean {

      private Boolean usefull;
      private BigDecimal rate;
      private String name;

      public Boolean getUsefull() {
          return usefull;
      }

      public void setUsefull(Boolean usefull) {
          this.usefull = usefull;
      }

      public BigDecimal getRate() {
          return rate;
      }

      public void setRate(BigDecimal rate) {
          this.rate = rate;
      }

      public String getName() {
          return name;
      }

      public void setName(String name) {
          this.name = name;
      }

      @Override
      public String toString() {
          return "Bean{" +
                  "usefull=" + usefull +
                  ", rate=" + rate +
                  ", name='" + name + '\'' +
                  '}';
      }
  }

  ```

- User：

  ```Java
  public class User {

      private Integer id;
      private String name;
      private String password;

      public Integer getId() {
          return id;
      }

      public void setId(Integer id) {
          this.id = id;
      }

      public String getName() {
          return name;
      }

      public void setName(String name) {
          this.name = name;
      }

      public String getPassword() {
          return password;
      }

      public void setPassword(String password) {
          this.password = password;
      }

      @Override
      public String toString() {
          return "User{" +
                  "id=" + id +
                  ", name='" + name + '\'' +
                  ", password='" + password + '\'' +
                  '}';
      }
  }
  ```

- Test：

  ```Java
  public static void main(String[] args) {
      ObjectPool pool = ObjectPool.init("object.json");
      User user = (User) pool.getObject("id1");
      System.out.println(user);
      Bean bean = (Bean) pool.getObject("id2");
      System.out.println(bean);
  }
  ```

- pom.xml

  ```xml
  <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>fastjson</artifactId>
      <version>1.2.7</version>
  </dependency>

  <dependency>
      <groupId>com.google.guava</groupId>
      <artifactId>guava</artifactId>
      <version>18.0</version>
  </dependency>
  ```

### 调用方法

- 当获取到某个类对应的Class对象之后，就可以通过该Class对象的`getMethod()`来获取一个Method数组或Method对象。
- 每个Method对象对应一个方法，在获得Method对象之后，就可以通过调用invoke方法来调用该Method对象对应的方法。；

```Java
@CallerSensitive
public Object invoke(Object obj, Object... args)
    throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    ...
}
```

- 下面我们对上面的对象池加强：可以看到Client获取到的对象的成员变量全都是默认值，既然我们学习了**动态调用对象的方法**,那么我们就通过配置文件来给对象设置值（在对象创建时），新的配置文件形式如下：

- 新配置文件：

  ```json
  {
    "objects": [
      {
        "id": "id1",
        "class": "com.fq.domain.User",
        "fields": [
          {
            "name": "id",
            "value": 101
          },
          {
            "name": "name",
            "value": "feiqing"
          },
          {
            "name": "password",
            "value": "ICy5YqxZB1uWSwcVLSNLcA=="
          }
        ]
      },
      {
        "id": "id2",
        "class": "com.fq.domain.Bean",
        "fields": [
          {
            "name": "usefull",
            "value": true
          },
          {
            "name": "rate",
            "value": 3.14
          },
          {
            "name": "name",
            "value": "bean-name"
          }
        ]
      },
      {
        "id": "id3",
        "class": "com.fq.domain.ComplexBean",
        "fields": [
          {
            "name": "name",
            "value": "complex-bean-name"
          },
          {
            "name": "refBean",
            "ref": "id2"
          }
        ]
      }
    ]
  }
  ```

- 其中fields代表该Bean所包含的属性， name为属性名称， value为属性值（属性类型为JSON支持的类型），ref代表引用一个对象（也就是属性类型为Object,但是一定要引用一个已经存在了的对象）。

  ```Java
  public class ObjectPool {

      private Map<String, Object> pool;

      private volatile static ObjectPool OBJECTPOOL;

      // 创建一个对象池的实例(保证是多线程安全的)
      private static void initSingletonPool() {
          if (OBJECTPOOL == null) {
              synchronized (ObjectPool.class) {
                  if (OBJECTPOOL == null) {
                      OBJECTPOOL = new ObjectPool(new ConcurrentHashMap<>());
                  }
              }
          }
      }

      private ObjectPool(Map<String, Object> pool) {
          this.pool = pool;
      }

      private static JSONArray getObjects(String config) throws IOException {
          Reader reader = new InputStreamReader(ClassLoader.getSystemResourceAsStream(config));
          return JSONObject.parseObject(CharStreams.toString(reader)).getJSONArray("objects");
      }

      private static Object getInstance(String className, JSONArray fields) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
          // 配置的Class
          Class clzz = Class.forName(className);
          // 目标Class的实例对象
          Object targetObject = clzz.newInstance();
          if (fields != null && fields.size() != 0) {
              for (int i = 0; i < fields.size(); i++) {
                  JSONObject field = fields.getJSONObject(i);
                  // 需要设置的成员变量名
                  String fieldName = field.getString("name");

                  // 需要设置的成员变量的值
                  Object fieldValue;
                  if (field.containsKey("value")) {
                      fieldValue = field.get("value");
                  } else if (field.containsKey("ref")) {
                      String refBeanId = field.getString("ref");
                      fieldValue = OBJECTPOOL.getObject(refBeanId);
                  } else {
                      throw new RuntimeException("neither value nor ref");
                  }

                  String setterName = "set" +
                          fieldName.substring(0, 1).toUpperCase() +
                          fieldName.substring(1);
                  // 需要设置的成员变量的setter方法
                  Method setterMethod = clzz.getMethod(setterName, fieldValue.getClass());
                  // 调用setter方法将值设置进去
                  setterMethod.invoke(targetObject, fieldValue);
              }
          }
          return targetObject;
      }

      // 根据指定的JSON配置文件来初始化对象池
      public static ObjectPool init(String config) {
          initSingletonPool();
          try {
              JSONArray objects = getObjects(config);
              if (objects != null && objects.size() != 0) {
                  for (int i = 0; i < objects.size(); i++) {
                      JSONObject object = objects.getJSONObject(i);
                      if (object == null || object.size() == 0) {
                          continue;
                      }
                      String id = object.getString("id");
                      String className = object.getString("class");
                      JSONArray fields = object.getJSONArray("fields");

                      OBJECTPOOL.putObject(id, getInstance(className, fields));
                  }
              }
              return OBJECTPOOL;
          } catch (IOException | IllegalAccessException
                  | InstantiationException | ClassNotFoundException
                  | NoSuchMethodException | InvocationTargetException e) {
              throw new RuntimeException(e);
          }
      }

      public Object getObject(String id) {
          return pool.get(id);
      }

      public void putObject(String id, Object object) {
          pool.put(id, object);
      }

      public void clear() {
          pool.clear();
      }
  }

  ```

- Test：

  ```Java
  public static void main(String[] args) {
      ObjectPool pool = ObjectPool.init("object2.json");
      User user = (User) pool.getObject("id1");
      System.out.println(user);
      Bean bean = (Bean) pool.getObject("id2");
      System.out.println(bean);
      ComplexBean complexBean = (ComplexBean) pool.getObject("id3");
      System.out.println(complexBean);
  }
  ```

- ComplexBean：

  ```Java
  public class ComplexBean {

      private String name;
      private Bean refBean;

      public String getName() {
          return name;
      }

      public void setName(String name) {
          this.name = name;
      }

      public Bean getRefBean() {
          return refBean;
      }

      public void setRefBean(Bean refBean) {
          this.refBean = refBean;
      }

      @Override
      public String toString() {
          return "ComplexBean{" +
                  "name='" + name + '\'' +
                  ", refBean=" + refBean +
                  '}';
      }
  }
  ```

- Spring框架就是通过这种方式将成员变量值以及依赖对象等都放在配置文件中进行管理的，从而实现了较好地解耦(不过Spring是通过XML作为配置文件)。

### 访问成员变量

- 通过Class对象的的`getField()`方法可以获取该类所包含的全部或指定的成员变量Field，Filed提供了如下两组方法来读取和设置成员变量值。

  - `getXxx(Object obj)`：获取obj对象的该成员变量的值，此处的Xxx对应8中基本类型,如果该成员变量的类型是引用类型，则取消get后面的Xxx；
  - `setXxx(Object obj, Xxx val)`：将obj对象的该成员变量值设置成val值。此处的Xxx对应8种基本类型，如果该成员类型是引用类型，则取消set后面的Xxx；

- 注：getDeclaredXxx方法可以获取所有的成员变量，无论private/public；

  ```Java
  public class Client {
      public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
          User user = new User();
          Field idField = User.class.getDeclaredField("id");
          setAccessible(idField);
          idField.set(user, 46);

          Field nameField = User.class.getDeclaredField("name");
          setAccessible(nameField);
          nameField.set(user, "feiqing");

          Field passwordField = User.class.getDeclaredField("password");
          setAccessible(passwordField);
          passwordField.set(user, "ICy5YqxZB1uWSwcVLSNLcA==");

          System.out.println(user);
      }

      public static void setAccessible(AccessibleObject object) {
          object.setAccessible(true);
      }
  }
  ```

### 使用反射获取泛型信息

- 为了通过反射操作泛型以迎合实际开发的需要，Java新增了`java.lang.reflect.ParameterizedType` `java.lang.reflect.GenericArrayType` `java.lang.reflect.TypeVariable` `java.lang.reflect.WildcardType`几种类型来代表不能归一到Class类型但是又和原始类型同样重要的类型。

| 类型                | 含义                                       |
| ----------------- | ---------------------------------------- |
| ParameterizedType | 一种参数化类型, 比如`Collection<String>`          |
| GenericArrayType  | 一种元素类型是参数化类型或者类型变量的数组类型                  |
| TypeVariable      | 各种类型变量的公共接口                              |
| WildcardType      | 一种通配符类型表达式, 如`?` `? extends Number` `? super Integer` |

- 其中，我们可以使用`ParameterizedType`来获取泛型信息。

```Java
public class Client {

    private Map<String, Object> objectMap;

    public void test(Map<String, Object> map, String string) {
    }

    public Map<User, Bean> test() {
        return null;
    }

    /**
     * 测试属性类型
     *
     * @throws NoSuchFieldException
     */
    public void testFieldType() throws NoSuchFieldException {
        Field field = Client.class.getDeclaredField("objectMap");
        Type gType = field.getGenericType();
        // 打印type与generic type的区别
        System.out.println(field.getType());
        System.out.println(gType);
        System.out.println("---------------");
        if (gType instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) gType;
            Type[] types = pType.getActualTypeArguments();
            for (Type type : types) {
                System.out.println(type.toString());
            }
        }
    }

    /**
     * 测试参数类型
     *
     * @throws NoSuchMethodException
     */
    public void testParamType() throws NoSuchMethodException {
        Method testMethod = Client.class.getMethod("test", Map.class, String.class);
        Type[] parameterTypes = testMethod.getGenericParameterTypes();
        for (Type type : parameterTypes) {
            System.out.println("type ->" + type);
            if (type instanceof ParameterizedType) {
                Type[] actualTypes = ((ParameterizedType) type).getActualTypeArguments();
                for (Type actualType : actualTypes) {
                    System.out.println("\tactual type ->" + actualType);

                }
            }
        }
    }

    /**
     * 测试返回值类型
     *
     * @throws NoSuchMethodException
     */
    public void testReturnType() throws NoSuchMethodException {
        Method testMethod = Client.class.getMethod("test");
        Type returnType = testMethod.getGenericReturnType();
        System.out.println("return type ->" + returnType);

        if (returnType instanceof ParameterizedType) {
            Type[] actualTypes = ((ParameterizedType) returnType).getActualTypeArguments();
            for (Type actualType : actualTypes) {
                System.out.println("\tactual type ->" + actualType);
            }
        }
    }

    public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException {
        Client client = new Client();
        client.testFieldType();
        client.testParamType();
        client.testReturnType();
    }
}
```

- 结果：

```control
interface java.util.Map
java.util.Map<java.lang.String, java.lang.Object>
---------------
class java.lang.String
class java.lang.Object
type ->java.util.Map<java.lang.String, java.lang.Object>
	actual type ->class java.lang.String
	actual type ->class java.lang.Object
type ->class java.lang.String
return type ->java.util.Map<cn.walmt.bean.User, cn.walmt.bean.Bean>
	actual type ->class cn.walmt.bean.User
	actual type ->class cn.walmt.bean.Bean
```

### [使用反射获取注解](http://blog.csdn.net/zjf280441589/article/details/50444343)

### 反射性能测试

- `Method/Constructor/Field/Element`都继承了`AccessibleObject`,`AccessibleObject`类中有一个`setAccessible`方法：

  ```Java
  public void setAccessible(boolean flag) throws SecurityException {
      ...
  }
  ```

- 该方法有两个作用: 

  1. **启用/禁用**访问安全检查开关：
     - 值为true，则指示反射的对象在使用时取消Java语言访问检查；
     - 值为false，则指示应该实施Java语言的访问检查。（**默认**）
  2. 可以禁止安全检查，提高反射的运行效率。

```Java
public class TestReflect {

    public void testNoneReflect() {
        User user = new User();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            user.setName(String.valueOf(i));
        }
        long count = System.currentTimeMillis() - start;
        System.out.println("没有反射, 共消耗 <" + count + "> 毫秒");
    }

    public void testNotAccess() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        User user = new User();
        Method method = Class.forName("cn.walmt.bean.User").getDeclaredMethod("setName", String.class);
        method.setAccessible(true);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            method.invoke(user, String.valueOf(i));
        }
        long count = System.currentTimeMillis() - start;
        System.out.println("没有访问权限, 共消耗 <" + count + "> 毫秒");
    }

    public void testUseAccess() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        User user = new User();
        Method method = Class.forName("cn.walmt.bean.User").getDeclaredMethod("setName", String.class);
        method.setAccessible(false);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            method.invoke(user, String.valueOf(i));
        }
        long count = System.currentTimeMillis() - start;
        System.out.println("有访问权限, 共消耗 <" + count + "> 毫秒");
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        TestReflect testReflect = new TestReflect();
        testReflect.testNoneReflect();
        testReflect.testNotAccess();
        testReflect.testUseAccess();
    }
}
```
