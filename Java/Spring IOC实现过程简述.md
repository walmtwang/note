### 获取及解析配置文件

![](img/21.png)

1. 通过继承自AbstractBeanDefinitionReader中的方法，来使用ResourceLoader将资源文件路径转换为对应的Resource文件。

   - ClassPathResource：
     - Spring的配置文件通过ClassPathResource进行封装。
     - 在Java中，将不同来源的资源抽象成URL，通过注册不同的handler（URLStreamHandler）来处理。
     - 一般handler的类型使用不同前缀来识别，然而URL没有默认定义相对ClassPath或ServletContext等资源的handler，虽然可以注册自己的URLStreamHandler，但需要了解URL实现机制以及URL也没提供一些基本方法（如检查当前资源是否存在，是否可读等），因而Spring对其内部使用到的资源实现了自己的抽象结构：Resource接口来封装底层资源。

   ![](img/22.png)

2. 通过DocumentLoader对Resource文件进行转换，将Resource文件转换为Document文件。

   - 主要步骤：
     1. 创建DocumentBuilderFactory。
     2. 通过DocumentBuilderFactory创建DocumentBuilder。
     3. 进而解析InputSource来返回Document对象。

3. 通过实现接口BeanDefinitionDocumentReader的DefaultBeanDefinitionDocumentReader类对Document进行解析，并使用BeanDefinitionParserDelegate对Element进行解析。

### 默认标签解析

###### bean标签的解析

- 有四个默认标签，分别是import、alias、bean和beans。
- **其中最重要且最复杂的是bean标签的解析：**
  1. 提取元素中的id以及name属性。
  2. 进一步解析其他所有属性并统一封装至GenericBeanDefinition类型的实例中。
     1. 解析class属性
     2. 解析parent属性
     3. 创建用于承载属性的AbstractBeanDefinition类型的GenericBeanDefinition
     4. 硬编码解析默认bean的各种属性
        1. 解析scope属性
        2. 解析abstract属性
        3. 解析lazy-init属性
        4. 解析autowire属性
        5. 解析dependency-check属性
        6. 解析depends-on属性
        7. 解析autowire-candidate属性
        8. 解析primary属性
        9. 解析init-method属性
        10. 解析destroy-method属性
        11. 解析factory-method属性
        12. 解析factory-bean属性
     5. 提取description
     6. 解析元数据
     7. 解析lookup-method属性
     8. 解析replaced-method属性
     9. 解析构造函数参数
     10. 解析property子元素
     11. 解析qualifier子元素
  4. 如果检测到bean没有指定beanName，那么使用默认规则为此Bean生成beanName。
  4. 将获取到的信息封装到BeanDefinitionHolder的实例中。

###### 注册解析的BeanDefinition

- 解析的BeanDefinition都会被注册到BeanDefinitionRegistry类型的实例registry中，而对于beanDefinition的注册分成两部分：**通过beanName的注册以及通过别名的注册**。
  1. 通过beanName注册BeanDefinition：
     1. 对AbstractBeanDefinition的校验，校验AbstractBeanDefinition的methodOverrides属性的。
     2. 对beanName已经注册的情况的处理。如果设置了不允许bean的覆盖，则需要抛出异常，否则直接覆盖。
     3. 加入map缓存。
     4. 清除解析之前留下的对应的beanName的缓存。
  2. 通过别名注册BeanDefinition：
     1. 若alias与beanName并名称相同则不需要处理并删除掉原有的alias。
     2. alias覆盖处理，若aliasName已经使用并已经指向另一beanName则需要用户的设置进行处理。
     3. alias循环检查，当A->B存在时，若再次出现A->C->B时候则会抛出异常。
     4. 注册alias。

###### 通知监听器解析及注册完成：

- 当程序开发人员需要对注册BeanDefinition时间进行监听时可以通过注册监听器的方式并将处理逻辑写入监听器，目前在Spring中并没有对此事件做任何逻辑处理。

##### 其余默认标签的解析

###### alias标签的解析

- 在对bean进行定义时，除了使用id属性来指定名称之外，为了提供多个名称，可以使用alias标签来指定。
- 在XML配置文件中，可以单独的使用元素来完成bean别名的定义。如配置文件中定义了一个JavaBean：`<bean id="testBean" class="com.test"/>`
- 要给这个JavaBean增加别名，我们就可以直接使用bean标签中的name属性：`<bean id="testBean" name="testBean,testBean2" class="com.test"/>`
- 同样，Spring还有另外一种声明别名的方式：

```xml
<bean id="testBean" class="com.test"/>
<alias name="testBean" alias="testBean,testBean2"/>
```

- alias标签的解析过程：
  1. 获取beanName
  2. 获取alias
  3. 注册alias
  4. 别名注册后通知监听器做相应处理

###### import标签的解析

- 使用import是分模块的一种，我们可以构造这样的Spring配置文件：

```xml
<beans>
	<import resource="customerContext.xml">
	<import resource="systemContext.xml">
</beans>
```

- Spring通过`importBeanDefinitionResource(ele);`方法解析配置文件，步骤大致如下：
  1. 获取resource属性所表示的路径。
  2. 解析路径中的系统属性，格式如"`${user.dir}`"。
  3. 判断location是绝对路径还是相对路径。
  4. 如果是绝对路径责任递归调用bean的解析过程，进行另一次解析。
  5. 如果是相对路径则计算出绝对路径并进行解析。
  6. 通知监听器，解析完成。

###### 嵌入式beans标签的解析

- 嵌入式的beans标签，非常类似于import标签所提供的功能，使用如下：

```xml
<beans xmls="……"
       xmls:xsi="……"
       xsi:schemaLocation="……">
	<bean id="aa" class="test.aa"/>
  	<beans>
  	……
  	</beans>
</beans>
```

- 对于嵌入式beans标签来讲，与单独的配置文件并没有太大的差别，无非是递归调用beans的解析过程。

### 自定义标签解析

- 略。

### bean的加载

- bean的加载功能实现远比bean的解析要复杂得多。
- 当调用getBean的时候才会进行对应bean的加载。

##### 其过程如下：

1. 转化对应beanName。
   - 传入参数name可能是beanName，可能是别名，也可能是FactoryBean，所所以需要进行一系列的解析，解析内容如下：
     1. 去除FactoryBean的修饰符，也就是如果name=“&aa”，name会首先去除&而使name=“aa”。
     2. 取指定alias所表示的最终beanName，例如别名A指向名称为B的bean则返回B；若别名A指向别名B，别名B又指向名称C的bean则返回C。
2. 尝试从缓存中加载单例。
   - 单例在Spring的统一容器内只会被创建一次，后续再获取bean，就直接从单例缓存中获取了。
   - 当然这里只是尝试加载，如果加载不成功则再次尝试从singletonFactories中加载。
   - 因为在创建单例bean的时候会存在依赖注入的情况，而在创建依赖的时候为了避免循环依赖，在Spring中创建bean的原则是不等bean创建完成就会将创建bean的ObjectFactory提早曝光加入缓存中，一旦下一个bean创建的时候需要依赖上一个bean则直接使用ObjectFactory。
3. bean的实例化。
   - 如果从缓存中得到了bean的原始状态，则需要对bean进行实例化。
   - 缓存中记录的只是最原始的bean状态，并不一定是我们最终想要的bean。
   - 举个例子：假如我们需要对工厂bean进行处理，那么这里得到的其实是工厂bean的初始状态，但是我们真正需要的是工厂bean中定义的factory-method方法中返回的bean，而getObjectForBeanInstance就是完成这工作的。
4. 原型模式的依赖检查
   - 只有在单例情况下才会尝试解析循环依赖，如果存在A中有B的属性，B中有A的属性，那么当依赖注入的时候，就会产生当A还未创建完的时候因为对B的创建再次返回创建A，造成循环依赖，也就是情况：`isPrototypeCurrentlyInCreation(beanName)`判断true。
5. 检测parentBeanFactory
   - 从代码上看，如果缓存没有数据的话直转到父类工厂上去加载了。
   - 一个很重要的判断条件：`parentBeanFactory != null && !containsBeanDefinition(beanName)`。如果parentBeanFactory为空则其他一切都无意义了，但是`!containsBeanDefinition(beanName)`就比较重要了，它是在检测如果当前加载的XML配置文件中不包含beanName所对应的配置，就只能到parentBeanFactory去尝试，然后再去递归的调用getBean方法。
6. 将存储XML配置文件的GenericBeanDefinition转换为RootBeanDefinition
   - 因为从XML配置文件中读取到的Bean信息是存储在GenericBeanDefinition中的，但是所有的Bean后续处理都是针对于RootBeanDefinition的，所以这里需要进行一个转换，转换的同时如果父类bean不为空的话，则会一并合并父类的属性。
7. 寻找依赖
   - 因为bean的初始化过程很可能会用到某些属性，而某些属性很可能是动态配置的，并且配置成依赖于其他的bean，那么这个时候就有必要先加载依赖的bean。
   - 所以在Spring的加载顺序中，在初始化某一个bean的时候首先会初始化这个bean所对应的依赖。
8. 针对不同的scope进行bean的创建
   - 在Spring中存在着不同的scope，其中默认的是singleton，但是还有些其他的配置诸如prototype。request之类的。
   - 在这个步骤中，Spring会根据不同的配置进行不同的初始化策略。
9. 类型转换
   - 程序到这里返回bean后已经基本结束了，通常对该方法的调用参数requiredType是为空的。
   - 但是可能会存在这样的情况，返回的bean其实是个String，但是requiredType却传入Integer类型，那么这时候本步骤就会起作用了。
   - 他的功能是将返回的bean转换为requiredType所指定的类型。
   - String转换为Integer是最简单的一种转换，在Spring中提供了各种各样的转换器，用户也可以自己扩展转换器来满足需求。

![](img\20.png)

### bean的加载过程

##### 缓存中获取单例bean

- 单例在Spring的同一个容器内只会被创建一次，后续再获取bean直接从单例缓存中获取。
- 当然这里也只是尝试加载，首先尝试从缓存中加载，然后再次尝试从singletonFactories中加载。
- 因为在创建单例bean的时候会存在依赖注入的情况，而在创建依赖的时候为了避免循环依赖，Spring创建bean的原则是不等bean创建完成就会将创建bean的ObjectFactory提早曝光加入到缓存中，一旦下一个bean创建时需要依赖上一个bean，则直接使用ObjectFactory。
- 其过程如下：
  - 这个方法首先尝试从singletonObjects里面获取实例。
  - 如果获取不到在从earlySingletonObjects里面获取。
  - 如果还获取不到，再尝试从singletonFactories里面获取beanName对应的ObjectFactory，然后调用这个ObjectFactory的getObject来创建bean，并放到earlySingletonObjects里面去，并且从singletonFactories里remove掉这个ObjectFactory。
  - 而对于后续的所有内存操作都只为了循环依赖检测时候使用，也就是在allowEarlyReference为true的情况下才会使用。
- 这里涉及用于存储bean的不同的map，简单解释如下：
  - singletonObjects：用于保存BeanName创建bean实例之间的关系，bean name -->bean instance。
  - singletonFactories：用于保存BeanName和创建bean的工厂之间的关系，bean name -->ObjectFactory。
  - earlySingletonObjects：也是保存BeanName和创建bean实例之间的关系，与singletonObjects的不同之处在于，当一个单例bean被放到这里面后，那么当bean还在创建过程中，就可以通过getBean方法获取到了，其目的是用来检测循环引用。
  - registeredSingletons：用来保存当前所有已注册的bean。

##### 从bean的实例中获取对象

- 在getBean方法中，getObjectForBeanInstance是个高频率使用的方法，无论是从缓存中获得bean还是根据不同的scope策略加载bean。
- 我们得到bean的实例后要做的第一步就是调用这个方法来检测一下正确性，其实就是用于检测当前bean是否是FactoryBean类型的Bean，如果是，那么需要调用该bean对应的FactoryBean实例中的`getObject()`作为返回值。
- 举个例子，假如我们需要对工厂bean进行处理，那么这里得到的其实是工厂bean的初始状态，但是我们真正需要的是工厂bean中定义的factory-method方法中返回的bean，而getObjectForBeanInstance方法就是完成这个工作的。
- 其工作过程：
  - 对FactoryBean正确性的验证。
  - 对非FactoryBean不做任何处理。
  - 对bean进行转换。
  - 将从Factory中解析bean的工作委托给getObjectFromFactoryBean。

##### 获取单例

- 如果缓存中不存在已经加载的单例bean，就需要从头开始bean的加载过程了，而Spring中使用getSingleton的重载方法实现bean的加载过程。
- 代码中使用了回调方法，使得程序可以在单例创建的前后做一些准备及处理操作，而真正的获取单例bean的方法其实并不是在此方法中实现，其实现逻辑实在ObjectFactory类型的实例singletonFactory中实现的。
- 这些准备及处理操作包括如下内容：
  1. 检查缓存是否已经加载过。
  2. 若没有加载，则记录beanName的正在加载状态。
  3. 加载单例前记录加载状态。
     - `beforeSingletonCreation(beanName)`方法做了一个很重要的操作：记录加载状态，通过`this.singletonsCurrentlyInCreation.add(beanName)`方法将当前正要创建的bean记录在缓存中，这样便可以对循环依赖进行检测。
  4. 通过调用参数传入的ObjectFactory的个体Object方法实例化bean。
  5. 加载单例后的处理方法调用。
     - 同步骤3的记录加载状态相似，当bean加载结束后需要移除缓存中对该bean的正在加载状态的记录。
  6. 将结果记录至缓存并删除加载bean过程中所记录的各种辅助状态。
  7. 返回处理结果。
     - 虽然我们已经从外部了解了加载bean的逻辑架构，但现在我们还并没有开始对bean加载功能的探索。
     - bean的加载逻辑其实是在传入的ObjectFactory类型的参数singletonFactory中定义的。
     - ObjectFactory的核心部分其实只是调用了createBean的方法。

##### 准备创建bean

- createBean函数完成的具体步骤及功能：
  1. 根据设置的class属性或者根据className来解析Class。
  2. 对override属性进行标记及验证。
     - 在Spring中没有override-method这样的配置，在Spring配置中是存在look-method和replace-method的，而这两个配置的加载其实就是将配置统一存放在BeanDefinition中的methodOverrides属性里，而这个函数的操作其实也就是针对于这两个配置的。
  3. 应用初始化前的后处理器，解析指定bean是否存在初始化前的短路操作。
  4. 创建bean。

##### 处理override属性

- 在Spring配置中存在lookup-method和replace-method两个配置功能，而这两个配置的加载其实就是将配置统一存放在BeanDefinition中的methodOverrides属性里。
- 这两个功能实现原理其实是在bean实例化的时候如果检测到存在methodOverrides属性，会动态地位当前bean生成代理并使用对应的拦截器为bean做增强处理。
- 对于方法匹配来讲，如果一个类中存在若干个重载方法，那么在函数调用及增强的时候还需要根据参数类型进行匹配，来最终确认当前调用的到底是哪个函数。
- 但是，Spring将一部分匹配工作在这里完成了，如果当前类中的方法只有一个，那么就设置重载该方法没有被重载，这样在后续调用的时候便可以直接使用找到的方法，而不需要进行方法的参数匹配验证，而且还可以提前对方法存在性进行验证。

##### 实例化的前置处理

- 在真正调用doCreate方法创建bean的实例前使用`resolveBeforeInstantiation(beanName, mbdToUse)`对BeanDefinition中的属性做些前置处理。

- 在函数中提供了一个短路判断，是最为关键的部分：

  ```Java
  if (bean != null) {
  	return bean;
  }
  ```

- 当经过前置处理后返回的结果如果不为空，那么会直接略过后续的Bean的创建而直接返回结果。

- 这一特性虽然很容易被忽略，但是缺起着至关重要的作用，我们熟知的AOP功能就是基于这里的判断的。

- 此方法中最主要的是两个方法`applyBeanPostProcessorsBeforeInstantiation(targetType, beanName)`和`applyBeanPostProcessorsAfterInitialization(bean, beanName)`。

- 两个方法实现非常简单，只是对后处理器中的所有InstantiationAwareBeanPostProcessor类型的后处理器进行postProcessBeforeInstantiation方法和BeanPostProcesser的postProcessAfterInitialization方法的调用。

### 创建bean

- 当经历过resolveBeforeInstantiation方法后，程序有两个选择，如果创建了代理或者说重写了InstantiationAwareBeanPostProcessor的postProcessBeforeInstantiation方法并在方法postProcessBeforeInstantiation方法并在方法postProcessBeforeInstantiation中改变了bean，则直接返回就可以了，否则需要进行常规bean的创建。
- 而这常规bean的创建就是在doCreateBean中完成的。
- 整个函数的概要思路：
  1. 如果是单例则需要首先清除缓存。
  2. 实例化bean，将BeanDefinition转换为BeanWrapper。
     - 转换是一个复杂的过程，但是我们可以尝试概括大致的功能，如下所示：
       1. 如果存在工厂方法则使用工厂方法进行初始化。
       2. 一个类有多个构造函数，每个构造函数都有不同的参数，所以需要根据参数锁定构造函数并进行初始化。
       3. 如果既不存在工厂方法也不存在带有参数的构造函数，则使用默认的构造函数进行bean的实例化。
  3. MergedBeanDefinitionPostProcessor的应用。
     - bean合并后的处理，Autowired注解正是通过此方法实现诸如类型的预解析。
  4. 依赖处理。
     - 在Spring中会有循环依赖的情况，例如，当A中含有B的属性，而B中又含有A的属性时就会构成一个循环依赖，此时如果A和B都是单例，那么在Spring中的处理方式就是当创建B的时候，涉及自动注入A的步骤时，并不是直接去再次创建A，而是通过放入缓存中的ObjectFactory来创建实例，这样就解决了循环依赖的问题。
  5. 属性填充。
     - 将所有属性填充至bean的实例中。
  6. 循环依赖检查。
     - 在Spring中解决循环依赖只对单例有效，而对于prototype的bean，Spring唯一要做的就是抛出异常。
     - 在这个步骤里会检测已经加载的bean是否已经出现依赖循环，并判断是否需要抛出异常。
  7. 注册DisposableBean。
     - 如果配置了destroy-method，这里需要注册以便于在销毁时候调用。
  8. 完成创建并返回。
- 上面的步骤非常的繁琐，每一步骤都使用了大量的代码来完成其功能。
- 最复杂和最难以理解的是循环依赖的处理，在真正进入doCreateBean前先了解一下循环依赖。

##### 创建bean实例

- 首先从ceateBeanInstance开始，该方法实例化的逻辑：
  1. 如果在RootBeanDefinition中存在factoryMethodName属性，或者说在配置文件中配置了factory-method，那么Spring会尝试使用`instantiateUsingFactoryMethod(beanName, mbd, args)`方法根据RootBeanDefinition中的配置生成bean的实例。
  2. 解析构造函数并进行构造函数的实例化。
     - 因为一个bean对应的类中可能会有多个构造函数，而每个构造函数的参数不同，Spring在根据参数及类型去判断最终会使用哪个构造函数进行实例化。
     - 判断的过程是个比较消耗性能的步骤，所以采用缓存机制，如果已经解析过则不需要重复解析而是直接从RootBeanDefinition中的属性resolvedConstructorOrFactoryMethod缓存的值去取，否则需要再次解析，并将解析的结果添加至RootBeanDefinition中的属性resolvedConstructorOrFactoryMethod中。

###### autowireConstructor

- 对于实例化的创建Spring中分成两种情况，一种是通用的实例化，另一种是带有参数的实例化。
- 带有参数的实例化过程相当复杂，因为存在不确定性，所以在判断对于参数上做了大量工作。
- autowireConstructor函数实现的功能有以下几个方面：

1. 构造函数参数的确定

   - 根据explicitArgs参数判断。

     - 如果传入的参数explicitArgs不为空，那便可以直接确定参数，因为explicitArgs参数是在调用Bean的时候用户指定的，在BeanFactory类中存在这样的方法：`Object getBean(String name, Object... args) throws BeansException;`。
     - 在获取bean的时候，用户不但可以指定bean的名称还可以指定bean所对应类的构造函数或者工厂方法的方法参数，主要用于静态工厂方法的调用，而这里是需要给定完全匹配的参数的。所以便可以判断，如果传入参数explicitArgs不为空，则可以确定构造函数参数就是它。

   - 缓存中获取

     - 除此之外，确定参数的办法如果之前已经分析过，也就是构造函数已经纪录在缓存中，那么便可以直接拿来使用。
     - 在缓存中缓存的可能是参数的最终类型也可能是参数的初始类型，例如：构造函数参数要求的是int类型，但是原始的参数值可能是String类型的“1”，那么即时在缓存中得到了参数，也需要经过类型转换器的过滤以确保参数类型与对应的构造函数参数类型完全对应。

   - 配置文件获取

     - 如果不能根据传入的参数explicitArgs确定构造函数的参数也无法在缓存中得到相关信息，那么就开启新一轮的分析了。
     - 分析从获取配置文件中配置的构造函数信息开始，经过之前的分析，我们知道Spring中配置文件中的信息经过转换都会通过BeanDefinition实例承载，也就是mbd中包含，那么可以通过调用`mbd.getConstructorArgumentValues()`获取配置的构造函数信息。
     - 有了配置中的信息便可以获取对应的参数信息了，获取参数值的信息包括直接指定值，如：直接指定构造函数中某个值为原始类型String类型，或者是一个对其他bean的引用，而这一处理委托给resolveConstructorArguments方法，并返回能解析到的参数的个数。
2. 构造器的确定。
   - 经过了第一步后已经确定了构造函数的参数，接下来的任务就是根据构造函数参数在所有构造函数中锁定对应的构造函数。
   - 而匹配的方法就是根据参数个数匹配，所以在匹配之前需要先对构造函数按照public构造函数优先参数数量降序、非public构造函数参数数量降序。
   - 这样可以在遍历的情况下迅速判断排在后面的构造函数参数个数是否符合条件。
   - 由于在配置文件中并不是唯一限制使用参数位置索引的方式去创建，同样还支持指定参数名称进行设定参数值的情况，如`<constructor-arg name="aa">`，那么这种情况就需要首先确定构造函数中的参数名称。
   - 获取参数名称可以有两种方式，一种是通过注解的方式直接获取，另一种就是通过使用Spring中提供的工具类ParameterNameDiscoverer来获取。
   - 构造函数、参数名称、参数类型、参数值都确定后就可锁定构造函数以及转换对应的参数类型了。
3. 根据确定的构造函数转化对应的参数类型。
   - 主要是使用Spring中提供的类型转换器或者用户提供的自定义类型转换器进行转换。
4. 构造函数不确定性的验证。
   - 有时即使构造函数、参数名称、参数类型、参数值都确定后也不一定会直接锁定构造函数，不同构造函数的参数为父子关系，所以Spring在最后又做了一次验证。
5. 根据实例化策略以及得到的构造函数及构造函数参数实例化Bean。

###### instantiateBean函数

- 实现无参数类型bean的构造。
- 此方法并没有什么实质性的逻辑，所以没有参数的话那将是非常简单的一件事，直接调用实例化策略进行实例化就可以了。

###### 实例化策略

- Spring没有使用最简单的反射方法直接反射来构造实例对象。
- 程序中，首先判断如果`beanDefinition.getMethodOverrides()`为空也就是用户没有使用replace或者lookup的配置方法，那么就直接使用反射的方式。
- 但是如果使用了这两个特性，就使用动态代理的方式将包含两个特性所对应的逻辑的拦截增强器设置进去。保证在调用方法的时候会被相应的拦截器增强，返回值为包含拦截器的代理实例。

### 记录创建bean的ObjectFactory

- 在doCreateBean函数中将ObjectFactory加入了SingletonFactory，其原因是为了防止循环依赖造成的死循环：![](img\23.png)
- 该图展示了创建beanA的流程，从图中我们看到，在创建A我的时候首先会记录类A所对应的beanName，并将beanA的创建工厂加入缓存中。
- 而在对A的属性填充也就是调用populate方法的时候又会在一次的对B进行递归创建。
- 同样的，因为B中同样存在A属性，因此在实例化B的populate方法中又会再次地初始化B，就是图形的最后，调用getBean(A)。
- 关键是在这里，在这个函数中并不是直接去实例化A，而是先去检测缓存中是否有已经创建好的对应的bean，或者是否已经创建好的ObjectFactory，而此时对于A的ObjectFactory早已经创建好，所以便不会再去向后执行，而是直接调用ObjectFactory去创建A，这里最关键的是ObjectFactory的实现。
- 根据分析，Spring处理循环依赖的解决办法是：
  - 在B中创建依赖A时通过ObjectFactory提供的实例化方法来中断A中的属性填充，使B中持有的A仅仅是刚刚初始化并没有填充任何属性的A，而这正初始化A的步骤还是在最开始创建A的时候进行的。
  - 但是因为A与B中的A所表示的属性地址是一样的，所以在A中创建好的属性填充自然可以通过B中的A获取，这样就解决了循环依赖的问题。

### 属性注入

- populateBean这个函数的主要功能就是属性填充。

- 在populateBean函数中提供了这样的处理流程：

  1. InstantiationAwareBeanPostProcessor处理器的postProcessAfterInstantiation函数的应用，此函数可以控制程序是否继续进行属性填充。
  2. 根据注入类型(byName/byType)，提取依赖的bean，并统一存入PropertyValues中。
  3. 应用InstantiationAwareBeanPostProcessor处理器的postProcessPropertyValues方法，对属性获取完毕填充前对属性的再次处理，典型应用是RequiredAnnotationBeanPostProcessor类中对属性的验证。
  4. 将所有PropertyValues中的属性填充至BeanWrapper中。

  - 在上面的步骤中，依赖注入（autowireByName/autowireByType）以及属性填充是比较重要的。

##### autowireByName

- 这个函数的功能是在传入的参数pvs中找出已经已经加载的bean，并递归实例化，进而加入到pvs中。

##### autowireByType

- autowireByType与autowireByName使用的复杂程度都很相似，但是其实现功能的复杂度却完全不一样。
- 实现根据名称自动匹配的第一步就是寻找bw中需要依赖注入的属性，同样对于根据类型自动匹配的实现来讲第一步也是寻找bw中需要依赖注入的属性，然后遍历这些属性并寻找类型匹配的bean，其中最复杂的就是寻找类型匹配的bean。
- 同时，Spring中提供了对集合的类型注入的支持，如使用注解的方式：

```Java
@Autowired
private List<Test> tests;
```

- Spring将会把所有与Test匹配的类型找出来并注入到tests属性中。
- 所以在autowireByType函数中，新建了局部遍历autowiredBeanNames，用于存储所有依赖的bean，如果只是对非集合类的属性注入来说，此属性并无用处。
- 对寻找类型匹配的逻辑实现封装在了resolveDependency函数中。