# Servlet生命周期及工作原理

### Servlet生命周期

Servlet生命周期分为三个阶段： 

1. 初始化阶段：调用init()方法 。
2. 响应客户请求阶段：调用service()方法。
3. 终止阶段：调用destroy()方法。

### Servlet工作原理

- Servlet接收和响应客户请求的过程：
  - 首先客户发送一个请求，Servlet是调用service()方法对请求进行响应的，通过源代码可见，service()方法中对请求的方式进行了匹配，选择调用doGet，doPost等这些方法，然后再进入对应的方法中调用逻辑层的方法， 实现对客户的响应。
- 在Servlet接口和GenericServlet中是没有doGet、doPost等这些方法的，HttpServlet中定义 了这些方法，但是都是返回error信息，所以，我们每次定义一个Servlet的时候，都必须实现doGet或doPost等这些方法。
- 每一个自定义的Servlet都必须实现Servlet的接口，Servlet接口中定义了五个方法，其中比较重要的三个方法涉及到Servlet的生命周期，分别是上文提到的init()、service()、destroy()方法。
- GenericServlet是一个通用的，不特定于任何协议的 Servlet,它实现了Servlet接口。而HttpServlet继承于GenericServlet，因此HttpServlet也实现了Servlet接口。所以我们定义Servlet的时候只需要继承HttpServlet即可。
- Servlet接口和GenericServlet是不特定于任何协议的，而HttpServlet是特定于HTTP协议的类，所以HttpServlet中实现了service()方法，并将请求ServletRequest，ServletResponse强转为HttpRequest和 HttpResponse。