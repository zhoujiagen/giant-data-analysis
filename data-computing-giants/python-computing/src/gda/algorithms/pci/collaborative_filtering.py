# -*- coding: utf-8 -*-

"""
协同过滤(Collaborative Filtering)
@author: zhoujiagen
Created on 2017-10-31 22:29:22
"""

from math import sqrt

from gda.tools.similarity import pearson


# ##################################################### 基于用户的协同过滤
def sim_distance(data, user1, user2):
    """
    返回用户之前的相似度 - 欧几里德距离.
    :param data: {用户: {电影: 评分}}.
    :param user1: 用户1的名称.
    :param user2: 用户2的名称.
    :return: user1与user2之间的相似度.
    :raise None
    """
    intersect_movies = {}  # user1/2均评过分的电影
    for movie in data[user1]:
        if movie in data[user2]:
            intersect_movies[movie] = 1

    count = len(intersect_movies)
    if count == 0:
        return 0

    # user1/2对同一电影评分的差值平方和
    sum_of_squares = sum([pow(data[user1][movie] - data[user2][movie], 2)
                          for movie in data[user1] if movie in data[user2]])

    return 1 / (1 + sqrt(sum_of_squares))


def sim_pearson(data, user1, user2):
    """
    返回用户之前的相似度 - 皮尔逊相关系数.
    :param data: {用户: {电影: 评分}}.
    :param user1: 用户1的名称.
    :param user2: 用户2的名称.
    :return: user1与user2之间的相似度.
    :raise None
    """
    intersect_movies = {}  # user1/2均评过分的电影
    for movie in data[user1]:
        if movie in data[user2]:
            intersect_movies[movie] = 1

    n = len(intersect_movies)
    if n == 0:
        return 1

    user1_scores = []
    user2_scores = []
    for movie in intersect_movies:
        user1_scores.append(data[user1][movie])
        user2_scores.append(data[user2][movie])
    return pearson(user1_scores, user2_scores)


def top_match(data, subject, n=5, sim_func=sim_pearson):
    """top_match_users的泛化."""
    sim_scores = [(sim_func(data, subject, other), other)
                  for other in data if other != subject]
    sim_scores.sort()
    sim_scores.reverse()
    return sim_scores[0:n]


def top_match_users(data, user, n=5, sim_func=sim_pearson):
    """
    计算与用户最为相似的用户列表.
    :param data: {用户: {电影: 评分}}.
    :param user: 用户名称.
    :param n: 要求返回的用户数量.
    :param sim_func: 相似度函数.
    :return: [(sim, user)]: 相似度函数意义下, 与用户最为相似的用户列表.
    :raise None
    """
    sim_scores = [(sim_func(data, user, other), other)
                  for other in data if other != user]
    sim_scores.sort()
    sim_scores.reverse()
    return sim_scores[0:n]


def get_recommendations(data, user, sim_func=sim_pearson):
    """
    给用户推荐影片.

    使用其他用户对影片评分的加权平均. 两个因素:
    (1) 其他用户对影片的评分;
    (2) 该用户与其他用户的相似度.
    :param data: {用户: {电影: 评分}}.
    :param user: 用户名称.
    :param sim_func: 相似度函数.
    :return: [(score, movie)]: 推荐的影片及预期评分.
    :raise None
    """

    # 用户对影片的评分因用户间的相似度而贡献的评分之和: {影片: 评分加权总分}
    sum_weighted_score = {}
    # 评价过影片的用户与该用户的相似度和: {影片: 相似度和}
    sum_sim_user = {}

    for other in data:
        if user == other:  # 考察其他用户
            continue

        sim_user = sim_func(data, user, other)  # 用户之间的相似度
        if sim_user <= 0:
            continue

        # 考察用户评价过, 该用户未评价过的影片
        for movie in data[other]:
            if movie not in data[user]:
                sum_weighted_score.setdefault(movie, 0)
                sum_weighted_score[movie] += sim_user * data[other][movie]

                sum_sim_user.setdefault(movie, 0)
                sum_sim_user[movie] += sim_user

    # 用户对影片的评分因用户间的相似度而贡献的评分之和
    # /
    # 对该影片评过分的用户与该用户的相似度的和
    rankings = [(weighted_score / sum_sim_user[movie], movie)
                for movie, weighted_score in sum_weighted_score.items()
                if sum_sim_user[movie] != 0]

    rankings.sort()
    rankings.reverse()

    return rankings


def transfer_data(data):
    """
    转换数据.
    :param data: {用户: {电影: 评分}}.
    :return: {电影: {用户: 评分}}.
    :raise None
    """
    result = {}
    for user, movie_score in data.items():
        for movie, _ in movie_score.items():
            result.setdefault(movie, {})
            result[movie][user] = data[user][movie]
    return result


# ##################################################### 基于物品的协同过滤


def get_sim_items(data, n=10, sim_func=sim_distance):
    """
    计算相似的物品集合.
    :param data: {用户: {电影: 评分}}.
    :type data: dict
    :param n: 相似物品的数量限制.
    :type n: int
    :param sim_func: 相似度函数.
    :type sim_func: function
    :return: {movie: [(sim_movies, movie')]}
    :raise None
    """
    result = {}

    movie_data = transfer_data(data)
    process_count = 0  # 处理数量计数
    for movie in movie_data:
        process_count += 1
        if process_count % 100 == 0:
            print("%d/%d" % (process_count, len(movie_data)))

        # 使用最佳匹配函数top_match
        match_movies = top_match(movie_data, movie, n=n, sim_func=sim_func)
        result[movie] = match_movies

    return result


def get_recommendations_by_item(data, sim_items, user):
    """
    基于物品的推荐.
    :param data: {用户: {电影: 评分}}.
    :param sim_items: 相似的物品集合{movie: [(sim_movies, movie')]}, 见get_sim_items().
    :param user: 用户名称.
    :type user: str
    :return: [(score, movie)]
    :raise None
    """
    user_scores = data[user]  # 该用户的所有电影评分
    sum_weighted_scores = {}  # 评过分的电影评分的电影相似性加权的和
    sum_sim_movies = {}  # 评过分的电影与未评过分的电影的相似性的和

    for movie, score in user_scores.items():
        for sim_movie, other_movie in sim_items[movie]:
            if other_movie in user_scores:  # 不处理已评分的电影
                continue

            sum_weighted_scores.setdefault(other_movie, 0)
            sum_weighted_scores[other_movie] += score * sim_movie

            sum_sim_movies.setdefault(other_movie, 0)
            sum_sim_movies[other_movie] += sim_movie

    # 用户已评分的电影因已评分与未评分电影的相似度而贡献的评分之和
    # /
    # 已评分与未评分电影的相似度之和
    rankings = [(score / sum_sim_movies[movie], movie)
                for movie, score in sum_weighted_scores.items()]
    rankings.sort()
    rankings.reverse()

    return rankings
