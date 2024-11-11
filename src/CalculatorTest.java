import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    private Calculator calculator;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        calculator = new Calculator();
    }
    @org.junit.jupiter.api.Test
    void evaluateSimpleAddition() throws Exception {
        assertEquals(5.0, calculator.evaluate("2 + 3"), 0.001);
    }

    @org.junit.jupiter.api.Test
    void evaluateSimpleSubtraction() throws Exception {
        assertEquals(2.0, calculator.evaluate("5 - 3"), 0.001);
    }

    @org.junit.jupiter.api.Test
    void evaluateSimpleMultiplication() throws Exception {
        assertEquals(6.0, calculator.evaluate("2 * 3"), 0.001);
    }

    @org.junit.jupiter.api.Test
    void evaluateSimpleDivision() throws Exception {
        assertEquals(2.0, calculator.evaluate("6 / 3"), 0.001);
    }

    @org.junit.jupiter.api.Test
    void evaluateComplexExpression() throws Exception {
        assertEquals(14.0, calculator.evaluate("2 + 3 * 5 - 6 / 2"), 0.001);
    }

    @org.junit.jupiter.api.Test
    void evaluateWithParentheses() throws Exception {
        assertEquals(10.0, calculator.evaluate("(2 + 3) * 2"), 0.001);
    }

    @org.junit.jupiter.api.Test
    void evaluateWithVariables() throws Exception {
        calculator.setVariable("x", 10.0);
        assertEquals(10.0, calculator.evaluate("x"), 0.001);
        assertEquals(15.0, calculator.evaluate("x + 5"), 0.001);
    }

    @org.junit.jupiter.api.Test
    void evaluateInvalidExpression() {

        Exception exception = assertThrows(Exception.class, () -> {
            calculator.evaluate("2 + 3 * ?");
        });

        assertEquals("Неверный символ: ?", exception.getMessage());
    }
    @org.junit.jupiter.api.Test
    void evaluateEmptyExpression() {

        Exception exception = assertThrows(Exception.class, () -> {
            calculator.evaluate("");
        });
        assertEquals("Выражение не может быть пустым", exception.getMessage());
    }
}