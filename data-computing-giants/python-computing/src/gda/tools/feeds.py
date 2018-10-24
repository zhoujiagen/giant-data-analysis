# -*- coding: utf-8 -*-

"""
Universal feed parser工具模块.
@author: zhoujiagen
Created on 2017-11-01 17:10:40
"""

import re

import feedparser
from prettytable import PrettyTable
import json
from gda.tools import WORKING_DIR


def get_word_counts(url):
    """
    获取url中文本单词和词频.
    :param url: 订阅源URL.
    :return: 标题, 单词和词频字典: (title, {word: frequency}).
    :raise None
    """
    result = {}

    feed_parser_dict = feedparser.parse(url)
    print("DEBUG>>>", feed_parser_dict.keys())
    for entry in feed_parser_dict.entries:
        if 'summary' in entry:
            summary = entry.summary
        else:
            summary = entry.description

        words = get_words(entry.title + ' ' + summary)
        for word in words:
            result.setdefault(word, 0)
            result[word] += 1

    return feed_parser_dict.feed.title, result


def show_word_counts(word_counts):
    """
    以表格形式展示单词和词频.
    :param word_counts: 单词和词频{word: frequency}
    :return: None
    :raise None
    """
    table = PrettyTable(['Word', 'Count'])
    for word, count in word_counts.items():
        table.add_row([word, count])
    print(table)


def get_words(html):
    """
    获取HTML内容中单词.
    :param html: HTML页面内容.
    :return: 单词列表: [word].
    :raise None
    """
    text = re.compile(r'<[^>]+>').sub('', html)  # 移除HTML标签
    words = re.compile(r'[^A-Z^a-z]+').split(text)  # 非字母
    return [word.lower() for word in words if word != '']


def download_feeds(urls, outfile=WORKING_DIR + 'pci/feeddata.txt', low_fraction=0.2, high_fraction=0.5):
    """
    下载订阅源信息.
    :param urls: 订阅源URL列表.
    :param outfile: 输出文件.
    :param low_fraction: 单词在文档中出现的最低比例.
    :param high_fraction: 单词在文档中出现的最高比例.
    :return: None
    :raise None
    """
    word_doc_count = {}  # {单词: 有该单词的文档数量}
    doc_word_counts = {}  # {标题: {单词: 词频}}

    for url in urls:
        print('DEBUG>>> processing %s' % url)
        title, wc = get_word_counts(url)
        doc_word_counts[title] = wc
        for word, count in wc.items():
            word_doc_count.setdefault(word, 0)
            if count > 1:
                word_doc_count[word] += 1

    # 选取在10%~50%文档中出现的单词
    words = []
    for word, doccount in word_doc_count.items():
        fraction = float(doccount) / len(urls)
        if fraction > low_fraction and fraction < high_fraction:
            words.append(word)

    # 处理pettytable数据
    out = open(outfile, 'w')
    out.write('title')
    for word in words:
        out.write('\t%s' % word)
    out.write('\n')

    headers = ['title']
    headers.extend(words)
    table = PrettyTable(headers)
    for doc, word_counts in doc_word_counts.items():
        out.write(doc)
        row = [doc]
        for word in words:
            if word in word_counts:
                out.write('\t%d' % word_counts[word])
                row.append(word_counts[word])
            else:
                out.write('\t0')
                row.append('0')
        out.write('\n')
        table.add_row(row)

    print(table)
    out_table = open(outfile[:-4] + '-table.txt', 'w')
    out_table.write(str(table))


def load_feeds_from_file(infile=WORKING_DIR + 'pci/feeddata.txt'):
    """
    从文件加载订阅源信息.
    :param infile: 输入文件, 见download_feeds().
    :return: (文档标题列表, 单词列表, 单词在文档中的词频矩阵)
       矩阵行为文档标题, 列为单词.
    :raise None
    """
    with open(infile) as f:
        lines = f.readlines()
    words = lines[0].strip().split('\t')[1:]
    docs = []
    matrix = []
    for line in lines[1:]:
        row_fields = line.split('\t')
        docs.append(row_fields[0])
        matrix.append([float(value) for value in row_fields[1:]])

    return docs, words, matrix


if __name__ == '__main__':
    WORD_COUNTS = get_word_counts('http://feeds.bbci.co.uk/news/entertainment_and_arts/rss.xml#')
    show_word_counts(WORD_COUNTS[1])

    with open(WORKING_DIR + "pci/bbc-news-rss.json") as f:
        feed_urls = json.loads(f.read())
    download_feeds(feed_urls)
    DOCS, WORDS, MATRIX = load_feeds_from_file()
    print(DOCS)
    print(WORDS)
    print(MATRIX)
