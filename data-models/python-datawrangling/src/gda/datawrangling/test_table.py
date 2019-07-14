# -*- coding: utf-8 -*-

"""
表格数据.
agate - tutorial: https://github.com/wireservice/agate/blob/1.6.1/tutorial.ipynb
"""
import unittest

import agate


class TestTableRepresentation(unittest.TestCase):
    def test_load(self):
        tester = agate.TypeTester(force={
            'last_name': agate.Text(),
            'first_name': agate.Text(),
            'age': agate.Number()
        })

        exonerations = agate.Table.from_csv('../../../data/exonerations-20150828.csv', column_types=tester)
        print(exonerations)  # 表的描述

    def test_aggregate(self):
        exonerations = agate.Table.from_csv('../../../data/exonerations-20150828.csv')
        print(exonerations.aggregate(agate.Count('false_confession', value=True)))

    def test_filtering(self):
        exonerations = agate.Table.from_csv('../../../data/exonerations-20150828.csv')
        print(len(exonerations))
        with_age = exonerations.where(lambda row: row['age'] is not None)
        print(len(with_age))

    def test_computing_new_columns(self):
        # 计算新的列
        exonerations = agate.Table.from_csv('../../../data/exonerations-20150828.csv')
        with_years_in_prison = exonerations.compute([
            ('years_in_prison', agate.Change('convicted', 'exonerated'))
        ])
        print(with_years_in_prison.aggregate(agate.Median('years_in_prison')))

    def test_slicing(self):
        exonerations = agate.Table.from_csv('../../../data/exonerations-20150828.csv')
        binned_ages = exonerations.bins('age', count=10, start=0, end=100)
        binned_ages.print_bars('age', 'Count', width=80)

    def test_grouping(self):
        exonerations = agate.Table.from_csv('../../../data/exonerations-20150828.csv')
        clean_state_data = exonerations.compute([
            ('federal', agate.Formula(agate.Boolean(),
                                      lambda row: row['state'].startswith('F-'))),
            ('state', agate.Formula(agate.Text(),
                                    lambda row: row['state'][2:] if row['state'].startswith('F-') else row['state']))
        ], replace=True)
        by_state = clean_state_data.group_by('state')
        state_totals = by_state.aggregate([('count', agate.Count())])
        sorted_totals = state_totals.order_by('count', reverse=True)
        sorted_totals.print_table(max_rows=10)

    def test_charting(self):
        exonerations = agate.Table.from_csv('../../../data/exonerations-20150828.csv')
        by_year_exonerated = exonerations.group_by('exonerated')
        counts = by_year_exonerated.aggregate([('count', agate.Count())])
        chart = counts.order_by('exonerated').line_chart('exonerated', 'count', path="line_chart.svg")
        print(chart)

    def test_join(self):
        table1 = agate.Table.from_csv('../../../data/data-text.csv')
        print(table1)
        table2 = agate.Table.from_csv('../../../data/countries.csv')
        print(table2)
        joined = table1.join(table2, 'Country', 'Country')
        joined.print_table()  # 查看数据


if __name__ == '__main__':
    unittest.main()
