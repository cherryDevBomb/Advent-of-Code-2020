package day_18;

import org.apache.commons.lang3.StringUtils;
import util.InputReader;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class OperationOrder {

    private static final String INPUT_FILE = "input_18.txt";

    private static final String ADD = "+";
    private static final String MULTIPLY = "*";
    private static final String PARENTHESIS_LEFT = "(";
    private static final String PARENTHESIS_RIGHT = ")";

    public static void main(String[] args) {
        new OperationOrder().doMath();
    }

    private void doMath() {
        List<List<String>> expressions = InputReader.readInputFile(INPUT_FILE).stream()
                .map(line -> Arrays.stream(line.split("")).filter(StringUtils::isNotBlank).collect(Collectors.toList()))
                .collect(Collectors.toList());

        long result = getSumOfResults(expressions, equalPrecedence());
        long resultAdvanced = getSumOfResults(expressions, advancedPrecedence());

        System.out.println("Sum of the results of evaluating the expressions: " + result);
        System.out.println("Sum of the results of evaluating the expressions with advanced math: " + resultAdvanced);
    }

    private long getSumOfResults(List<List<String>> expressions, BiFunction<String, String, Boolean> precedence) {
        return expressions.stream()
                .mapToLong(expression -> evaluateExpression(expression, precedence))
                .sum();
    }

    /**
     * Evaluate a mathematical expression that consist of addition (+), multiplication (*), and parentheses ((...)).
     * Just like normal math, parentheses indicate that the expression inside must be evaluated before it can be used by the surrounding expression.
     * Addition still finds the sum of the numbers on both sides of the operator, and multiplication still finds the product.
     * However, the rules of operator precedence have changed. Rather than evaluating multiplication before addition, the operators have the same precedence,
     * and are evaluated left-to-right regardless of the order in which they appear.
     *
     * @param expression mathematical expression represented as string tokens in infix notation
     * @return result of evaluation
     */
    private long evaluateExpression(List<String> expression, BiFunction<String, String, Boolean> precedence) {
        Stack<Long> values = new Stack<>();
        Stack<String> operators = new Stack<>();

        expression.forEach(token -> {
            if (StringUtils.isNumeric(token)) {
                values.push(Long.parseLong(token));
            } else if (PARENTHESIS_LEFT.equals(token)) {
                operators.push(token);
            } else if (PARENTHESIS_RIGHT.equals(token)) {
                while (!PARENTHESIS_LEFT.equals(operators.peek())) {
                    evaluateTopmost(values, operators);
                }
                operators.pop();
            } else {
                while (!operators.empty() && !PARENTHESIS_LEFT.equals(operators.peek()) && precedence.apply(token, operators.peek())) {
                    evaluateTopmost(values, operators);
                }
                operators.push(token);
            }
        });

        while (!operators.empty()) {
            evaluateTopmost(values, operators);
        }

        return values.pop();
    }

    /**
     * Pop the topmost operator from the operators stack, pop 2 topmost values from the values stack.
     * Calculate the result and push it back onto the values stack.
     *
     * @param values    the values stack
     * @param operators the operators stack
     */
    private void evaluateTopmost(Stack<Long> values, Stack<String> operators) {
        String operator = operators.pop();
        long val1 = values.pop();
        long val2 = values.pop();
        long result = getOperatorMap().get(operator).apply(val1, val2);
        values.push(result);
    }

    /**
     * Part 1: + and * have the same precedence
     *
     * @return BiFunction that denotes equal precedence for + and *
     */
    private BiFunction<String, String, Boolean> equalPrecedence() {
        return (op1, op2) -> true;
    }

    /**
     * Part 2: + has greater precedence than *
     *
     * @return BiFunction that denotes greater precedence for + than *
     */
    private BiFunction<String, String, Boolean> advancedPrecedence() {
        return (op1, op2) -> !ADD.equals(op1) || !MULTIPLY.equals(op2);
    }

    private Map<String, BinaryOperator<Long>> getOperatorMap() {
        Map<String, BinaryOperator<Long>> operatorMap = new HashMap<>();
        operatorMap.put(ADD, (a, b) -> a + b);
        operatorMap.put(MULTIPLY, (a, b) -> a * b);
        return operatorMap;
    }
}
