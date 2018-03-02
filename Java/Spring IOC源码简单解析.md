# [Spring IOC源码解析](http://www.cnblogs.com/ITtangtang/p/3978349.html)

## 什么是Ioc/DI

- IoC 容器：最主要是完成了完成对象的创建和依赖的管理注入等等。
- 控制反转（IoC），就是把原先我们代码里面需要实现的对象创建、依赖的代码，反转给容器来帮忙实现。那么必然的我们需要创建一个容器，同时需要一种描述来让容器知道需要创建的对象与对象的关系。这个描述最具体表现就是我们可配置的文件。
- 对象和对象关系可以用 xml ， properties 文件等语义化配置文件表示。
- 描述对象关系的文件可能是存放在 classpath ， filesystem ，或者是 URL 网络资源， servletContext 等。
- 有了配置文件，还需要对配置文件解析。
  - 不同的配置文件对对象的描述不一样，如标准的，自定义声明式的，如何统一？ 
  - 在内部需要有一个统一的关于对象的定义，所有外部的描述都必须转化成统一的描述定义。
  - 如何对不同的配置文件进行解析？需要对不同的配置文件语法，采用不同的解析器。

## Spring IOC体系结构

### **BeanFactory**

- Spring Bean的创建是典型的工厂模式，这一系列的Bean工厂，也即IOC容器为开发者管理对象间的依赖关系提供了很多便利和基础服务，在Spring中有许多的IOC容器的实现供用户选择和使用，其相互关系如下：

  ![](img\1.x-png)

- 其中BeanFactory作为最顶层的一个接口类，它定义了IOC容器的基本功能规范，BeanFactory 有三个子类：ListableBeanFactory、HierarchicalBeanFactory 和AutowireCapableBeanFactory。

  - 但是从上图中我们可以发现最终的默认实现类是 DefaultListableBeanFactory，他实现了所有的接口。那为何要定义这么多层次的接口呢？
  - 查阅这些接口的源码和说明发现，每个接口都有他使用的场合，它主要是为了区分在 Spring 内部在操作过程中对象的传递和转化过程中，对对象的数据访问所做的限制。
    - 例如 ListableBeanFactory 接口表示这些 Bean 是可列表的。
    - 而 HierarchicalBeanFactory 表示的是这些 Bean 是有继承关系的，也就是每个Bean 有可能有父 Bean。
    - AutowireCapableBeanFactory 接口定义 Bean 的自动装配规则。这四个接口共同定义了 Bean 的集合、Bean 之间的关系、以及 Bean 行为。

- 最基本的IOC容器接口BeanFactory：

```java
public interface BeanFactory {    
     
     //对FactoryBean的转义定义，因为如果使用bean的名字检索FactoryBean得到的对象是工厂生成的对象，    
     //如果需要得到工厂本身，需要转义           
     String FACTORY_BEAN_PREFIX = "&"; 
        
     //根据bean的名字，获取在IOC容器中得到bean实例    
     Object getBean(String name) throws BeansException;    
   
    //根据bean的名字和Class类型来得到bean实例，增加了类型安全验证机制。    
     Object getBean(String name, Class requiredType) throws BeansException;    
    
    //提供对bean的检索，看看是否在IOC容器有这个名字的bean    
     boolean containsBean(String name);    
    
    //根据bean名字得到bean实例，并同时判断这个bean是不是单例    
    boolean isSingleton(String name) throws NoSuchBeanDefinitionException;    
    
    //得到bean实例的Class类型    
    Class getType(String name) throws NoSuchBeanDefinitionException;    
    
    //得到bean的别名，如果根据别名检索，那么其原名也会被检索出来    
   String[] getAliases(String name);    
    
 }
```

- 在BeanFactory里只对IOC容器的基本行为作了定义，根本不关心你的bean是如何定义怎样加载的。正如我们只关心工厂里得到什么的产品对象，至于工厂是怎么生产这些对象的，这个基本的接口不关心。
- 而要知道工厂是如何产生对象的，我们需要看具体的IOC容器实现，spring提供了许多IOC容器的实现。
  - 比如XmlBeanFactory，ClasspathXmlApplicationContext等。
  - 其中XmlBeanFactory就是针对最基本的IoC容器的实现，这个IoC容器可以读取XML文件定义的BeanDefinition（XML文件中对bean的描述），如果说XmlBeanFactory是容器中的屌丝，ApplicationContext应该算容器中的高帅富。
- ApplicationContext是Spring提供的一个高级的IoC容器，它除了能够提供IoC容器的基本功能外，还为用户提供了以下的附加服务。
- 从ApplicationContext接口的实现，我们看出其特点：
  1. 支持信息源，可以实现国际化。（实现MessageSource接口）
  2.  访问资源。(实现ResourcePatternResolver接口)
  3. 支持应用事件。(实现ApplicationEventPublisher接口)

### **BeanDefinition**

- SpringIOC容器管理了我们定义的各种Bean对象及其相互的关系，Bean对象在Spring实现中是以BeanDefinition来描述的，其继承体系如下：

  ![](img\2.x-png)

- Bean 的解析过程非常复杂，功能被分的很细，因为这里需要被扩展的地方很多，必须保证有足够的灵活性，以应对可能的变化。Bean 的解析主要就是对 Spring 配置文件的解析。这个解析过程主要通过下图中的类完成：

  ![](img\3.x-png)

## IoC容器的初始化

- IoC容器的初始化包括BeanDefinition的Resource定位、载入和注册这三个基本的过程。

- 我们以ApplicationContext为例讲解，ApplicationContext系列容器也许是我们最熟悉的，因为web项目中使用的XmlWebApplicationContext就属于这个继承体系，还有ClasspathXmlApplicationContext等，其继承体系如下图所示：

  ![](img\4.x-png)

- ApplicationContext允许上下文嵌套，通过保持父上下文可以维持一个上下文体系。

- 对于bean的查找可以在这个上下文体系中发生，首先检查当前上下文，其次是父上下文，逐级向上，这样为不同的Spring应用提供了一个共享的bean定义环境。

### 下面我们分别简单地演示一下两种ioc容器的创建过程

1. **XmlBeanFactory(屌丝IOC)的整个流程**

   - 通过XmlBeanFactory的源码，我们可以发现：

   ```Java
   public class XmlBeanFactory extends DefaultListableBeanFactory{

        private final XmlBeanDefinitionReader reader; 

        public XmlBeanFactory(Resource resource)throws BeansException{
            this(resource, null);
        }
        
        public XmlBeanFactory(Resource resource, BeanFactory parentBeanFactory)
             throws BeansException{
            super(parentBeanFactory);
            this.reader = new XmlBeanDefinitionReader(this);
            this.reader.loadBeanDefinitions(resource);
       }
    }
   ```

   ```Java
   //根据Xml配置文件创建Resource资源对象，该对象中包含了BeanDefinition的信息
    ClassPathResource resource = new ClassPathResource("application-context.xml");
   //创建DefaultListableBeanFactory
    DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
   //创建XmlBeanDefinitionReader读取器，用于载入BeanDefinition。之所以需要BeanFactory作为参数，是因为会将读取的信息回调配置给factory
    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
   //XmlBeanDefinitionReader执行载入BeanDefinition的方法，最后会完成Bean的载入和注册。完成后Bean就成功的放置到IOC容器当中，以后我们就可以从中取得Bean来使用
    reader.loadBeanDefinitions(resource);
   ```

   - 通过前面的源码，`this.reader = new XmlBeanDefinitionReader(this); `中其中this 传的是 factory 对象。

2. **FileSystemXmlApplicationContext 的IOC容器流程**

   1. 高富帅IOC解剖：

      - `ApplicationContext =new FileSystemXmlApplicationContext(xmlPath);`。
      - 先看其构造函数：
      - 调用构造函数：

      ```Java
      /**
      * Create a new FileSystemXmlApplicationContext, loading the definitions
      * from the given XML files and automatically refreshing the context.
      * @param configLocations array of file paths
      * @throws BeansException if context creation failed
       */public FileSystemXmlApplicationContext(String... configLocations) throws BeansException {
              this(configLocations, true, null);
          }
      ```

      - 实际调用：

      ```Java
      public FileSystemXmlApplicationContext(String[] configLocations, boolean refresh, ApplicationContext parent)  
              throws BeansException {    
          super(parent);  
          setConfigLocations(configLocations);  
          if (refresh) {  
              refresh();  
          }  
      }
      ```

   2. **设置资源加载器和资源定位**

      - 通过分析FileSystemXmlApplicationContext的源代码可以知道，在创建FileSystemXmlApplicationContext容器时，构造方法做以下两项重要工作：
        - 首先，调用父类容器的构造方法(`super(parent)`方法)为容器设置好Bean资源加载器。
        - 然后，再调用父类AbstractRefreshableConfigApplicationContext的`setConfigLocations(configLocations)`方法设置Bean定义资源文件的定位路径。
      - 通过追踪FileSystemXmlApplicationContext的继承体系，发现其父类的父类AbstractApplicationContext中初始化IoC容器所做的主要源码如下：

      ```Java
      public abstract class AbstractApplicationContext extends DefaultResourceLoader  
              implements ConfigurableApplicationContext, DisposableBean {  
          // 静态初始化块，在整个容器创建过程中只执行一次  
          static {  
              // 为了避免应用程序在Weblogic8.1关闭时出现类加载异常加载问题，加载IoC容  
              // 器关闭事件(ContextClosedEvent)类  
              ContextClosedEvent.class.getName();  
          }  
          // FileSystemXmlApplicationContext调用父类构造方法调用的就是该方法  
          public AbstractApplicationContext(ApplicationContext parent) {  
              this.parent = parent;  
              this.resourcePatternResolver = getResourcePatternResolver();  
          }  
          // 获取一个Spring Source的加载器用于读入Spring Bean定义资源文件  
          protected ResourcePatternResolver getResourcePatternResolver() {  
              // AbstractApplicationContext继承DefaultResourceLoader，也是一个  
              // Spring资源加载器，其getResource(String location)方法用于载入资源  
              return new PathMatchingResourcePatternResolver(this);  
          }   
      ……  
      }
      ```

      - AbstractApplicationContext构造方法中调用PathMatchingResourcePatternResolver的构造方法创建Spring资源加载器：

      ```Java
      public PathMatchingResourcePatternResolver(ResourceLoader resourceLoader) {  
              Assert.notNull(resourceLoader, "ResourceLoader must not be null");  
              //设置Spring的资源加载器  
              this.resourceLoader = resourceLoader;  
      } 
      ```

      - 在设置容器的资源加载器之后，接下来FileSystemXmlApplicationContext执行setConfigLocations方法通过调用其父类AbstractRefreshableConfigApplicationContext的方法进行对Bean定义资源文件的定位，该方法的源码如下：

      ```Java
      //处理单个资源文件路径为一个字符串的情况  
      public void setConfigLocation(String location) {  
         //String CONFIG_LOCATION_DELIMITERS = ",; /t/n";  
         //即多个资源文件路径之间用” ,; /t/n”分隔，解析成数组形式  
          setConfigLocations(StringUtils.tokenizeToStringArray(location, CONFIG_LOCATION_DELIMITERS));  
      }  

      //解析Bean定义资源文件的路径，处理多个资源文件字符串数组  
       public void setConfigLocations(String[] locations) {  
          if (locations != null) {  
              Assert.noNullElements(locations, "Config locations must not be null");  
              this.configLocations = new String[locations.length];  
              for (int i = 0; i < locations.length; i++) {  
                  // resolvePath为同一个类中将字符串解析为路径的方法  
                  this.configLocations[i] = resolvePath(locations[i]).trim();  
              }  
          }  
          else {  
              this.configLocations = null;  
          }  
      }
      ```

      - 通过这两个方法的源码我们可以看出，我们既可以使用一个字符串来配置多个Spring Bean定义资源文件，也可以使用字符串数组，即下面两种方式都是可以的：
        - `ClasspathResource res = new ClasspathResource(“a.xml,b.xml,……”);`
        - `ClasspathResource res = new ClasspathResource(newString[]{“a.xml”,”b.xml”,……});`
      - 至此，Spring IoC容器在初始化时将配置的Bean定义资源文件定位为Spring封装的Resource。

   3. AbstractApplicationContext的refresh函数载入Bean定义过程：

      - Spring IoC容器对Bean定义资源的载入是从refresh()函数开始的，refresh()是一个模板方法，refresh()方法的作用是：
        - 在创建IoC容器前，如果已经有容器存在，则需要把已有的容器销毁和关闭，以保证在refresh之后使用的是新建立起来的IoC容器。
        - refresh的作用类似于对IoC容器的重启，在新建立好的容器中对容器进行初始化，对Bean定义资源进行载入。
      - FileSystemXmlApplicationContext通过调用其父类AbstractApplicationContext的refresh()函数启动整个IoC容器对Bean定义的载入过程：

      ```Java
      public void refresh() throws BeansException, IllegalStateException {  
         synchronized (this.startupShutdownMonitor) {  
             //调用容器准备刷新的方法，获取容器的当时时间，同时给容器设置同步标识  
             prepareRefresh();  
             //告诉子类启动refreshBeanFactory()方法，Bean定义资源文件的载入从  
             //子类的refreshBeanFactory()方法启动  
             ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();  
             //为BeanFactory配置容器特性，例如类加载器、事件处理器等  
             prepareBeanFactory(beanFactory);  
             try {  
                 //为容器的某些子类指定特殊的BeanPost事件处理器  
                 postProcessBeanFactory(beanFactory);  
                 //调用所有注册的BeanFactoryPostProcessor的Bean  
                 invokeBeanFactoryPostProcessors(beanFactory);  
                 //为BeanFactory注册BeanPost事件处理器.  
                 //BeanPostProcessor是Bean后置处理器，用于监听容器触发的事件  
                 registerBeanPostProcessors(beanFactory);  
                 //初始化信息源，和国际化相关.  
                 initMessageSource();  
                 //初始化容器事件传播器.  
                 initApplicationEventMulticaster();  
                 //调用子类的某些特殊Bean初始化方法  
                 onRefresh();  
                 //为事件传播器注册事件监听器.  
                 registerListeners();  
                 //初始化所有剩余的单态Bean.  
                 finishBeanFactoryInitialization(beanFactory);  
                 //初始化容器的生命周期事件处理器，并发布容器的生命周期事件  
                 finishRefresh();  
             }  
             catch (BeansException ex) {  
                 //销毁以创建的单态Bean  
                 destroyBeans();  
                 //取消refresh操作，重置容器的同步标识.  
                 cancelRefresh(ex);  
                 throw ex;  
             }  
         }  
      }
      ```

      - `refresh()`方法主要为IoC容器Bean的生命周期管理提供条件，Spring IoC容器载入Bean定义资源文件从其子类容器的`refreshBeanFactory()`方法启动，所以整个`refresh()`中`ConfigurableListableBeanFactory beanFactory =obtainFreshBeanFactory();`这句以后代码的都是注册容器的信息源和生命周期事件，载入过程就是从这句代码启动。
      - `refresh()`方法的作用是：
        - 在创建IoC容器前，如果已经有容器存在，则需要把已有的容器销毁和关闭，以保证在refresh之后使用的是新建立起来的IoC容器。
        - refresh的作用类似于对IoC容器的重启，在新建立好的容器中对容器进行初始化，对Bean定义资源进行载入。
      - AbstractApplicationContext的`obtainFreshBeanFactory()`方法调用子类容器的`refreshBeanFactory()`方法，启动容器载入Bean定义资源文件的过程，代码如下：

      ```Java
      protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {  
          //这里使用了委派设计模式，父类定义了抽象的refreshBeanFactory()方法，具体实现调用子类容器的refreshBeanFactory()方法
          refreshBeanFactory();  
          ConfigurableListableBeanFactory beanFactory = getBeanFactory();  
          if (logger.isDebugEnabled()) {  
              logger.debug("Bean factory for " + getDisplayName() + ": " + beanFactory);  
          }  
          return beanFactory;  
      }
      ```

      - AbstractApplicationContext子类的`refreshBeanFactory()`方法：

        - AbstractApplicationContext类中只抽象定义了`refreshBeanFactory()`方法，容器真正调用的是其子类AbstractRefreshableApplicationContext实现的`refreshBeanFactory()`方法，方法的源码如下：

        ```Java
        protected final void refreshBeanFactory() throws BeansException {  
           if (hasBeanFactory()) {//如果已经有容器，销毁容器中的bean，关闭容器  
               destroyBeans();  
               closeBeanFactory();  
           }  
           try {  
                //创建IoC容器  
                DefaultListableBeanFactory beanFactory = createBeanFactory();  
                beanFactory.setSerializationId(getId());  
               //对IoC容器进行定制化，如设置启动参数，开启注解的自动装配等  
               customizeBeanFactory(beanFactory);  
               //调用载入Bean定义的方法，主要这里又使用了一个委派模式，在当前类中只定义了抽象的loadBeanDefinitions方法，具体的实现调用子类容器  
               loadBeanDefinitions(beanFactory);  
               synchronized (this.beanFactoryMonitor) {  
                   this.beanFactory = beanFactory;  
               }  
           }  
           catch (IOException ex) {  
               throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);  
           }  
        }
        ```

        ​