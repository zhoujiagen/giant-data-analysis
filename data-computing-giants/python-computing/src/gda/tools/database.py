# -*- coding: utf-8 -*-

"""
数据库工具.
@author: zhoujiagen
Created on 03/11/2018 10:02 AM
"""
import pymysql


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
