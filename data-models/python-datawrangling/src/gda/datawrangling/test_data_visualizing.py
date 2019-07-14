# -*- coding: utf-8 -*-

"""
数据可视化.

pygal: http://www.pygal.org/en/stable/index.html
"""
import unittest
import pygal


class TestDataVisualizing(unittest.TestCase):
    def test_world_map(self):
        worldmap_chart = pygal.maps.world.World()
        worldmap_chart.title = 'Minimum deaths by capital punishement (source: Amnesty International)'
        worldmap_chart.add('In 2012', {
            'af': 14,
            'bd': 1,
            'by': 3,
            'cn': 1000,
            'gm': 9,
            'in': 1,
            'ir': 314,
            'iq': 129,
            'jp': 7,
            'kp': 6,
            'pk': 1,
            'ps': 6,
            'sa': 79,
            'so': 6,
            'sd': 5,
            'tw': 6,
            'ae': 1,
            'us': 43,
            'ye': 28
        })
        worldmap_chart.render_in_browser()


if __name__ == '__main__':
    unittest.main()
