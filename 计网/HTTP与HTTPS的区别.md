# [HTTP与HTTPS的区别](http://www.cnblogs.com/wqhwe/p/5407468.html)

- 超文本传输协议HTTP协议被用于在Web浏览器和网站服务器之间传递信息，HTTP协议以明文方式发送内容，不提供任何方式的数据加密，如果攻击者截取了Web浏览器和网站服务器之间的传输报文，就可以直接读懂其中的信息，因此，HTTP协议不适合传输一些敏感信息，比如：信用卡号、密码等支付信息。
- 为了解决HTTP协议的这一缺陷，需要使用另一种协议：安全套接字层超文本传输协议HTTPS，为了数据传输的安全，HTTPS在HTTP的基础上加入了SSL协议，SSL依靠证书来验证服务器的身份，并为浏览器和服务器之间的通信加密。

### HTTP和HTTPS的基本概念

- HTTP：是互联网上应用最为广泛的一种网络协议，是一个客户端和服务器端请求和应答的标准（TCP），用于从WWW服务器传输超文本到本地浏览器的传输协议，它可以使浏览器更加高效，使网络传输减少。
- HTTPS：是以安全为目标的HTTP通道，简单讲是HTTP的安全版，即HTTP下加入SSL层，HTTPS的安全基础是SSL，因此加密的详细内容就需要SSL。
- HTTPS协议的主要作用可以分为两种：
  - 一种是建立一个信息安全通道，来保证数据传输的安全；
  - 另一种就是确认网站的真实性。

### HTTP与HTTPS有什么区别

- HTTP协议传输的数据都是未加密的，也就是明文的，因此使用HTTP协议传输隐私信息非常不安全，为了保证这些隐私数据能加密传输，于是网景公司设计了SSL（Secure Sockets Layer）协议用于对HTTP协议传输的数据进行加密，从而就诞生了HTTPS。简单来说，HTTPS协议是由SSL+HTTP协议构建的可进行加密传输、身份认证的网络协议，要比http协议安全。
- HTTPS和HTTP的区别主要如下：
  1. HTTPS协议需要到ca申请证书，一般免费证书较少，因而需要一定费用。
  2. HTTP是超文本传输协议，信息是明文传输，https则是具有安全性的SSL加密传输协议。
  3. HTTP和HTTPS使用的是完全不同的连接方式，用的端口也不一样，前者是80，后者是443。
  4. HTTP的连接很简单，是无状态的；HTTPS协议是由SSL+HTTP协议构建的可进行加密传输、身份认证的网络协议，比HTTP协议安全。

### HTTPS的工作原理

- 我们都知道HTTPS能够加密信息，以免敏感信息被第三方获取，所以很多银行网站或电子邮箱等等安全级别较高的服务都会采用HTTPS协议。![](https://github.com/walmt/interview_questions/blob/master/%E8%AE%A1%E7%BD%91/img/4.jpg?raw=true)
- 客户端在使用HTTPS方式与Web服务器通信时有以下几个步骤，如图所示。![](https://github.com/walmt/interview_questions/blob/master/%E8%AE%A1%E7%BD%91/img/1.gif?raw=true)
  1. 客户使用https的URL访问Web服务器，要求与Web服务器建立SSL连接。