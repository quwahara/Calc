
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ParsrTest {

    String text;
    Lexer lexer = new Lexer();
    Parser parser = new Parser();
    List<Token> block;

    @Before
    public void setUp() throws Exception {
        text = "";
    }

    @Test
    public void empty() throws Exception {
        text = "";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(0, block.size());
    }

    @Test
    public void line1_token1_1() {
        text = "+";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void line1_token1_2() throws Exception {
        text = "0";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("0", block.get(0).paren());
    }

    @Test
    public void line1_token1_3() throws Exception {
        text = "a";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("a", block.get(0).paren());
    }

    @Test
    public void line1_token2() {
        text = "a +";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void line1_token3_1() throws Exception {
        text = "a = 0";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("(a = 0)", block.get(0).paren());
    }

    @Test
    public void line1_token3_2() throws Exception {
        text = "1 + 2";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("(1 + 2)", block.get(0).paren());
    }

    @Test
    public void line1_token4() {
        text = "a = 1 +";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void line1_token5_1() throws Exception {
        text = "a = 1 + 2";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("(a = (1 + 2))", block.get(0).paren());
    }

    @Test
    public void line1_token5_2() throws Exception {
        text = "a = b = 2";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("(a = (b = 2))", block.get(0).paren());
    }

    @Test
    public void line1_token6() {
        text = "a = 1 + 2 +";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void line1_token7_1() throws Exception {
        text = "a = 1 + 2 - 3";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("(a = ((1 + 2) - 3))", block.get(0).paren());
    }

    @Test
    public void line1_token7_2() throws Exception {
        text = "a = 1 * 2 - 3";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("(a = ((1 * 2) - 3))", block.get(0).paren());
    }

    @Test
    public void line1_token7_3() throws Exception {
        text = "a = 1 + 2 / 3";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("(a = (1 + (2 / 3)))", block.get(0).paren());
    }

    @Test
    public void line1_token9_3() throws Exception {
        text = "a = (1 + 2) / 3";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("(a = ((1 + 2) / 3))", block.get(0).paren());
    }

    @Test
    public void line1_expression1() throws Exception {
        text = "a = 1 + b = 2 - 4 + 5 * 6 / 7 * 8 / 9 - 10 / 12 * 13 / 14 * 15 + 16 - 17";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals(
                "(a = ((1 + b) = (((((2 - 4) + ((((5 * 6) / 7) * 8) / 9)) - ((((10 / 12) * 13) / 14) * 15)) + 16) - 17)))",
                block.get(0).paren());
    }

    @Test
    public void line1_println() throws Exception {
        text = "println(1)";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("(println (1))", block.get(0).paren());
    }

    @Test
    public void line1_unaryOperator1() throws Exception {
        text = "-1";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("-1", block.get(0).paren());
    }

    @Test
    public void line1_unaryOperator2() throws Exception {
        text = "-1 + 2";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("(-1 + 2)", block.get(0).paren());
    }

    @Test
    public void line1_unaryOperator3() throws Exception {
        text = "-(1 + 2)";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("-(1 + 2)", block.get(0).paren());
    }

    @Test
    public void func1() {
        text = "function";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void func2() {
        text = "function f";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void func3() {
        text = "function f(";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void func4() {
        text = "function f(a";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void func5() {
        text = "function f(a)";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void func6() {
        text = "function f(a) {";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void func7() throws Exception {
        text = "function f(a) {}";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals(""
                + "func \"function\"\n"
                + "[ident]\n"
                + "  ident \"f\"\n"
                + "[params]\n"
                + "  ident \"a\"\n"
                + "[block]\n"
                + "", block.get(0).indent(""));
    }

    @Test
    public void func8() throws Exception {
        text = "function f() {}";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals(""
                + "func \"function\"\n"
                + "[ident]\n"
                + "  ident \"f\"\n"
                + "[params]\n"
                + "[block]\n"
                + "", block.get(0).indent(""));
    }

    @Test
    public void func9() throws Exception {
        text = "function f(a, b) {}";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals(""
                + "func \"function\"\n"
                + "[ident]\n"
                + "  ident \"f\"\n"
                + "[params]\n"
                + "  ident \"a\"\n"
                + "  ident \"b\"\n"
                + "[block]\n"
                + "", block.get(0).indent(""));
    }

    @Test
    public void func10() throws Exception {
        text = "function f(a, b, c) {}";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals(""
                + "func \"function\"\n"
                + "[ident]\n"
                + "  ident \"f\"\n"
                + "[params]\n"
                + "  ident \"a\"\n"
                + "  ident \"b\"\n"
                + "  ident \"c\"\n"
                + "[block]\n"
                + "", block.get(0).indent(""));
    }
    
    @Test
    public void invoke1() {
        text = "f(";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void invoke2() {
        text = "f(a";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void invoke3() {
        text = "f(a,";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void invoke4() {
        text = "f(a,b";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void invoke5() {
        text = "f(a,b,";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void invoke6() {
        text = "f(a,b,c";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void invoke7() throws Exception {
        text = "f()";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals(""
                + "paren \"(\"\n"
                + "[left]\n"
                + "  ident \"f\"\n"
                + "[params]\n"
                + "", block.get(0).indent(""));
    }
    
    @Test
    public void invoke8() throws Exception {
        text = "f(a)";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals(""
                + "paren \"(\"\n"
                + "[left]\n"
                + "  ident \"f\"\n"
                + "[params]\n"
                + "  ident \"a\"\n"
                + "", block.get(0).indent(""));
    }

    @Test
    public void invoke9() throws Exception {
        text = "f(a,b)";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        System.out.println(block.get(0).indent(""));
        assertEquals(""
                + "paren \"(\"\n"
                + "[left]\n"
                + "  ident \"f\"\n"
                + "[params]\n"
                + "  ident \"a\"\n"
                + "  ident \"b\"\n"
                + "", block.get(0).indent(""));
    }

    @Test
    public void invoke10() throws Exception {
        text = "f(a,b,c)";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        System.out.println(block.get(0).indent(""));
        assertEquals(""
                + "paren \"(\"\n"
                + "[left]\n"
                + "  ident \"f\"\n"
                + "[params]\n"
                + "  ident \"a\"\n"
                + "  ident \"b\"\n"
                + "  ident \"c\"\n"
                + "", block.get(0).indent(""));
    }

}
