/**
 * This package contains support routines to explain analysis procedure in Lucene<br/>
 * <strong>Terminologies</strong>
 * <p>
 * <h3>分析(analysis)</h3> 在Lucene中指将Field文本转换成最基本的索引表示单元(Term)的过程。
 * </p>
 * <p>
 * <h3>分析过程</h3> 分析器对分析操作进行了封装。 语汇单元化过程(tokenization): 分析器将文本转换成语汇单元(token)，可能包括的步骤有
 * <li>提取单词</li>
 * <li>去除标点符号</li>
 * <li>去掉字母上的音调符号</li>
 * <li>将字母转换成小写</li>
 * <li>取出常用词</li>
 * <li>将单词还原为词干形式(词干还原)</li>
 * <li>将单词转换成基本形式(词形归并lemmatization)</li> 从文本流中提取的文本块称为语汇单元(token)。token与域名(field)结合后形成项(term)。
 * </p>
 * <p>
 * <h3>分析操作使用时机</h3>
 * <li>建立索引期间</li>
 * <li>使用QueryParser对象搜索时</li>
 * <li>在搜索结果中高亮显示被搜索内容等</li>
 * </p>
 * <p>
 * <h3></h3>
 * </p>
 */
package com.spike.text.lucene.indexing.analysis;

