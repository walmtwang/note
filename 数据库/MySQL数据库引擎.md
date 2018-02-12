## [mysql 数据库引擎](http://www.cnblogs.com/0201zcr/p/5296843.html)

### 数据库引擎

- **数据库引擎**是用于存储、处理和保护数据的核心服务。
- 利用数据库引擎可控制访问权限并快速处理事务，从而满足企业内大多数需要处理大量数据的应用程序的要求。 
- 使用数据库引擎创建用于联机事务处理或联机分析处理数据的关系数据库。
- 这包括创建用于存储数据的表和用于查看、管理和保护数据安全的数据库对象（如索引、视图和存储过程）。

### 数据库引擎任务

- 在数据库引擎文档中，各主题的**顺序**遵循用于实现使用数据库引擎进行数据存储的系统的任务的主要顺序。
  - 设计并创建数据库以保存系统所需的关系或XML文档。
  - 实现系统以访问和更改数据库中存储的数据。包括实现网站或使用数据的应用程序，还包括生成使用SQL Server工具和实用工具以使用数据的过程。
  - 为单位或客户部署实现的系统。
  - 提供日常管理支持以优化数据库的性能。

### MySQL数据库引擎类别

- 你能用的数据库引擎取决于mysql在安装的时候是如何被编译的。要添加一个新的引擎，就必须重新编译MYSQL。在缺省情况下，MYSQL支持三个引擎：**ISAM、MYISAM和HEAP**。另外两种类型**INNODB**和**BERKLEY**（BDB），也常常可以使用。

##### ISAM

- ISAM是一个定义明确且历经时间考验的数据表格管理方法，它在设计之时就考虑到数据库被查询的次数要远大于更新的次数。
- 因此，ISAM执行读取操作的速度很快，而且不占用大量的内存和存储资源。I
- SAM的两个主要**不足之处**在于，它**不支持事务处理**，也**不能够容错**：如果你的硬盘崩溃了，那么数据文件就无法恢复了。
- 如果你正在把ISAM用在关键任务应用程序里，那就必须经常备份你所有的实时数据，通过其复制特性，MYSQL能够支持这样的备份应用程序。

##### MYISAM

- MYISAM是MYSQL的ISAM扩展格式和**缺省的数据库引擎**。
- 除了**提供**ISAM里所没有的**索引和字段管理的**功能，MYISAM还使用一种**表格锁定的机制**，来**优化多个并发的读写操作**。
- 其代价是你需要经常运行OPTIMIZE TABLE命令，来恢复被更新机制所浪费的空间。
- MYISAM还有一些有用的扩展，例如用来修复数据库文件的MYISAMCHK工具和用来恢复浪费空间的MYISAMPACK工具。
- MYISAM强调了快速读取操作，这可能就是为什么MYSQL受到了WEB开发如此青睐的主要原因：在WEB开发中你所进行的大量数据操作都是读取操作。
- 所以，大多数虚拟主机提供商和INTERNET平台提供商只允许使用MYISAM格式。

##### HEAP

- HEAP**允许只驻留在内存里的临时表格**。
- 驻留在内存里让HEAP要比ISAM和MYISAM都快，但是它所**管理的数据是不稳定的**，而且如果在关机之前没有进行保存，那么所有的数据都会丢失。
- 在数据行被删除的时候，HEAP也不会浪费大量的空间。
- HEAP表格在你需要使用SELECT表达式来选择和操控数据的时候非常有用。
- 要记住，在用完表格之后就删除表格。

##### INNODB和BERKLEYDB

- INNODB和BERKLEYDB（BDB）数据库引擎都是造就MYSQL灵活性的技术的直接产品，这项技术就是MYSQL++ API。
- 在使用MYSQL的时候，你所面对的每一个挑战几乎都源于ISAM和MYISAM数据库引擎不支持事务处理也不支持外来键。
- 尽管要比ISAM和MYISAM引擎慢很多，但是INNODB和BDB包括了**对事务处理和外来键的支持**，这两点都是前两个引擎所没有的。
- 如前所述，如果你的设计需要这些特性中的一者或者两者，那你就要被迫使用后两个引擎中的一个了。

### mysql数据引擎更换方式

##### 查看当前数据库支持的引擎和默认的数据库引擎：

```sql
show engines;
```

- 查询结果：

| Engine             | Support | Comment                                  | Transactions | XA   | Savepoints |
| ------------------ | ------- | ---------------------------------------- | ------------ | ---- | ---------- |
| InnoDB             | DEFAULT | Supports transactions, row-level locking, and foreign keys | YES          | YES  | YES        |
| MRG_MYISAM         | YES     | Collection of identical MyISAM tables    | NO           | NO   | NO         |
| MEMORY             | YES     | Hash based, stored in memory, useful for temporary tables | NO           | NO   | NO         |
| BLACKHOLE          | YES     | /dev/null storage engine (anything you write to it disappears) | NO           | NO   | NO         |
| MyISAM             | YES     | MyISAM storage engine                    | NO           | NO   | NO         |
| CSV                | YES     | CSV storage engine                       | NO           | NO   | NO         |
| ARCHIVE            | YES     | Archive storage engine                   | NO           | NO   | NO         |
| PERFORMANCE_SCHEMA | YES     | Performance Schema                       | NO           | NO   | NO         |
| FEDERATED          | NO      | Federated MySQL storage engine           | NULL         | NULL | NULL       |

##### 更改数据库引擎

1. 更改方式1：修改配置文件my.ini：

   - 在[mysqld]后面添加`default-storage-engine=InnoDB`，重启服务，数据库默认的引擎修改为InnoDB。

2. 更改方式2：

   - 在建表的时候指定：

   ```sql
   create table mytbl(   
       id int primary key,   
       name varchar(50)   
   )type=MyISAM;
   ```

3. 更改方式3：

   - 建表后更改：

   ```sql
   alter table mytbl2 type = InnoDB;
   ```

##### 查看修改结果

- 方式一：`show table status from mytest; `。
- 方式二：`show create table table_name;`。

# MyIASM和Innodb引擎详解

### Innodb引擎

- Innodb引擎提供了对数据库ACID**事务**的支持，并且实现了SQL标准的四种隔离级别。
- 该引擎还提供了行级锁和外键约束，它的设计目标是处理大容量数据库系统，它本身其实就是基于MySQL后台的完整数据库系统，MySQL运行时Innodb会在内存中建立缓冲池，用于缓冲数据和索引。
- 但是该引擎不支持FULLTEXT类型的索引，而且它没有保存表的行数，当`SELECT COUNT(*) FROM TABLE`时需要扫描全表。
- 当需要使用数据库事务时，该引擎当然是首选。由于锁的粒度更小，写操作不会锁定全表，所以在并发较高时，使用Innodb引擎会提升效率。
- 但是使用行级锁也不是绝对的，如果在执行一个SQL语句时MySQL不能确定要扫描的范围，InnoDB表同样会锁全表。

##### ACID含义：

- **A** 事务的**原子性**(Atomicity)：
  - **指一个事务要么全部执行,要么不执行**。
- **C** 事务的**一致性**(Consistency)：
  - 指事务的运行并不改变数据库中数据的一致性。
- **I** **独立性**(Isolation)：
  - 事务的独立性也有称作隔离性，是指两个以上的事务不会出现交错执行的状态。因为这样可能会导致数据不一致。
- **D** **持久性**(Durability)：
  - 事务的持久性是指事务执行成功以后，该事务所对数据库所作的更改便是持久的保存在数据库之中。

### MyIASM引擎

- MyIASM是MySQL默认的引擎，但是它没有提供对数据库事务的支持，也不支持行级锁和外键，因此当INSERT(插入)或UPDATE(更新)数据时即写操作需要锁定整个表，效率便会低一些。
- 不过和Innodb不同，MyIASM中存储了表的行数，于是SELECT COUNT(*) FROM TABLE时只需要直接读取已经保存好的值而不需要进行全表扫描。
- 如果表的读操作远远多于写操作且不需要数据库事务的支持，那么MyIASM也是很好的选择。

### 两种引擎的选择

- 大尺寸的数据集趋向于选择InnoDB引擎，因为它支持事务处理和故障恢复。
- 数据库的大小决定了故障恢复的时间长短，InnoDB可以利用事务日志进行数据恢复，这会比较快。
- 主键查询在InnoDB引擎下也会相当快，不过需要注意的是如果主键太长也会导致性能问题，关于这个问题我会在下文中讲到。
- 大批的INSERT语句(在每个INSERT语句中写入多行，批量插入)在MyISAM下会快一些，但是UPDATE语句在InnoDB下则会更快一些，尤其是在并发量大的时候。

# Index——索引

- **索引**（Index）是**帮助MySQL高效获取数据的数据结构**。
- MyIASM和Innodb都使用了树这种数据结构做为索引。
- 下面我接着讲这两种引擎使用的索引结构，讲到这里，首先应该谈一下B-Tree和B+Tree。

### MyIASM引擎的索引结构

- MyISAM引擎的索引结构为**B+Tree**，其中B+Tree的**数据域存储的内容为实际数据的地址**，也就是说它的索引和实际的数据是分开的，只不过是用索引指向了实际的数据，这种索引就是所谓的**非聚集索引**。如下图所示：![](https://github.com/walmt/interview_questions/blob/master/数据库/img/1.png?raw=true)
- 这里设表一共有三列，假设我们以Col1为主键，则上图是一个MyISAM表的主索引（Primary key）示意。可以看出MyISAM的索引文件仅仅保存数据记录的地址。
- 在MyISAM中，主索引和辅助索引（Secondary key）在结构上没有任何区别，只是主索引要求key是唯一的，而辅助索引的key可以重复。如果我们在Col2上建立一个辅助索引，则此索引的结构如下图所示：![](https://github.com/walmt/interview_questions/blob/master/数据库/img/2.png?raw=true)
- 同样也是一颗B+Tree，data域保存数据记录的地址。因此，MyISAM中索引检索的算法为首先按照B+Tree搜索算法搜索索引，如果指定的Key存在，则取出其data域的值，然后以data域的值为地址，读取相应数据记录。

### Innodb引擎的索引结构

- 与MyISAM引擎的索引结构同样也是B+Tree，但是Innodb的索引文件本身就是数据文件，即**B+Tree的数据域存储的就是实际的数据**，这种索引就是**聚集索引**。这个索引的key就是数据表的主键，因此InnoDB表数据文件本身就是主索引。
- 并且和MyISAM不同，InnoDB的**辅助索引数据域存储的也是相应记录主键的值**而不是地址，所以当以辅助索引查找时，会先根据辅助索引找到主键，再根据主键索引找到实际的数据。
  - 所以Innodb不建议使用过长的主键，否则会使辅助索引变得过大。
  - 建议使用自增的字段作为主键，这样B+Tree的每一个结点都会被顺序的填满，而不会频繁的分裂调整，会有效的提升插入数据的效率。

### 两者区别

- 第一个重大区别是InnoDB的数据文件本身就是索引文件。

  - 从上文知道，MyISAM索引文件和数据文件是分离的，索引文件仅保存数据记录的地址。
  - 而在InnoDB中，表数据文件本身就是按B+Tree组织的一个索引结构，这棵树的叶节点data域保存了完整的数据记录。
  - 这个索引的key是数据表的主键，因此InnoDB表数据文件本身就是主索引。

- 下图是InnoDB主索引（同时也是数据文件）的示意图，可以看到叶节点包含了完整的数据记录。

  - 这种索引叫做聚集索引。
  - 因为InnoDB的数据文件本身要按主键聚集，所以InnoDB要求表必须有主键（MyISAM可以没有）：
    - 如果没有显式指定，则MySQL系统会自动选择一个可以唯一标识数据记录的列作为主键。
    - 如果不存在这种列，则MySQL自动为InnoDB表生成一个隐含字段作为主键，这个字段长度为6个字节，类型为长整形。

  ![](https://github.com/walmt/interview_questions/blob/master/数据库/img/1.jpg?raw=true)

- 第二个与MyISAM索引的不同是InnoDB的辅助索引data域存储相应记录主键的值而不是地址。

  - 换句话说，InnoDB的所有辅助索引都引用主键作为data域。
  - 例如，下图为定义在Col3上的一个辅助索引：

  ![](https://github.com/walmt/interview_questions/blob/master/数据库/img/2.jpg?raw=true)

- 这里以英文字符的ASCII码作为比较准则。聚集索引这种实现方式使得按主键的搜索十分高效，但是辅助索引搜索需要检索两遍索引：首先检索辅助索引获得主键，然后用主键到主索引中检索获得记录。

### 小结

- 了解不同存储引擎的索引实现方式对于正确使用和优化索引都非常有帮助，例如知道了InnoDB的索引实现后，就很容易明白为什么不建议使用过长的字段作为主键，因为所有辅助索引都引用主索引，过长的主索引会令辅助索引变得过大。
- 再例如，用非单调(可能是指“非递增”的意思)的字段作为主键在InnoDB中不是个好主意，因为InnoDB数据文件本身是一颗B+Tree，非单调(可能是指“非递增”的意思)的主键会造成在插入新记录时数据文件为了维持B+Tree的特性而频繁的分裂调整，十分低效，而使用自增字段作为主键则是一个很好的选择。