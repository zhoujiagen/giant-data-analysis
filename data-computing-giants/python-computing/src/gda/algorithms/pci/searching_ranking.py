# -*- coding: utf-8 -*-

"""
搜索和排序.
@author: zhoujiagen
Created on 24/10/2018 5:16 PM
"""
from gda.algorithms.pci import WORKING_DIR
import pymysql
import pprint


def load_data():
    """加载抓取的JSON数据."""
    import json
    with open(WORKING_DIR + "scala-spec.json") as f:
        pages = json.loads(f.read())

    return sorted(pages, key=lambda page: page["url"])


def parse_data():
    """解析数据."""
    pages = load_data()

    urls = set([])
    url_self_ref_dict = {}  # 页面内自身引用
    url_refs = []  # {from: "", to: "", text: [word]}
    url_texts_dict = {}  # {url: [word]}
    for page in pages:
        url = page["url"]
        urls.add(url)

        url_texts_dict[url] = page["texts"]  # " ".join(texts)

        url_self_ref_dict.setdefault(url, [])
        out_links = page["out_links"]
        for out_link in out_links:
            out_url = out_link["url"]
            if out_url.startswith("#"):
                url_self_ref_dict[url].append(out_url)
            elif out_url.startswith("sec:"):
                url_self_ref_dict[url].append(out_url)
            else:
                out_base_url = out_url.split("#")[0]
                url_refs.append({"from": url, "to": out_base_url, "text": out_link["content"]})

    return {"urls": urls,
            "url_texts_dict": url_texts_dict,
            "url_self_ref_dict": url_self_ref_dict,
            "url_refs": url_refs, }


def show_data(data):
    """展示数据."""
    from prettytable import PrettyTable

    # 页面内容和自身引用信息
    distinct_words = set([])
    table = PrettyTable(['url', 'size', 'content', 'fragments'])
    table.align = "l"
    n = len(data["url_self_ref_dict"])
    for k, v in data["url_self_ref_dict"].items():
        distinct_words = distinct_words.union(set(data["url_texts_dict"][k]))
        table.add_row([k, len(data["url_texts_dict"][k]), data["url_texts_dict"][k][:100], v])
    print(table)
    print(len(distinct_words))

    # 页面间引用信息
    table = PrettyTable(['from', 'to', 'text'])
    table.align = "l"
    for url_ref in data["url_refs"]:
        table.add_row([url_ref["from"], url_ref["to"], url_ref["text"]])
    print(table)


def connect_mysql(host='127.0.0.1',
                  port=3306,
                  user='root',
                  password='admin',
                  database='pci',
                  charset='utf8'):
    """
    获取MySQL连接.
    :param host:
    :param port:
    :param user:
    :param password:
    :param database:
    :param charset:
    :return:
    """
    return pymysql.connect(host=host,
                           port=port,
                           user=user,
                           password=password,
                           database=database,
                           charset=charset)


def save_data_to_mysql(data):
    """保存数据到MySQL."""
    urls = data["urls"]
    url_texts_dict = data["url_texts_dict"]
    url_self_ref_dict = data["url_self_ref_dict"]
    url_refs = data["url_refs"]

    connection = connect_mysql()
    cursor = connection.cursor()

    url_id_dict = {}  # url => id
    word_id_dict = {}  # word => id

    try:
        for url in urls:
            print("DEBUG>>> handling {}".format(url))
            if url not in url_id_dict.keys():
                url_id_dict[url] = _save_url(connection, cursor, url, " ".join(url_texts_dict[url]))

            words = url_texts_dict[url]
            for i in range(len(words)):
                word = words[i]
                if word not in word_id_dict.keys():
                    word_id_dict[word] = _save_word(connection, cursor, word)
                _save_url_word(connection, cursor, url_id_dict[url], word_id_dict[word], i)

        for url_ref in url_refs:
            print("DEBUG>>> handling {}".format(url_ref))
            from_url = url_ref['from']
            if from_url not in url_id_dict.keys():
                url_id_dict[from_url] = _save_url(connection, cursor, from_url, "")
            from_url_id = url_id_dict[from_url]
            to_url = url_ref['to']
            if to_url not in url_id_dict.keys():
                url_id_dict[to_url] = _save_url(connection, cursor, to_url, "")
            to_url_id = url_id_dict[to_url]
            text = url_ref['text']
            link_id = _save_link(connection, cursor, from_url_id, to_url_id)
            for word in text:
                if word not in word_id_dict.keys():
                    word_id_dict[word] = _save_word(connection, cursor, word)
                _save_link_word(connection, cursor, link_id, word_id_dict[word])

    except Exception as e:
        print(e)
        connection.rollback()
        raise e
    finally:
        connection.close()


def _save_url(connection, cursor, url, content):
    """
    保存URL数据.
    :param connection:
    :param cursor:
    :param url:
    :param content:
    :return:
    """
    sql_str = "INSERT INTO url(url, content) VALUES ('%s', '%s')" % (url, content)
    try:
        cursor.execute(sql_str)
        connection.commit()
        return cursor.lastrowid
    except Exception as e:
        print(e)
        connection.rollback()
        raise e


def _save_word(connection, cursor, word):
    """
    保存单词数据.
    :param connection:
    :param cursor:
    :param word:
    :return:
    """
    sql_str = "INSERT INTO word(word) VALUES ('%s')" % word
    try:
        cursor.execute(sql_str)
        connection.commit()
        return cursor.lastrowid
    except Exception as e:
        print(e)
        connection.rollback()
        raise e


def _save_link(connection, cursor, from_url_id, to_url_id):
    """
    保存链接数据.
    :param connection:
    :param cursor:
    :param from_url_id:
    :param to_url_id:
    :return:
    """
    sql_str = "INSERT INTO link(from_url_id,to_url_id) VALUES ('%d', '%d')" % (from_url_id, to_url_id)
    try:
        cursor.execute(sql_str)
        connection.commit()
        return cursor.lastrowid
    except Exception as e:
        print(e)
        connection.rollback()
        raise e


def _save_url_word(connection, cursor, url_id, word_id, location):
    """
    保存URL和单词关联数据.
    :param connection:
    :param cursor:
    :param url_id:
    :param word_id:
    :param location:
    :return:
    """
    sql_str = "INSERT INTO url_word(url_id, word_id, location) VALUES ('%d', '%d', '%d')" % (url_id, word_id, location)
    try:
        cursor.execute(sql_str)
        connection.commit()
    except Exception as e:
        print(e)
        connection.rollback()
        raise e


def _save_link_word(connection, cursor, link_id, word_id):
    """
    保存链接和单词关联数据.
    :param connection:
    :param cursor:
    :param link_id:
    :param word_id:
    :return:
    """
    sql_str = "INSERT INTO link_word(link_id,word_id) VALUES ('%d', '%d')" % (link_id, word_id)
    try:
        cursor.execute(sql_str)
        connection.commit()
    except Exception as e:
        print(e)
        connection.rollback()
        raise e


def search_word_location_on_url(query=None):
    """
    搜索单词在URL中的位置.
    :param query:
    :return: [{"word": "", url": "", "locations": [int]}]
    """
    connection = connect_mysql()
    cursor = connection.cursor()
    sql_str = """
    select w.id, w.word, u.id, u.url, group_concat(uw.location)
    from word w
        inner join url_word uw on w.id = uw.word_id
        inner join url u on u.id = uw.url_id
    where w.word in %s
    group by w.id, u.id
    """
    cursor.execute(sql_str, (tuple(query),))
    query_result = cursor.fetchall()
    rows = []
    for row in query_result:
        rows.append({"word": row[1], "url": row[3], "locations": sorted(map(int, row[4].split(",")))})
    return sorted(rows, key=lambda _row: _row["url"])


def search_word_url(query=None):
    connection = connect_mysql()
    cursor = connection.cursor()
    # 单词可以在同一URL页面中出现多次
    sql_str = """
        select w.word, u.id, u.url
        from word w
            inner join url_word uw on w.id = uw.word_id
            inner join url u on u.id = uw.url_id
        where w.word in %s
        group by w.word, u.id
        """
    cursor.execute(sql_str, (tuple(query),))
    query_result = cursor.fetchall()
    return [(word, url_id, url) for (word, url_id, url) in query_result]


def search_rank_by_word_frequency(query=None):
    """
    按词频排序.
    :param query:
    :return:
    """
    query_result = search_word_location_on_url(query)
    scores = {}
    for _row in query_result:
        scores[_row["url"]] = len(_row["locations"])
    normalized_scores = normalize(scores, small_is_better=False)
    return sorted([(score, url) for (url, score) in normalized_scores.items()],
                  key=lambda item: item[0],
                  reverse=True)


def search_rank_by_inbound_link(query=None):
    """
    按入链数排序.
    :param query:
    :return:
    """
    word_urls = search_word_url(query)
    url_dict = dict([(url_id, url) for (_, url_id, url) in word_urls])  # url_id => url
    url_inbound_count_dict = search_inbound_count(list(url_dict.keys()))  # url_id => count
    scores = dict([(url_dict[url_id], count) for (url_id, count) in url_inbound_count_dict.items()])
    print("DEBUG>>> url inbounds")
    pprint.pprint(scores)
    normalized_scores = normalize(scores)
    return sorted([(score, url) for (url, score) in normalized_scores.items()],
                  key=lambda item: item[0],
                  reverse=True)


def search_inbound_count(url_ids):
    connection = connect_mysql()
    cursor = connection.cursor()
    # 单词可以在同一URL页面中出现多次
    sql_str = """
    select l.to_url_id, count(l.id)
    from link l
    where l.to_url_id in %s
    group by l.to_url_id
    """
    cursor.execute(sql_str, (tuple(url_ids),))
    query_result = cursor.fetchall()
    result = dict([(url_id, count) for (url_id, count) in query_result])
    return result


def normalize(scores, small_is_better=False):
    """
    正则化评分.
    :param scores: {id: score}
    :type dict
    :param small_is_better:
    :return:
    """
    v_small = 0.00001
    if small_is_better:
        min_score = min(scores.values())
        return dict([(u, float(min_score) / max(v_small, l)) for (u, l) in scores.items()])
    else:
        max_score = max(scores.values())
        if max_score == 0: max_score = v_small
        return dict([(u, float(c) / max_score) for (u, c) in scores.items()])


def calculate_page_rank(damping_factor=0.85, interations=20):
    """
    计算PageRank
    :return:
    """
    url_link_count_dict = search_link_count()
    page_ranks = dict([(url_id, 1.0) for url_id in url_link_count_dict.keys()])
    for i in range(interations):
        print("DEBUG>>> iteration {}: {}".format(i, page_ranks))
        for url_id in page_ranks.keys():
            page_ranks[url_id] = 0.15 + damping_factor * sum(
                [(page_ranks[_url_id] / _default_link_count(url_link_count_dict["link_count"]))
                 for (_url_id, url_link_count_dict) in url_link_count_dict.items()
                 if url_id != _url_id])
        # normalize
        max_pr = max(page_ranks.values())
        page_ranks = dict(map(lambda item: (item[0], float(item[1]) / max_pr), page_ranks.items()))

    return sorted(
        [(pr, url_link_count_dict[url_id]["url"]) for (url_id, pr) in page_ranks.items()],
        key=lambda item: item[0],
        reverse=True)


def _default_link_count(input_value):
    if input_value == 0 or input_value is None:
        return 1
    else:
        return input_value


def search_link_count():
    """
    查询URL的链接数量
    :return:
    """
    connection = connect_mysql()
    cursor = connection.cursor()
    sql_str = """
    select v1.id, v1.url, v1.link_count, 
        case  when v2.inbound_link_count is null then 0 else v2.inbound_link_count end inbound_link_count
    from
        (
            select u.id, u.url, count(l.id) link_count
            from url u
                left join link l on (l.from_url_id = u.id or l.to_url_id = u.id)
            group by u.id
        ) v1
        left join 
        (
            select l.to_url_id url_id, count(l.id) inbound_link_count
            from link l
            group by l.to_url_id
        ) v2 on v1.id = v2.url_id
    """
    cursor.execute(sql_str)
    query_result = cursor.fetchall()
    result = dict([(url_id, {"url": url, "link_count": link_count, "inbound_link_count": inbound_link_count})
                   for (url_id, url, link_count, inbound_link_count) in query_result])
    return result


if __name__ == '__main__':
    data = parse_data()
    show_data(data)
    save_data_to_mysql(data)
    search_result = search_rank_by_word_frequency(["implicit", ])
    pprint.pprint(search_result)

    search_result = search_word_url(["implicit", ])
    pprint.pprint(search_result)

    search_result = search_rank_by_inbound_link(["implicit", ])
    pprint.pprint(search_result)

    pprint.pprint(calculate_page_rank())

# schema
#
# CREATE TABLE `link` (
#   `id` bigint(20) NOT NULL AUTO_INCREMENT,
#   `from_url_id` bigint(20) NOT NULL,
#   `to_url_id` bigint(20) NOT NULL,
#   PRIMARY KEY (`id`)
# ) ENGINE=InnoDB AUTO_INCREMENT=613 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
#
# CREATE TABLE `link_word` (
#   `link_id` bigint(20) NOT NULL,
#   `word_id` bigint(20) NOT NULL
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
#
# CREATE TABLE `url` (
#   `id` bigint(20) NOT NULL AUTO_INCREMENT,
#   `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
#   `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
#   PRIMARY KEY (`id`),
#   UNIQUE KEY `url_UNIQUE` (`url`)
# ) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
#
# CREATE TABLE `url_word` (
#   `url_id` bigint(20) NOT NULL,
#   `word_id` bigint(20) NOT NULL,
#   `location` int(11) NOT NULL
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
#
# CREATE TABLE `word` (
#   `id` bigint(20) NOT NULL AUTO_INCREMENT,
#   `word` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
#   PRIMARY KEY (`id`),
#   UNIQUE KEY `word_UNIQUE` (`word`)
# ) ENGINE=InnoDB AUTO_INCREMENT=5996 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
