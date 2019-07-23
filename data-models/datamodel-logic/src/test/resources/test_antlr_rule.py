# -*- coding: utf-8 -*-

"""

@author: zhoujiagen
Created on 15/07/2019 10:16 AM
"""


def construct_test(rule_file_path):
    """构造测试方法."""
    with open(rule_file_path) as f:
        for rule_raw in f:
            rule = rule_raw[0:-1]
            rule_capitalize = rule[0].upper() + rule[1:]

            print("""
            @Test public void test{}() {{
              final String input = "";
              MySqlParser parser = this.constructParser(input);

              MySqlParserVisitor<Object> visitor = new RelationalAlgebraMySqlParserVisitor();
              {}Context context = parser.{}();
              Object {} = visitor.visit{}(context);
              System.out.print({});
            }}
            """.format(rule_capitalize, rule_capitalize, rule, rule, rule_capitalize, rule))


def construct_factory_methods(rule_file_path):
    """构造工厂中方法."""
    with open(rule_file_path) as f:
        for rule_raw in f:
            rule = rule_raw[0:-1]
            rule_capitalize = rule[0].upper() + rule[1:]

            print("""public static {} make{}() {{ return new {}(); }}""".format(rule_capitalize, rule_capitalize,
                                                                                rule_capitalize))


def construct_group_rule_branches(rule_file_path):
    """构造规则分支检测."""
    index = 0
    with open(rule_file_path) as f:
        for rule_raw in f:
            rule = rule_raw[0:-1]
            rule_capitalize = rule[0].upper() + rule[1:]
            #

            # for factory
            # print("""public static {} make{}() {{ return new {}(); }}""".format(rule_capitalize, rule_capitalize, rule_capitalize))
            #
            # for group rules
            branch = """else if (ctx.{}() != null) {{      
                    return this.visit{}(ctx.{}());
                }} """.format(rule, rule_capitalize, rule)
            if index == 0:
                branch = branch[5:]
            print(branch)
            index += 1

    print("""else {
              throw ParserError.make(ctx);
            }""")


if __name__ == '__main__':
    # root
    # sqlStatements
    # sqlStatement
    # emptyStatement
    # ddlStatement
    # ...
    file_path = "/Users/zhoujiagen/Downloads/rule.txt"
    construct_group_rule_branches(file_path)
