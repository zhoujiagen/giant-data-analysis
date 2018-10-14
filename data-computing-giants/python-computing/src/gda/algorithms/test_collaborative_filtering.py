# -*- coding: utf-8 -*-

'''
Created on 2017-11-01 10:34:56
测试协同过滤.
@author: zhoujiagen
'''


from prettytable import PrettyTable

from gda.algorithms.collaborative_filtering import sim_distance, sim_pearson, \
    top_match_users, get_recommendations, top_match, transfer_data, \
    get_sim_items, get_recommendations_by_item


# 用户对影片的评分
CRITICS = {
    'Lisa Rose':{
        'Lady in the Water': 2.5,
        'Snakes on a Plane': 3.5,
        'Just My Luck': 3.0,
        'Superman Returns': 3.5,
        'You, Me and Dupree': 2.5,
        'The Night Listener': 3.0,
    },
    'Gene Seymour':{
        'Lady in the Water': 3.0,
        'Snakes on a Plane': 3.5,
        'Just My Luck': 1.5,
        'Superman Returns': 5.0,
        'The Night Listener': 3.0,
        'You, Me and Dupree': 3.5,
    },
    'Michael Phillips':{
        'Lady in the Water': 2.5,
        'Snakes on a Plane': 3.0,
        'Superman Returns': 3.5,
        'The Night Listener': 4.0,
    },
    'Claudia Puig':{
        'Snakes on a Plane': 3.5,
        'Just My Luck': 3.0,
        'The Night Listener': 4.5,
        'Superman Returns': 4.0,
        'You, Me and Dupree': 2.5,
    },
    'Mick LaSalle':{
        'Lady in the Water': 3.0,
        'Snakes on a Plane': 4.0,
        'Just My Luck': 2.0,
        'Superman Returns': 3.0,
        'The Night Listener': 3.0,
        'You, Me and Dupree': 2.0,
    },
    'Jack Matthews':{
        'Lady in the Water': 3.0,
        'Snakes on a Plane': 4.0,
        'The Night Listener': 3.0,
        'Superman Returns': 5.0,
        'You, Me and Dupree': 3.5,
    },
    'Toby':{
        'Snakes on a Plane': 4.5,
        'You, Me and Dupree': 1.0,
        'Superman Returns': 4.0,
    }
}


def get_users(data):
    """获取数据集中的用户列表.

    Args:
        data: {用户: {电影: 评分}}.

    Returns:
        用户列表.

    Raises:
        None
    """
    users = []
    for user, _ in data.items():
        users.append(user)
    return users


def get_movies(data):
    """获取数据集中的电影列表.

    Args:
        data: {用户: {电影: 评分}}.

    Returns:
        电影列表.

    Raises:
        None
    """
    movies = set()
    for _, movie_score in data.items():
        for movie, _ in movie_score.items():
            movies.add(movie)
    return list(movies)


def show_data(data):
    """展示数据.

    Args:
        data: {用户: {电影: 评分}}.

    Returns:
        None

    Raises:
        None
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
    print


def show_sim_between_users(data, sim_func):
    """显式用户之间的相似度.

    Args:
        data: {用户: {电影: 评分}}.
        sim_func: 相似度函数.

    Returns:
        None

    Raises:
        None
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

    print("用户之间的相似度[%s]:" % sim_func.__name__) # sim_func.func_name
    print(table)
    print


if __name__ == '__main__':
    show_data(CRITICS)

    show_sim_between_users(CRITICS, sim_distance)
    show_sim_between_users(CRITICS, sim_pearson)

    print('用户Jack Matthews的最佳匹配用户:')
    print(top_match_users(CRITICS, 'Jack Matthews'))
    print

    print('给用户Toby的推荐:')
    print(get_recommendations(CRITICS, 'Toby'))
    print

    print('影片Superman Returns的相似影片:')
    TRANSFORMED_CRITICS = transfer_data(CRITICS)
    print(top_match(TRANSFORMED_CRITICS, 'Superman Returns'))
    print
    print('影片Lady in the Water的相似影片:')
    TRANSFORMED_CRITICS = transfer_data(CRITICS)
    print(top_match(TRANSFORMED_CRITICS, 'Lady in the Water', sim_func = sim_distance))
    print

    SIM_ITEMS = get_sim_items(CRITICS)
    print('相似电影集合:')
    print(SIM_ITEMS)
    print('给用户Toby的推荐:')
    print(get_recommendations_by_item(CRITICS, SIM_ITEMS, 'Toby'))
