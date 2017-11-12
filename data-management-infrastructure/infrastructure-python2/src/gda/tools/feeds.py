# -*- coding: utf-8 -*-

'''
Created on 2017-11-01 17:10:40
Universal feed parser工具模块.
@author: zhoujiagen
'''

import re

import feedparser
from prettytable import PrettyTable


# BBC RSS订阅源
RSS_FEED_BBC_NEWS = [
    'http://feeds.bbci.co.uk/news/politics/rss.xml#',
    'http://feeds.bbci.co.uk/news/health/rss.xml#',
    'http://feeds.bbci.co.uk/news/education/rss.xml#',
    'http://feeds.bbci.co.uk/news/world/rss.xml#',
    'http://feeds.bbci.co.uk/news/uk/rss.xml#',
    'http://feeds.bbci.co.uk/news/business/rss.xml#',
    'http://feeds.bbci.co.uk/news/technology/rss.xml#',
    'http://feeds.bbci.co.uk/news/entertainment_and_arts/rss.xml#',
]


def get_word_counts(url):
    """获取url中文本单词和词频.

    Args:
        url: 订阅源URL.

    Returns:
        标题, 单词和词频字典: (title, {word: frequency}).

    Raises:
        None
    """
    result = {}

    feedParserDict = feedparser.parse(url)
    for entry in feedParserDict.entries:
        if 'summary' in entry:
            summary = entry.summary
        else:
            summary = entry.description

        words = get_words(entry.title + ' ' + summary)
        for word in words:
            result.setdefault(word, 0)
            result[word] += 1

    return feedParserDict.feed.title, result


def show_word_counts(word_counts):
    """展示单词和词频.

    Args:
        word_counts: 单词和词频{word: frequency}

    Returns:
        None

    Raises:
        None
    """
    table = PrettyTable(['Word', 'Count'])
    for word, count in word_counts.items():
        table.add_row([word, count])
    print table


def get_words(html):
    """获取HTML内容中单词.

    Args:
        html: HTML内容.

    Returns:
        单词列表: [word].

    Raises:
        None
    """
    text = re.compile(r'<[^>]+>').sub('', html)  # 移除HTML标签
    words = re.compile(r'[^A-Z^a-z]+').split(text)  # 非字母
    return [word.lower() for word in words if word != '']


def download_feeds(urls, outfile='feeddata.txt', low_fraction=0.2, high_fraction=0.5):
    """下载订阅源信息.

    Args:
        url: 订阅源URL列表.
        outfile: 输出文件.
        low_fraction: 单词在文档中出现的最低比例.
        high_fraction: 单词在文档中出现的最高比例.

    Returns:
       None

    Raises:
        None
    """
    word_doccount = {}  # {单词: 有该单词的文档数量}
    doc_wordcounts = {}  # {标题: {单词: 词频}}

    for url in urls:
        print 'DEBUG>>> processing %s' % url
        title, wc = get_word_counts(url)
        doc_wordcounts[title] = wc
        for word, count in wc.items():
            word_doccount.setdefault(word, 0)
            if count > 1:
                word_doccount[word] += 1

    # 选取在10%~50%文档中出现的单词
    words = []
    for word, doccount in word_doccount.items():
        fraction = float(doccount) / len(urls)
        if fraction > low_fraction and fraction < high_fraction:
            words.append(word)

    # 处理pettytable数据
    out = file(outfile, 'w')
    out.write('title')
    for word in words:
        out.write('\t%s' % word)
    out.write('\n')

    headers = ['title']
    headers.extend(words)
    table = PrettyTable(headers)
    for doc, wordcounts in doc_wordcounts.items():
        out.write(doc)
        row = [doc]
        for word in words:
            if word in wordcounts:
                out.write('\t%d' % wordcounts[word])
                row.append(wordcounts[word])
            else:
                out.write('\t0')
                row.append('0')
        out.write('\n')
        table.add_row(row)

    print table
    out_table = file('table-' + outfile, 'w')
    out_table.write(str(table))


def load_feeds_from_file(infile='feeddata.txt'):
    """从文件加载订阅源信息.

    Args:
        infile: 输入文件, 见download_feeds().

    Returns:
       (文档标题列表, 单词列表, 单词在文档中的词频矩阵)
       矩阵行为文档标题, 列为单词.

    Raises:
        None
    """
    lines = [line for line in file(infile)]
    words = lines[0].strip().split('\t')[1:]
    docs = []
    matrix = []
    for line in lines[1:]:
        row_fileds = line.split('\t')
        docs.append(row_fileds[0])
        matrix.append([float(value) for value in row_fileds[1:]])

    return docs, words, matrix


if __name__ == '__main__':
    WORD_COUNTS = get_word_counts('http://feeds.bbci.co.uk/news/entertainment_and_arts/rss.xml#')
    # show_word_counts(WORD_COUNTS[1])

    # download_feeds(RSS_FEED_BBC_NEWS)
    DOCS, WORDS, MATRIX = load_feeds_from_file()
    print DOCS
    print WORDS
    print MATRIX

