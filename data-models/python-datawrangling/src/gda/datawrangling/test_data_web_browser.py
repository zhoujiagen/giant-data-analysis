import unittest

"""
通过Web浏览器解析数据.

selenium: https://selenium-python.readthedocs.io/
ghost.py: https://ghost-py.readthedocs.io/en/latest/
    ghost.py requires PySide2 Qt5 bindings. - all huge files!!!
"""


class TestDataWebBrowser(unittest.TestCase):
    def test_Selenium(self):
        from selenium import webdriver
        # download 'chromedriver': https://sites.google.com/a/chromium.org/chromedriver/home
        browser = webdriver.Chrome()

        # load page
        browser.get("http://localhost:9999/files/archive/spec/2.12/")
        browser.set_page_load_timeout(1)

        # inspect page elements
        links = [(a.text, a.get_property("href")) for a in browser.find_elements_by_tag_name("a")]
        import pprint
        pprint.pprint(links)
        browser.maximize_window()

        # wait
        import selenium.webdriver.support.wait as w
        import time
        browser_wait = w.WebDriverWait(browser, timeout=10)
        browser_wait.until(lambda web_driver: (time.sleep(5), True))
        browser.close()

    def test_GhostPy(self):
        pass


if __name__ == '__main__':
    unittest.main()
