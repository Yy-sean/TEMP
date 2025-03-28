// 文件保存为 ~/Desktop/Calculator.java

import java.util.Stack;
import java.util.Scanner;

public class Calculator {
    
    // 运算符优先级映射
    private static int precedence(char op) {
        return switch (op) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            default -> -1;
        };
    }

    // 中缀表达式转后缀表达式
    private static String infixToPostfix(String expr) {
        StringBuilder output = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        
        for (char c : expr.toCharArray()) {
            if (Character.isWhitespace(c)) continue;
            
            if (Character.isDigit(c) || c == '.') {
                output.append(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    output.append(' ').append(stack.pop());
                }
                stack.pop(); // 弹出左括号
            } else {
                output.append(' ');
                while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) {
                    output.append(stack.pop()).append(' ');
                }
                stack.push(c);
            }
        }
        
        while (!stack.isEmpty()) {
            output.append(' ').append(stack.pop());
        }
        return output.toString();
    }

    // 计算后缀表达式
    private static double evaluatePostfix(String expr) {
        Stack<Double> stack = new Stack<>();
        StringBuilder numBuffer = new StringBuilder();
        
        for (char c : expr.toCharArray()) {
            if (Character.isDigit(c) {
                numBuffer.append(c);
            } else if (c == '.') {
                numBuffer.append(c);
            } else if (Character.isWhitespace(c)) {
                if (numBuffer.length() > 0) {
                    stack.push(Double.parseDouble(numBuffer.toString()));
                    numBuffer.setLength(0);
                }
            } else {
                double b = stack.pop();
                double a = stack.pop();
                switch (c) {
                    case '+' -> stack.push(a + b);
                    case '-' -> stack.push(a - b);
                    case '*' -> stack.push(a * b);
                    case '/' -> {
                        if (b == 0) throw new ArithmeticException("Division by zero");
                        stack.push(a / b);
                    }
                }
            }
        }
        return stack.pop();
    }

    // 输入验证
    private static boolean validateInput(String input) {
        return input.matches("^[\\d\\+\\-*/()\\.\\s]+$");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Java命令行计算器 (输入 exit 退出)");
        
        while (true) {
            System.out.print(">>> ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            
            try {
                if (!validateInput(input)) {
                    throw new IllegalArgumentException("非法字符");
                }
                
                String postfix = infixToPostfix(input);
                double result = evaluatePostfix(postfix);
                System.out.printf("结果: %.2f%n", result);
                
            } catch (Exception e) {
                System.out.println("错误: " + e.getMessage());
            }
        }
        scanner.close();
    }
}