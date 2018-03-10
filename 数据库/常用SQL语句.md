# 常用的SQL语句

### 创建表

##### 语法

```SQL
CREATE TABLE <表名> (
	<列名> <数据类型> [列级完整约束条件]
	[,<列名> <数据类型> [列级完整约束条件]]
    ...
    [,<表级完整性约束条件>]
);
```

##### 示例

```SQL
-- 创建学生表
CREATE TABLE student(
    s_id CHAR(9) PRIMARY KEY,
    s_name CHAR(20) UNIQUE,
    s_sex CHAR(2),
    s_age SMALLINT,
    s_dept CHAR(20)
);

-- 创建课程表
CREATE TABLE course(
	c_id CHAR(4) PRIMARY KEY,
    c_name CHAR(40) NOT NULL,
    c_parent_id CHAR(4),
    c_credit SMALLINT,
    FOREIGN KET (c_parent_id) REFERENCES course(c_id)
);

-- 建立学生选课表
CREATE TABLE student_course(
    s_id CHAR(9),
    c_id CHAR(4),
    sc_grade SMALLINT,
    PRIMARY KEY(s_id, c_id),
    FOREIGN KEY (s_id) REFERENCES student(s_id),
    FOREIGN KEY (c_id) REFERENCES course(c_id)
);
```

### 修改表

##### 语法

```SQL
ALTER TABLE <表名>
[ADD [COLUMN] <新列名><数据类型>[完整性约束]]
[ADD <表级完整性约束>]
[DROP [COLUMN]<列名>[CASCADE|RESTRICT]]
[DROP CONSTRAINT<完整性约束名>[RESTRICT|CASCADE]]
[ALTER COLUMN <列名><数据类型>];
```

##### 示例

```SQL
-- 往student表添加入学时间
ALTER TABLE student ADD s_entrance DATE;
-- 将年龄的数据类型由字符型（假设原来是字符型）改为整型
ALTER TABLE student ALTER COLUMN s_age INT;
-- 增加课名称必须取唯一值的约束条件
ALTER TABLE course ADD UNIQUE(c_name);
```

### 删除基本表

##### 语法

```SQL
DROP TABLE <表名> [RESTRICT|CASCADE];
```

##### 示例

```SQL
DROP TABLE student CASCADE;
```

### 索引的建立、修改、删除

##### 语法

```SQL
CREATE [UNIQUE] [CLUSTER] INDEX <索引名> ON <表名>(<列名>[次序][,<列名>[<次序>]]...);

ALTER INDEX <旧索引名> RENAME TO <新索引名>;

DROP INDEX <索引名>;
```

##### 示例

```SQL
CREATE UNIQUE INDEX student_id on student(s_id);
CREATE UNIQUE INDEX course_id on course(c_id);
CREATE UNIQUE INDEX sc_id on student_course(s_id ASC, c_id desc);

ALTER INDEX sc_id RENAME TO student_course_id;

DROP INDEX student_id;
```

### 数据查询

##### 语法

```SQL
SELECT [ALL|DISTINCT] <目标列表达式> [别名] [,<目标列表达式> [别名]]...
FROM <表名或视图名> [别名] [,<表名或视图名> [别名]]... | (<SELECT 语句>) [AS] <别名>
[WHERE <条件表达式>]
[GROUP BY <列名1> [HAVING <条件表达式>]]
[ORDER BY <列名2> [ASC|DESC]];
```

##### 示例

```SQL
-- 查询全体学生的学号与姓名
SELECT s_id, s_name FROM student;
-- 查询全体学生的姓名、学号、所在系
SELECT s_name, s_id, s_dept FROM student;
-- 查询全体学生的详细记录
SELECT * FROM student;
-- 去重
SELECT DISTINCT s_id FROM student_course;
```

##### WHERE常用的查询条件

| 查询条件             | 谓词                                               |
| -------------------- | -------------------------------------------------- |
| 比较                 | =, >, <, >=, <=, !=, <>, !>, !<;NOT+上述比较运算符 |
| 确定范围             | BETWEEN AND, NOT BETWEEN AND                       |
| 确定集合             | IN, NOT IN                                         |
| 字符匹配             | LIKE, NOT LIKE                                     |
| 空值                 | IS NULL, IS NOT NULL                               |
| 多重条件（逻辑运算） | AND, OR, NOT                                       |

##### 聚集函数

| 函数                         | 作用                                   |
| ---------------------------- | -------------------------------------- |
| COUNT(*)                     | 统计元组个数                           |
| COUNT([DISTINCT\|ALL]<列名>) | 统计一列中值的个数                     |
| SUM([DISTINCT\|ALL]<列名>)   | 计算一列值的综合（此列必须是数值型）   |
| AVG([DISTINCT\|ALL]<列名>)   | 计算一列值的平均值（此列必须是数值型） |
| MAX([DISTINCT\|ALL]<列名>)   | 求一列值中的最大值                     |
| MIN([DISTINCT\|ALL]<列名>)   | 求一列值中的最小值                     |

##### 连接查询

###### 例子

- A表：

| id   | name |
| ---- | ---- |
| 1    | 张三 |
| 2    | 李四 |
| 3    | 王五 |

- B表：

| id   | job  | parent_id |
| ---- | ---- | --------- |
| 1    | 23   | 1         |
| 2    | 34   | 2         |
| 3    | 34   | 4         |

###### 内链接

`select a.*, b.* from a inner join b on a.id = b.parent_id;`

- 结果：

| a.id | a.name | b.id | b.job | b.parent_id |
| ---- | ------ | ---- | ----- | ----------- |
| 1    | 张三   | 1    | 23    | 1           |
| 2    | 李四   | 2    | 34    | 2           |

###### 左外连接

`select a.*, b.* from a left join b on a.id = b.parent_id;`

- 结果

| a.id | a.name | b.id | b.job | b.parent_id |
| ---- | ------ | ---- | ----- | ----------- |
| 1    | 张三   | 1    | 23    | 1           |
| 2    | 李四   | 2    | 34    | 2           |
| 3    | 王五   | null | null  | null        |

###### 右外连接

`select a.*, b.* from a right join b on a.id = b.parent_id; `

- 结果

| a.id | a.name | b.id | b.job | b.parent_id |
| ---- | ------ | ---- | ----- | ----------- |
| 1    | 张三   | 1    | 23    | 1           |
| 2    | 李四   | 2    | 34    | 2           |
| null | null   | 3    | 34    | 4           |

###### 全连接

`select a.*, b.* from a full join b on a.id = b.parent_id;`

- 结果

| a.id | a.name | b.id | b.job | b.parent_id |
| ---- | ------ | ---- | ----- | ----------- |
| 1    | 张三   | 1    | 23    | 1           |
| 2    | 李四   | 2    | 34    | 2           |
| 3    | 王五   | null | null  | null        |
| null | null   | 3    | 34    | 4           |

##### 嵌套查询

1. 带有IN\NOT IN谓词的子查询：

   ```SQL
   SELECT s_name FROM student WHERE s_id IN (
   	SELECT s_id FROM student_course WHERE c_id = '2'
   );
   ```

2. 带有比较运算符的子查询：

   ```SQL
   SELECT s_id FROM student WHERE s_dept = (
   	SELECT s_dept FROM student WHERE s_name = '老王'
   );
   ```

3. 带有ANY（SOME）或ALL谓词的子查询：

   | 谓词                                              | 作用                                                         |
   | ------------------------------------------------- | ------------------------------------------------------------ |
   | \>ANY(\<ANY、\>=ANY、\<=ANY、=ANY、!=(或\<\>)ANY) | 大于（小于、大于等于、小于等于、等于、不等于）子查询结果中的某个值 |
   | \>ALL(\<ALL、\>=ALL、\<=ALL、=ALL、!=(或\<\>)ALL) | 大于（小于、大于等于、小于等于、等于、不等于）子查询结果中的所有值 |

   - 示例

   ```SQL
   SELECT s_name, s_age FROM student WHERE s_age < ANY(
       SELECT s_age FROM student WHERE s_dept = 'CS'
   );
   ```

4. 带有EXIST\NOT EXIST谓词的子查询

   ```SQL
   SELECT s_name FROM student WHERE EXISTS (
   	SELECT * FROM student_course WHERE s_id = student.s_id
   );
   ```


##### 集合查询

- 并操作(UNION)、交操作(INTERSECT)、差操作(EXCEPT)。

```SQL
SELECT * FROM student WHERE s_dept = 'CS'
UNION
SELECT * FROM student WHERE s_age <= 19;
```

##### 基于派生表的查询

```SQL
SELECT s_id, c_id FROM student_course as sc, (
    SELECT s_id, AVG(sc_grade) FROM student_course GROUP BY s_id
) AS avg_sc(avg_s_id, avg_sc_grade)
WHERE sc.s_id = avg_sc.avg_s_id and sc.sc_grade >= avg_sc.avg_sc_grade;
```

### 插入数据

##### 语法

```SQL
INSERT INTO <表名>[(<属性列1> [,<属性列2>]...)] VALUES (<常量1> [,<常量2>]...)
```

##### 示例

```SQL
INSERT INTO student(s_id, s_name, s_sex, s_dept, s_age)
VALUES('1', '老王', '男', 'CS', 18);

INSERT INTO student VALUES('1', '老王', '男', 18, 'CS');
```

### 更新数据

##### 语法

```SQL
UPDATE <表名> SET <列名> = <表达式> [,<列名> = <表达式>]...[WHERE <条件>];
```

##### 示例

```SQL
UPDATE student SET s_age = 20 WHERE s_id = '1';
```

### 删除数据

##### 语法

```SQL
DELETE FROM <表名> [WHERE <条件>];
```

##### 示例

```SQL
DELETE FROM student WHERE s_id = '1';
```

### 空值处理

- 用`IS NULL`或`IS NOT NULL` 。

```SQL
SELECT * FROM student WHERE s_dept IS NULL;
```

### 视图

##### 语法

```SQL
CREATE VIEW <视图名> [(<列名> [,<列名>]...)]
AS <子查询>
[WITH CHECK OPTION];
```

##### 示例

```SQL
CREATE VIEW is_student
AS
SELECT s_id, s_name, s_age FROM student where s_dept = 'CS';
```

