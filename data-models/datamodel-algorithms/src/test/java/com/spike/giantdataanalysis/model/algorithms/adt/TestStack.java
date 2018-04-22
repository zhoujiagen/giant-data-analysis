package com.spike.giantdataanalysis.model.algorithms.adt;

import org.junit.Test;

import com.google.common.base.Preconditions;

public class TestStack {

  @Test
  public void example() {
    Stack<Integer> q = new Stack<>();

    q.push(1);
    q.push(2);
    q.push(3);
    q.push(4);
    q.push(5);

    int N = q.size();
    for (int i = 0; i < N; i++) {
      System.out.print(q.pop() + " ");
    }
    System.out.println();

    // OUT
    // 5 4 3 2 1
  }

  /**
   * Dijkstra的双栈算数表达式求值.
   * <p>
   * 细节:
   * 
   * <pre>
   * 表达式由括号, 运算符和操作数(数字)组成.
   * 
   * 实现中使用了两个栈: 操作数栈, 运算符栈.
   * 
   * 从左至右依次入栈:
   * (1) 将操作数压入操作数栈;
   * (2) 将运算符压入运算符栈;
   * (3) 忽略左括号;
   * (4) 遇到右括号时, 弹出一个运算符, 弹出所需数量的操作数, 将计算结果压入操作数栈.
   * </pre>
   */
  @Test
  public void arithmetic_expression_evaluation() {
    // (1+((2+3)*(4*5)))
    String[] expression = "( 1 + ( ( 2 + 3 ) * ( 4 * 5 ) ) )".split("\\s");
    this.evaluate(expression); // OUT: 101.0

    // ((1+sqrt(5.0))/2.0)
    expression = "( ( 1 + sqrt ( 5.0 ) ) / 2.0 )".split("\\s");
    this.evaluate(expression); // OUT: 1.618033988749895
  }

  private void evaluate(String[] expression) {
    Preconditions.checkArgument(expression != null && expression.length > 0);

    Stack<String> ops = new Stack<>(); // 运算符栈
    Stack<Double> vals = new Stack<>(); // 操作数栈

    for (String e : expression) {
      switch (e) {
      case "(":
        break;
      case "+":
      case "-":
      case "*":
      case "/":
      case "sqrt":
        ops.push(e);
        break;
      case ")": {
        String op = ops.pop();
        double v = vals.pop();
        switch (op) {
        case "+":
          v = vals.pop() + v;
          break;
        case "-":
          v = vals.pop() - v;
          break;
        case "*":
          v = vals.pop() * v;
          break;
        case "/":
          v = vals.pop() / v;
          break;
        case "sqrt":
          v = Math.sqrt(v);
          break;
        default:
          break;
        }
        vals.push(v); // 计算结果压入操作数栈
        break;
      }
      default:
        vals.push(Double.parseDouble(e));
        break;
      }
    }

    System.out.println(vals.pop()); // 最终计算结果
  }
}
