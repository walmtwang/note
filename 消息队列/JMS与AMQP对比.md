# [JMS与AMQP对比](http://geek.csdn.net/news/detail/71894)

## JMS

- Java消息传递服务（Java Messaging Service (JMS)）
- JMS是最成功的异步消息传递技术之一。随着Java在许多大型企业应用中的使用，JMS就成为了企业系统的首选。它定义了构建消息传递系统的API。

![](img/2)

- 下面是JMS的主要特性：
  - 面向Java平台的标准消息传递API，只允许基于JAVA实现的消息平台的之间进行通信。
  - 在Java或JVM语言比如Scala、Groovy中具有互用性。
  - 无需担心底层协议。
  - 有queues和topics两种消息传递模型。
  - 支持事务。
  - 能够定义消息格式（消息头、属性和内容）。
- 通信机制：
  - 消息生产者和消息消费者必须知道对方的Queue。
- 消息传输机制的区别：
  - JMS支持PTP和publis/subscribe机制,PTP只可以点对点通信,public/subscribe在一端发出请求后所有其他端收到消息。

---

## AMQP

- 高级消息队列协议
- JMS非常棒而且人们也非常乐意使用它。微软开发了NMS（.NET消息传递服务）来支持他们的平台和编程语言，它效果还不错。
- 但是碰到了互用性的问题。两套使用两种不同编程语言的程序如何通过它们的异步消息传递机制相互通信呢。
- 此时就需要定义一个异步消息传递的通用标准。JMS或者NMS都没有标准的底层协议。它们可以在任何底层协议上运行，但是API是与编程语言绑定的。
- AMQP解决了这个问题，它使用了一套标准的底层协议，加入了许多其他特征来支持互用性，为现代应用丰富了消息传递需求。

![](img/3)

- 下面是AMQP的主要特性：
  - 独立于平台的底层消息传递协议
  - 消费者驱动消息传递
  - 跨语言和平台的互用性
  - 它是底层协议的
  - 有5种交换类型direct，fanout，topic，headers，system
  - 面向缓存的
  - 可实现高性能
  - 支持长周期消息传递
  - 支持经典的消息队列，循环，存储和转发
  - 支持事务（跨消息队列）
  - 支持分布式事务（XA，X/OPEN，MS DTC）
  - 使用SASL和TLS确保安全性
  - 支持代理安全服务器
  - 元数据可以控制消息流
  - 不支持LVQ
  - 客户端和服务端对等
  - 可扩展
- 通信机制：
  - 消息生产者和消息消费者无须知道对方的Queue,消息生产者将Exchange通过Route key和任意Queue绑定。消息消费者通过Route key从任意Queue中获取Exchange。
- 消息传输机制的区别：
  1. 所有RouteKey相同的Queue接受到数据。
  2. 所有相同的Exchange的Queue接受到数据。
  3. 所有wilecard的Exchange的Queue接受到数据。
  4. 可以让webservice等接受到数据。