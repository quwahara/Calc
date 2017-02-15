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

    private Map<String, Interpreter.Variable> run() throws Exception {
        List<Token> tokens = lexer.init(text).tokenize();
        List<Token> blk = parser.init(tokens).block();
        return interpreter.init(blk).run();
    }

}