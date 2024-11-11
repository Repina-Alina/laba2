import java.util.HashMap;
import java.util.Scanner;

/**
 * Класс для выполнения математических операций над выражениями, содержащими числа и переменные.
 * Позволяет вычислять значения выражений, используя заданные переменные.
 */
public class Calculator {
    private final HashMap<String, Double> variables = new HashMap<>();
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Оценивает математическое выражение и возвращает его значение.
     * @param expression математическое выражение в виде строки, которое необходимо оценить
     * @return значение оцененного выражения
     * @throws Exception если возникают ошибки при оценке, например, если присутсвуют неверные символы
     */
    public double evaluate(String expression) throws Exception {
        expression = removeWhitespace(expression);
        if (expression.isEmpty()){
            throw new Exception("Выражение не может быть пустым");
        }
        return parseExpression(expression);
    }

    /**
     * Удаляет все пробелы из выражения.
     * @param expression математическое выражение, из которого необходимо удалить пробелы
     * @return выражение без пробелов
     */
    private String removeWhitespace(String expression) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i<expression.length(); i++) {
            char c = expression.charAt(i);
            if (c!= ' ') {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Парсит и вычисляет значение математического выражения.
     * @param expression математическое выражение в виде строки
     * @return численное значение оцененного выражения
     * @throws Exception если возникают ошибки при парсинге или неверные символы
     */
    private double parseExpression(String expression) throws Exception {
        double[] values = new double[expression.length()];
        char[] operators = new char[expression.length()];
        int valueIndex = 0;
        int operatorIndex = 0;

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (isDigit(c) || c == '.') {
                StringBuilder number = new StringBuilder();
                while (i < expression.length() && (isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    number.append(expression.charAt(i++));
                }
                values[valueIndex++] = Double.parseDouble(number.toString());
                i--;
            } else if (isLetter(c)) {
                StringBuilder variableName = new StringBuilder();
                while (i < expression.length() && isLetter(expression.charAt(i))) {
                    variableName.append(expression.charAt(i++));
                }
                String varName = variableName.toString();
                if (!variables.containsKey(varName)) {
                    double value = getVariableValue(varName);
                    variables.put(varName,value);
                }
                values[valueIndex++] = variables.get(varName);
                i--;
            } else if (c == '(') {
                operators[operatorIndex++] = c;
            } else if (c == ')') {
                while (operatorIndex > 0 && operators[operatorIndex - 1] != '(') {
                    valueIndex--;
                    values[valueIndex - 1] = applyOperation(operators[--operatorIndex], values[valueIndex - 1], values[valueIndex]);
                }
                operatorIndex--;
            } else if (isOperator(c)) {
                while (operatorIndex > 0 && precedence(c) <= precedence(operators[operatorIndex - 1])) {
                    valueIndex--;
                    values[valueIndex - 1] = applyOperation(operators[--operatorIndex], values[valueIndex - 1], values[valueIndex]);
                }
                operators[operatorIndex++] = c;
            } else {
                throw new Exception("Неверный символ: " + c);
            }
        }
        while (operatorIndex > 0) {
            valueIndex--;
            values[valueIndex - 1] = applyOperation(operators[--operatorIndex], values[valueIndex - 1], values[valueIndex]);
        }
        return values[0];
    }

    /**
     * Проверяет, является ли символ цифрой.
     * @param c символ, который необходимо проверить
     * @return true, если символ является цифрой, иначе false
     */
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * Проверяет является ли символ буквой (переменной).
     * @param c символ, который необходимо проверить
     * @return true, если символ является цифрой, иначе false
     */
    private boolean isLetter (char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * Проверяет является ли символ оператором.
     * @param c символ, который необходимо проверить
     * @return true, если символ является цифрой, иначе false
     */
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    /**
     * Возвращает приоритет оператора.
     * @param operator символ оператора
     * @return целочисленный приоритет оператора, где более высокий номер означает более высокий приоритет
     */
    private int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }

    /**
     * Применяет оператор к двум числовым значениям и возвращает результат.
     * @param operator оператор, который нужно применить
     * @param a первое значение
     * @param b второе значение
     * @return результат применения оператора к a и b
     * @throws Exception если возникает ошибка при выполнении операции
     */
    private double applyOperation(char operator, double a, double b) throws Exception {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) throw new Exception("Деление на ноль");
                return a / b;
            default:
                throw new Exception("Неверный оператор: " + operator);
        }
    }

    /**
     * Устанавливает значение переменной в калькуляторе.
     * Добавляет переменную с указанным именем и значением в хранилище переменных.
     * @param varName имя переменной, которую нужно установить
     * @param value значение, которое будет присвоено переменной
     */
    public void setVariable(String varName, double value) {
        variables.put(varName, value);
    }

    /**
     * Получает значение переменной от пользователя.
     * Запрашивает у пользователя ввод значение для переменной с указанным именем.
     * @param varName имя переменной, для которой нужно получить значение
     * @return значение переменной, введенное пользователем
     */
    private double getVariableValue(String varName) {
        System.out.print("Введите значение переменной '" + varName + "': ");
        return scanner.nextDouble();
    }
}
