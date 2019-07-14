# -*- coding: utf-8 -*-

"""
测试协同过滤.
@author: zhoujiagen
Created on 2017-11-01 10:34:56
"""

import json

from prettytable import PrettyTable
import pprint
from gda.algorithms.pci import WORKING_DIR

from gda.algorithms.pci.collaborative_filtering import sim_distance, sim_pearson, \
    top_match_users, get_recommendations, top_match, transfer_data, \
    get_sim_items, get_recommendations_by_item


def get_users(data):
    """
    获取数据集中的用户列表.
    :param data: {用户: {电影: 评分}}.
    :return: 用户列表.
    :raise None
    """
    users = []
    for user, _ in data.items():
        users.append(user)
    return users


def get_movies(data):
    """
    获取数据集中的电影列表.
    :param data: {用户: {电影: 评分}}.
    :return: 电影列表.
    :raise None
    """
    movies = set()
    for _, movie_score in data.items():
        for movie, _ in movie_score.items():
            movies.add(movie)
    return list(movies)


def show_data(data):
    """
    展示数据.
    :param data: {用户: {电影: 评分}}.
    :return: None
    :raise None
    """
    headers = ['User']
    movies = get_movies(data)
    headers.extend(movies)
    table = PrettyTable(headers)

    for user, movie_score in data.items():
        row = [user]
        for header_movie in movies:
            score = movie_score.get(header_movie, None)
            if score:
                row.append(score)
            else:
                row.append('N/A')
        table.add_row(row)

    print("原始数据:")
    print(table)
    print()


def show_sim_between_users(data, sim_func):
    """
    显示用户之间的相似度.
    :param data: {用户: {电影: 评分}}.
    :param sim_func: 相似度函数.
    :return: None
    :raise None
    """
    users = get_users(data)
    headers = ['Users']
    headers.extend(users)

    table = PrettyTable(headers)
    for user1 in users:
        row = [user1]
        for user2 in users:
            row.append(sim_func(data, user1, user2))
        table.add_row(row)

    print("用户之间的相似度[%s]:" % sim_func.__name__)
    print(table)
    print()


if __name__ == '__main__':
    # 用户对影片的评分
    with open(WORKING_DIR + "movie-critics.json") as f:
        movie_critics = json.loads(f.read())
    show_data(movie_critics)

    show_sim_between_users(movie_critics, sim_distance)
    show_sim_between_users(movie_critics, sim_pearson)

    print('用户Jack Matthews的最佳匹配用户:')
    pprint.pprint(top_match_users(movie_critics, 'Jack Matthews'))
    print()

    print('给用户Toby的推荐:')
    pprint.pprint(get_recommendations(movie_critics, 'Toby'))
    print()

    print('影片Superman Returns的相似影片:')
    t_movie_critics = transfer_data(movie_critics)
    pprint.pprint(top_match(t_movie_critics, 'Superman Returns'))
    print()
    print('影片Lady in the Water的相似影片:')
    t_movie_critics = transfer_data(movie_critics)
    pprint.pprint(top_match(t_movie_critics, 'Lady in the Water', sim_func=sim_distance))
    print()

    sim_items = get_sim_items(movie_critics)
    print('相似电影集合:')
    pprint.pprint(sim_items)
    print('给用户Toby的推荐:')
    pprint.pprint(get_recommendations_by_item(movie_critics, sim_items, 'Toby'))
