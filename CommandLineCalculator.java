import java.util.Scanner;
import java.util.Stack;

public class CommandLineCalculator {

    // 运算符优先级表
    private static final int ADD = 1;
    private static final int SUB = 1;
    private static final int MUL = 2;
    private static final int DIV = 2;
    private static final int LEFT_PAREN = 0;
    private static final int RIGHT_PAREN = -1;

    // 主方法
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("欢迎使用命令行计算器");
        System.out.println("输入 'quit' 退出程序");

        while (true) {
            System.out.print("请输入表达式: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("退出计算器");
                break;
            }

            try {
                double result = evaluateExpression(input);
                System.out.println("结果: " + result);
            } catch (Exception e) {
                System.out.println("错误: " + e.getMessage());
            }
        }

        scanner.close();
    }

    // 计算表达式
    private static double evaluateExpression(String expression) {
        Stack<Double> operandStack = new Stack<>(); // 存储操作数
        Stack<Character> operatorStack = new Stack<>(); // 存储运算符

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                // 处理数字（包括小数）
                double number = 0;
                boolean isDecimal = false;
                int decimalCount = 0;
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    if (expression.charAt(i) == '.') {
                        isDecimal = true;
                    } else {
                        if (isDecimal) {
                            number = number * 10 + (expression.charAt(i) - '0');
                            decimalCount++;
                            number /= Math.pow(10, decimalCount);
                        } else {
                            number = number * 10 + (expression.charAt(i) - '0');
                        }
                    }
                    i++;
                }
                i--; // 回退一个字符，因为循环多走了一步
                operandStack.push(number);
            } else if (c == '(') {
                // 左括号直接入栈
                operatorStack.push(c);
            } else if (c == ')') {
                // 右括号，弹出运算符直到遇到左括号
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    calculate(operandStack, operatorStack);
                }
                if (!operatorStack.isEmpty()) {
                    operatorStack.pop(); // 弹出左括号
                }
            } else if (isOperator(c)) {
                // 处理运算符
                while (!operatorStack.isEmpty() && getPriority(c) <= getPriority(operatorStack.peek())) {
                    calculate(operandStack, operatorStack);
                }
                operatorStack.push(c);
            }
        }

        // 计算剩余的运算符
        while (!operatorStack.isEmpty()) {
            calculate(operandStack, operatorStack);
        }

        return operandStack.pop();
    }

    // 判断是否是运算符
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    // 获取运算符优先级
    private static int getPriority(char operator) {
        switch (operator) {
            case '+':
                return ADD;
            case '-':
                return SUB;
            case '*':
                return MUL;
            case '/':
                return DIV;
            case '(':
                return LEFT_PAREN;
            case ')':
                return RIGHT_PAREN;
            default:
                return -1;
        }
    }

    // 执行一次运算
    private static void calculate(Stack<Double> operandStack, Stack<Character> operatorStack) {
        if (operandStack.size() < 2) {
            throw new RuntimeException("表达式格式不正确");
        }

        double b = operandStack.pop();
        double a = operandStack.pop();
        char operator = operatorStack.pop();

        double result;
        switch (operator) {
            case '+':
                result = a + b;
                break;
            case '-':
                result = a - b;
                break;
            case '*':
                result = a * b;
                break;
            case '/':
                if (b == 0) {
                    throw new RuntimeException("除数不能为零");
                }
                result = a / b;
                break;
            default:
                throw new RuntimeException("未知运算符: " + operator);
        }

        operandStack.push(result);
    }
}