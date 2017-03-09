import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class InterpreterTest {

    String text;
    Lexer lexer = new Lexer();
    Parser parser = new Parser();
    Interpreter interpreter = new Interpreter();
    Map<String, Interpreter.Variable> actual;

    List<Token> block;

    @Before
    public void setUp() throws Exception {
        text = "";
    }

    @Test
    public void testBody_empty() throws Exception {
        text = "";
        actual = run();
        assertEquals(0, actual.size());
    }

    @Test
    public void testBody_1() throws Exception {
        text = "a";
        actual = run();
        assertEquals(1, actual.size());
        assertEquals(0, (int) actual.get("a").value);
    }

    @Test
    public void testBody_2() throws Exception {
        text = "a = 1";
        actual = run();
        assertEquals(1, actual.size());
        assertEquals(1, (int) actual.get("a").value);
    }

    @Test
    public void testBody_3() throws Exception {
        text = "a = b = 1";
        actual = run();
        assertEquals(2, actual.size());
        assertEquals(1, (int) actual.get("a").value);
        assertEquals(1, (int) actual.get("b").value);
    }

    @Test
    public void testBody_4() throws Exception {
        text = "println(1)";
        actual = run();
        assertEquals(0, actual.size());
    }

    @Test
    public void testBody_5() throws Exception {
        try {
            text = "a = println(1)";
            actual = run();
            fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void testBody_6() throws Exception {
        try {
            text = "1 = 1";
            actual = run();
            fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void testBody_7() throws Exception {
        try {
            text = "println = 1";
            actual = run();
            fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void testBody_8() throws Exception {
        text = "println(a = 1 + 2 * 3)";
        actual = run();
        assertEquals(1, actual.size());
        assertEquals(7, (int) actual.get("a").value);
    }

    @Test
    public void testBody_9() throws Exception {
        try {
            text = "(a + 1) = 1";
            actual = run();
            fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void testBody_10() throws Exception {
        text = "a = (1 + 2) * 3";
        actual = run();
        assertEquals(1, actual.size());
        assertEquals(9, (int) actual.get("a").value);
    }

    @Test
    public void testBody_11() throws Exception {
        text = "a = +1";
        actual = run();
        assertEquals(1, actual.size());
        assertEquals(1, (int) actual.get("a").value);
    }

    @Test
    public void testBody_12() throws Exception {
        text = "a = -1";
        actual = run();
        assertEquals(1, actual.size());
        assertEquals(-1, (int) actual.get("a").value);
    }

    @Test
    public void testBody_13() throws Exception {
        text = "a = +1 + 2";
        actual = run();
        assertEquals(1, actual.size());
        assertEquals(3, (int) actual.get("a").value);
    }

    @Test
    public void testBody_14() throws Exception {
        text = "a = -1 + 2";
        actual = run();
        assertEquals(1, actual.size());
        assertEquals(1, (int) actual.get("a").value);
    }

    @Test
    public void testBody_15() throws Exception {
        text = "a = +(1 + 2)";
        actual = run();
        assertEquals(1, actual.size());
        assertEquals(3, (int) actual.get("a").value);
    }

    @Test
    public void testBody_16() throws Exception {
        text = "a = -(1 + 2)";
        actual = run();
        assertEquals(1, actual.size());
        assertEquals(-3, (int) actual.get("a").value);
    }

    @Test
    public void testBody_17() throws Exception {
        text = "a = 3 * -(1 + 2)";
        actual = run();
        assertEquals(1, actual.size());
        assertEquals(-9, (int) actual.get("a").value);
    }

    @Test
    public void testBody_18() throws Exception {
        text += "v = 0";
        text += "function addV(num) {";
        text += "  v = v + num";
        text += "}";
        text += "addV(3)";
        actual = run();
        assertEquals(2, actual.size());
        assertEquals(3, (int) actual.get("v").value);
    }

    @Test
    public void testBody_19() throws Exception {
        text += "v = 0";
        text += "function f() {";
        text += "  v = 1";
        text += "}";
        text += "f()";
        actual = run();
        assertEquals(1, actual.size());
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_20() throws Exception {
        text += "v = 0";
        text += "function f(a, b) {";
        text += "  v = a + b";
        text += "}";
        text += "f(1, 2)";
        actual = run();
        assertEquals(3, actual.size());
        assertEquals(3, (int) actual.get("v").value);
    }

    @Test
    public void testBody_21() throws Exception {
        text += "v = 0";
        text += "function f(a, b, c) {";
        text += "  v = a + b + c";
        text += "}";
        text += "f(1, 2, 3)";
        actual = run();
        assertEquals(4, actual.size());
        assertEquals(6, (int) actual.get("v").value);
    }

    @Test
    public void testBody_22() throws Exception {
        text += "function f(a, b, c) {";
        text += "  return a + b + c";
        text += "}";
        text += "v = f(1, 2, 3)";
        actual = run();
        assertEquals(4, actual.size());
        assertEquals(6, (int) actual.get("v").value);
    }

    @Test
    public void testBody_23() throws Exception {
        text += "function f(a) {";
        text += "  b = a + 1";
        text += "  return b";
        text += "}";
        text += "v = f(1)";
        actual = run();
        assertEquals(3, actual.size());
        assertEquals(2, (int) actual.get("v").value);
    }

    @Test
    public void testBody_24() throws Exception {
        text += "function f(a) {";
        text += "  b = a + 1";
        text += "  c = b + 1";
        text += "  return c";
        text += "}";
        text += "v = f(1)";
        actual = run();
        assertEquals(4, actual.size());
        assertEquals(3, (int) actual.get("v").value);
    }

    @Test
    public void testBody_25() throws Exception {
        text += "function f(a) {";
        text += "  b = a + 1";
        text += "  c = b + 1";
        text += "  return c";
        text += "  d = c + 1";
        text += "}";
        text += "v = f(1)";
        actual = run();
        assertEquals(4, actual.size());
        assertEquals(3, (int) actual.get("v").value);
    }

    @Test
    public void testBody_26() throws Exception {
        text += "function f1(a) {";
        text += "  return a + 1";
        text += "}";
        text += "function f2(b) {";
        text += "  return f1(b) + 1";
        text += "}";
        text += "v = f2(1)";
        actual = run();
        assertEquals(3, (int) actual.get("v").value);
    }

    @Test
    public void testBody_27() throws Exception {
        text += "v = 0";
        text += "function inc() {";
        text += "  v = v + 1";
        text += "  return";
        text += "}";
        text += "function incX2() {";
        text += "  inc()";
        text += "  inc()";
        text += "  return";
        text += "}";
        text += "incX2()";
        actual = run();
        assertEquals(2, (int) actual.get("v").value);
    }

    private Map<String, Interpreter.Variable> run() throws Exception {
        List<Token> tokens = lexer.init(text).tokenize();
        List<Token> blk = parser.init(tokens).block();
        return interpreter.init(blk).run();
    }

}
