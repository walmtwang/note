# [DFA算法实现敏感词过滤](http://blog.csdn.net/chenssy/article/details/26961957)

### DFA简介

- 在实现文字过滤的算法中，DFA是唯一比较好的实现算法。DFA即Deterministic Finite Automaton，也就是确定有穷自动机，它是是通过event和当前的state得到下一个state，即event+state=nextstate。

### 敏感词构建成树

- 在我们的敏感词库中存在如下几个敏感词：日本人、日本鬼子、毛泽东。那么我需要构建成一个什么样的结构呢？
- 首先：query 日 ---> {本}、query 本 --->{人、鬼}、query 人 --->{null}、query 鬼 ---> {子}、query 子 --->{null}，形如下结构：![](img/15.png?raw=true)
- 这样我们就将我们的敏感词库构建成了一个类似与一颗一颗的树，这样我们判断一个词是否为敏感词时就大大减少了检索的匹配范围。比如我们要判断日本人，根据第一个字我们就可以确认需要检索的是那棵树，然后再在这棵树中进行检索。
- 但是如何来判断一个敏感词已经结束了呢？利用标识位来判断。
- 所以对于这个关键是如何来构建一棵棵这样的敏感词树。下面我已Java中的HashMap为例来实现DFA算法。
- 注：
  - 下面流程图是用一个敏感词构建/扩大一个棵树，每个敏感词都会构建一个树或者扩大某棵树。
  - 获取字是指遍历某个敏感词的各个字。
- ![](img/16.png?raw=true)

##### [程序实现](https://www.cnblogs.com/AlanLee/p/5329555.html)

```Java
/**
 * 构建敏感词库
 *
 * @param keyWordSet
 * @return 返回构建好的词库
 */
public static Map initKeyWord(Set<String> keyWordSet) {
    // 初始化HashMap对象并控制容器的大小
    Map sensitiveWordMap = new HashMap(keyWordSet.size());
    // 敏感词
    String key = null;
    // 用来按照相应的格式保存敏感词库数据
    Map nowMap = null;
    // 用来辅助构建敏感词库
    Map newWordMap = null;
    // 使用一个迭代器来循环敏感词集合
    Iterator<String> iterator = keyWordSet.iterator();
    while (iterator.hasNext()) {
        key = iterator.next();
        // 重新赋值，nowMap在接下来可能会改变
        nowMap = sensitiveWordMap;
        for (int i = 0; i < key.length(); i++) {
            char keyChar = key.charAt(i);

            // 判断这个字是否存在于敏感词库中
            Map wordMap = (Map) nowMap.get(keyChar);
            if (wordMap != null) {
                nowMap = wordMap;
            } else {
                newWordMap = new HashMap();
                newWordMap.put("isEnd", Boolean.FALSE);
                nowMap.put(keyChar, newWordMap);
                nowMap = newWordMap;
            }

            // 如果该字是当前敏感词的最后一个字，则标识为结尾字
            if (i == key.length() - 1) {
                nowMap.put("isEnd", Boolean.TRUE);
            }
        }
    }
    return sensitiveWordMap;
}
```

- 假设敏感词为“中国人民”、“中国男人”、“五星红旗”，得到的树为：

```control
{五={isEnd=false, 星={红={旗={isEnd=true}, isEnd=false}, isEnd=false}}, 中={isEnd=false, 国={男={人={isEnd=true}, isEnd=false}, 人={民={isEnd=true}, isEnd=false}, isEnd=false}}}
```

### 使用敏感词库过滤敏感词

```Java
/**
 * 判断是否存在敏感词
 * @param content
 * @param sensitiveWordMap
 * @return
 */
public static boolean isContaintSensitiveWord(String content, Map sensitiveWordMap) {
    // 一个一个字遍历
    for (int i = 0; i < content.length(); i++) {
        if (checkSensitiveWord(content, i, sensitiveWordMap)) {
            return true;
        }
    }
    return false;
}

/**
 * 判断content的index下标开头的字及其接下来的字是否组成敏感词
 * @param content
 * @param index
 * @param sensitiveWordMap
 * @return
 */
private static boolean checkSensitiveWord(String content, int index, Map sensitiveWordMap) {
    Map nowMap = sensitiveWordMap;
    for (int i = index; i < content.length(); i++) {
        char c = content.charAt(i);
        // 判断该字是否存在于敏感词库中
        nowMap = (Map) nowMap.get(c);
        // nowMap为空，不是敏感词
        if (nowMap == null) {
            return false;
        } else {
            // isEnd为true，找到敏感词，否则，接下去寻找
            if ((Boolean) nowMap.get("isEnd")) {
                return true;
            }
        }
    }
    return false;
}
```

