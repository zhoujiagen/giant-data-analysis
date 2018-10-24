import unittest

import pymysql
import pprint


def connect(host='127.0.0.1',
            port=3306,
            user='root',
            password='admin',
            database='tpc',
            charset='utf8'):
    return pymysql.connect(host=host,
                           port=port,
                           user=user,
                           password=password,
                           database=database,
                           charset=charset)


class TestDataMySQL(unittest.TestCase):
    def test_connection(self):
        connection = connect()
        self.assertIsNotNone(connection)

    def test_select(self):
        connection = connect()
        cursor = connection.cursor()
        sql_str = "SELECT * FROM C_Customer t WHERE t.C_FIRST = '%s'" % 'rGEuzlBGqq'
        cursor.execute(sql_str)
        rows = cursor.fetchall()
        self.assertIsNotNone(rows)
        pprint.pprint(rows)

    def test_insert(self):
        connection = connect()
        cursor = connection.cursor()
        sql_str = """
        INSERT INTO C_Warehouse(`W_ID`,`W_NAME`,`W_STREET1`,`W_STREET2`,`W_CITY`,`W_STATE`,`W_ZIP`,`W_TAX`,`W_YTD`) 
        VALUES('%s','%s','%s','%s','%s','%s','%s','%s','%s')
        """ % (3, "warehouse1", "street1", "street2", "city2", "NN", "zip2", 0.0, 0.0)
        try:
            cursor.execute(sql_str)
            connection.commit()
        except Exception as e:
            print(e)
            connection.rollback()
            raise e
        finally:
            cursor.close()
            connection.close()

    def test_insert_return_id(self):
        """
        REF: https://stackoverflow.com/questions/2548493/how-do-i-get-the-id-after-insert-into-mysql-database-with-python

        table schema:
        CREATE TABLE `AI_ID` (
            `id` int(11) NOT NULL AUTO_INCREMENT,
            `description` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
            PRIMARY KEY (`id`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
        """
        connection = connect()
        cursor = connection.cursor()
        sql_str = "INSERT INTO AI_ID(`description`) VALUES('%s')" % "hello"
        try:
            cursor.execute(sql_str)
            connection.commit()
            print(cursor.lastrowid)
        except Exception as e:
            print(e)
            connection.rollback()
            raise e
        finally:
            cursor.close()
            connection.close()

    def test_batch_insert(self):
        connection = connect()
        cursor = connection.cursor()
        to_insert_rows = [(98, "warehouse1", "street1", "street2", "city2", "NN", "zip2", 0.0, 0.0),
                          (99, "warehouse2", "street1", "street2", "city2", "NN", "zip2", 0.0, 0.0)]
        sql_str = """INSERT INTO C_Warehouse(`W_ID`,`W_NAME`,`W_STREET1`,`W_STREET2`,`W_CITY`,`W_STATE`,`W_ZIP`,`W_TAX`,`W_YTD`) 
                VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s)
                """
        try:
            cursor.executemany(sql_str, to_insert_rows)
            connection.commit()
        except Exception as e:
            print(e)
            connection.rollback()
            raise e
        finally:
            cursor.close()
            connection.close()

    def test_update(self):
        connection = connect()
        cursor = connection.cursor()
        sql_str = """UPDATE C_Warehouse 
        SET W_STREET1 = 'street1' 
        WHERE W_ID IN (3, 98, 99)"""
        try:
            cursor.execute(sql_str)
            connection.commit()
        except Exception as e:
            print(e)
            connection.rollback()
            raise e
        finally:
            cursor.close()
            connection.close()


if __name__ == '__main__':
    unittest.main()
