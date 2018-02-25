# [基于redis实现的点赞功能设计思路详解](http://www.jb51.net/article/113210.htm)

## 前言

- 点赞其实是一个很有意思的功能。基本的设计思路有大致两种， 一种自然是用mysql等。
- 数据库直接落地存储， 另外一种就是利用点赞的业务特征来扔到redis(或memcache)中， 然后离线刷回mysql等。

## 直接写入Mysql

- 直接写入Mysql是最简单的做法。
- 做两个表即可：
  1. `post_like` ：记录文章被赞的次数，已有多少人赞过这种数据就可以直接从表中查到；
  2. `user_like_post` ：记录用户赞过了哪些文章， 当打开文章列表时，显示的有没有赞过的数据就在这里面。

### 缺点

- 数据库读写压力大。
  - 热门文章会有很多用户点赞，甚至是短时间内被大量点赞， 直接操作数据库从长久来看不是很理想的做法。

## redis存储随后批量刷回数据库

- redis主要的特点就是快， 毕竟主要数据都在内存；
- 另外为啥我选择redis而不是memcache的主要原因在于redis支持更多的数据类型， 例如hash, set, zset等。
- 下面具体的会用到这几个类型。

### 优点

1. 性能高。
2. 缓解数据库读写压力。

- 其实我更多的在于缓解写压力， 读压力通过mysql主从甚至通过加入redis对热点数据做缓存都可以解决，写压力对于前面的方案确实是不大好使。

### 缺点

1. 开发复杂。
   - 这个比直接写mysql的方案要复杂很多， 需要考虑的地方也很多。
2. 不能保证数据安全性。
   - redis挂掉的时候会丢失数据， 同时不及时同步redis中的数据， 可能会在redis内存置换的时候被淘汰掉。
   - 不过对于我们点赞而已， 稍微丢失一点数据问题不大。

### 具体设计

##### Mysql设计

- 这一块和写入写mysql是一样的，毕竟是要落地存储的。
- 所以还是同样的需要`post_like`, `user_like_post`这两表存储文章被点赞的个数(等统计), 用户对那些文章点了赞(取消赞)。
- 这两表分别通过`post_id`, `user_id`进行关联。

##### redis设计部分

- 在redis中弄一个set存放所有被点赞的文章：`post_set`。
- 对每个post以post_id作为key，搞一个set存放所有对该post点赞的用户：`post_user_like_set_{$post_id}`。
- 将每个用户对每个post的点赞情况放到一个hash里面去， hash的字段就跟进需求来处理：`post_user_like_{$post_id}_{$user_id}`。
- 对每个post维护一个计数器， 用来记录当前在redis中的点赞数：`post_{$post_id}_counter`。
  - 这里我们只用counter记录尚未同步到mysql中的点赞数(可以为负)， 每次刷回mysql中时将counter中的数据和数据库已有的赞数相加即可。

##### 用户点赞/取消赞

- 获取`user_id`, `post_id`， 查询该用户是否已经点过赞， 已点过则不允许再次点赞。
- 这里需要注意的是用户点赞的记录可能在数据库中， 也可能在缓存中， 所以查询的时候缓存和数据库都要查询， 缓存没有再查询数据库。
- 将用户的点赞/取消赞的情况记录在redis中， 具体为：
  1. 写入`post_set`。
     - 将`post_id`写入`post_set`。
  2. 写入`post_user_like_set_{$post_id}`。
     - 将`user_id`写入`post_user_like_set_{$post_id}`。
  3. 写入`post_user_like_{$post_id}_{$user_id}`。
     - 将用户点赞数据， 例如赞状态, post_id, user_id, ctime(操作时间), mtime(修改时间)写入`post_user_like_{$post_id}_{$user_id}`中。
  4. 更新`post_{$post_id}_counter`。
     - 更新`post_{$post_id}_counter`, 这里的更新稍晚复杂一点， 需要和前面一样先获取当前用户是否对这个post点过赞。
     - 如果点过， 并且本次是取消赞， counter减一， 如果没点过， 本次是点赞， counter加一。
     - 如果原来是取消赞的情况， 本次是点赞， counter加一。

##### 同步刷回数据库

- 循环从`post_set`中pop出来一个`post_id`至到空。
- 根据`{$post_id}` , 每次从`post_user_like_set_{$post_id}`中pop出来一个`user_id`直到空。
- 根据`post_id`, `user_id`, 直接获取对应的hash表的内容(`post_user_like_{$post_id}_{$user_id}`。
- 将hash表中的数据写入`user_like_post`表中。
- 将`post_{$post_id}_counter`中的数据和`post_like`中的数据相加， 将结果写入到`post_like`表中。

##### 页面展示

1. 查询用户点赞情况。
   - 前面已经说过， 需要同时查询redis和mysql。
2. 查询post点赞统计。
   - 同样需要查询redis中的`post_{$post_id}_counter`和mysql的`post_like`表, 并将两者相加得到的结果才是正确的结果。

### 总结

- 解决了mysql读写的问题。
- 但没有针对用户量较大的场景考虑分表的设计, 可以考虑针对user_id或者post_id进行分表。