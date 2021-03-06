
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class LexerTest {

    Lexer lxr;
    List<Token> act;

    @Before
    public void before() {
        lxr = new Lexer();
    }

    @Test
    public void testTokenize_empty() throws Exception {
        act = lxr.init("").tokenize();
        assertEquals(0, act.size());
    }

    @Test
    public void testTokenize_empty2() throws Exception {
        act = lxr.init(" ").tokenize();
        assertEquals(0, act.size());
    }

    @Test
    public void testTokenize_sign1() throws Exception {
        act = lxr.init("+").tokenize();
        assertEquals(1, act.size());
        assertEquals("sign", act.get(0).kind);
        assertEquals("+", act.get(0).value);
    }

    @Test
    public void testTokenize_sign2() throws Exception {
        act = lxr.init("=").tokenize();
        assertEquals(1, act.size());
        assertEquals("sign", act.get(0).kind);
        assertEquals("=", act.get(0).value);
    }

    @Test
    public void testTokenize_sign3() throws Exception {
        act = lxr.init("==").tokenize();
        assertEquals(1, act.size());
        assertEquals("sign", act.get(0).kind);
        assertEquals("==", act.get(0).value);
    }

    @Test
    public void testTokenize_sign4() throws Exception {
        act = lxr.init("!").tokenize();
        assertEquals(1, act.size());
        assertEquals("sign", act.get(0).kind);
        assertEquals("!", act.get(0).value);
    }

    @Test
    public void testTokenize_sign5() throws Exception {
        act = lxr.init("!=").tokenize();
        assertEquals(1, act.size());
        assertEquals("sign", act.get(0).kind);
        assertEquals("!=", act.get(0).value);
    }

    @Test
    public void testTokenize_sign6() throws Exception {
        act = lxr.init("<").tokenize();
        assertEquals(1, act.size());
        assertEquals("sign", act.get(0).kind);
        assertEquals("<", act.get(0).value);
    }

    @Test
    public void testTokenize_sign7() throws Exception {
        act = lxr.init("<=").tokenize();
        assertEquals(1, act.size());
        assertEquals("sign", act.get(0).kind);
        assertEquals("<=", act.get(0).value);
    }

    @Test
    public void testTokenize_sign8() throws Exception {
        act = lxr.init(">-").tokenize();
        assertEquals(2, act.size());
        assertEquals("sign", act.get(0).kind);
        assertEquals(">", act.get(0).value);
        assertEquals("sign", act.get(1).kind);
        assertEquals("-", act.get(1).value);
    }

    @Test
    public void testTokenize_sign9() throws Exception {
        act = lxr.init(">=").tokenize();
        assertEquals(1, act.size());
        assertEquals("sign", act.get(0).kind);
        assertEquals(">=", act.get(0).value);
    }

    @Test
    public void testTokenize_sign10() throws Exception {
        act = lxr.init("&-").tokenize();
        assertEquals(2, act.size());
        assertEquals("sign", act.get(0).kind);
        assertEquals("&", act.get(0).value);
        assertEquals("sign", act.get(1).kind);
        assertEquals("-", act.get(1).value);
    }

    @Test
    public void testTokenize_sign11() throws Exception {
        act = lxr.init("&&").tokenize();
        assertEquals(1, act.size());
        assertEquals("sign", act.get(0).kind);
        assertEquals("&&", act.get(0).value);
    }

    @Test
    public void testTokenize_sign12() throws Exception {
        act = lxr.init("|-").tokenize();
        assertEquals(2, act.size());
        assertEquals("sign", act.get(0).kind);
        assertEquals("|", act.get(0).value);
        assertEquals("sign", act.get(1).kind);
        assertEquals("-", act.get(1).value);
    }

    @Test
    public void testTokenize_sign13() throws Exception {
        act = lxr.init("||").tokenize();
        assertEquals(1, act.size());
        assertEquals("sign", act.get(0).kind);
        assertEquals("||", act.get(0).value);
    }

    @Test
    public void testTokenize_symbol() throws Exception {
        act = lxr.init(",").tokenize();
        assertEquals(1, act.size());
        assertEquals("symbol", act.get(0).kind);
        assertEquals(",", act.get(0).value);
    }

    @Test
    public void testTokenize_paren() throws Exception {
        act = lxr.init("()").tokenize();
        assertEquals(2, act.size());
        assertEquals("paren", act.get(0).kind);
        assertEquals("(", act.get(0).value);
        assertEquals("paren", act.get(1).kind);
        assertEquals(")", act.get(1).value);
    }

    @Test
    public void testTokenize_bracket() throws Exception {
        act = lxr.init("[]").tokenize();
        assertEquals(2, act.size());
        assertEquals("bracket", act.get(0).kind);
        assertEquals("[", act.get(0).value);
        assertEquals("bracket", act.get(1).kind);
        assertEquals("]", act.get(1).value);
    }

    @Test
    public void testTokenize_curly() throws Exception {
        act = lxr.init("{}").tokenize();
        assertEquals(2, act.size());
        assertEquals("curly", act.get(0).kind);
        assertEquals("{", act.get(0).value);
        assertEquals("eob", act.get(1).kind);
        assertEquals("}", act.get(1).value);
    }

    @Test
    public void testTokenize_digit() throws Exception {
        act = lxr.init("0").tokenize();
        assertEquals(1, act.size());
        assertEquals("digit", act.get(0).kind);
        assertEquals("0", act.get(0).value);
    }

    @Test
    public void testTokenize_digit2() throws Exception {
        act = lxr.init("90").tokenize();
        assertEquals(1, act.size());
        assertEquals("digit", act.get(0).kind);
        assertEquals("90", act.get(0).value);
    }

    @Test
    public void testTokenize_digit3() throws Exception {
        act = lxr.init("901").tokenize();
        assertEquals(1, act.size());
        assertEquals("digit", act.get(0).kind);
        assertEquals("901", act.get(0).value);
    }

    @Test
    public void testTokenize_ident() throws Exception {
        act = lxr.init("a").tokenize();
        assertEquals(1, act.size());
        assertEquals("ident", act.get(0).kind);
        assertEquals("a", act.get(0).value);
    }

    @Test
    public void testTokenize_ident2() throws Exception {
        act = lxr.init("a1").tokenize();
        assertEquals(1, act.size());
        assertEquals("ident", act.get(0).kind);
        assertEquals("a1", act.get(0).value);
    }

    @Test
    public void testTokenize_string1() throws Exception {
        act = lxr.init("\"\"").tokenize();
        assertEquals(1, act.size());
        assertEquals("string", act.get(0).kind);
        assertEquals("", act.get(0).value);
    }

    @Test
    public void testTokenize_string2() throws Exception {
        act = lxr.init("\"a\"").tokenize();
        assertEquals(1, act.size());
        assertEquals("string", act.get(0).kind);
        assertEquals("a", act.get(0).value);
    }

    @Test
    public void testTokenize_string3() throws Exception {
        act = lxr.init("\"ab\"").tokenize();
        assertEquals(1, act.size());
        assertEquals("string", act.get(0).kind);
        assertEquals("ab", act.get(0).value);
    }

    @Test
    public void testTokenize_string4() throws Exception {
        act = lxr.init("\"\\\\\\/\\b\\f\\n\\r\\t\"").tokenize();
        assertEquals(1, act.size());
        assertEquals("string", act.get(0).kind);
        assertEquals("\\/\b\f\n\r\t", act.get(0).value);
    }

    @Test
    public void testTokenize_expr1() throws Exception {
        act = lxr.init("ans1 = 9 / 3 * 4 - 6 + 5").tokenize();
        assertEquals(11, act.size());
        assertEquals("ident", act.get(0).kind);
        assertEquals("ans1", act.get(0).value);
        assertEquals("sign", act.get(1).kind);
        assertEquals("=", act.get(1).value);
        assertEquals("digit", act.get(2).kind);
        assertEquals("9", act.get(2).value);
        assertEquals("sign", act.get(3).kind);
        assertEquals("/", act.get(3).value);
        assertEquals("digit", act.get(4).kind);
        assertEquals("3", act.get(4).value);
        assertEquals("sign", act.get(5).kind);
        assertEquals("*", act.get(5).value);
        assertEquals("digit", act.get(6).kind);
        assertEquals("4", act.get(6).value);
        assertEquals("sign", act.get(7).kind);
        assertEquals("-", act.get(7).value);
        assertEquals("digit", act.get(8).kind);
        assertEquals("6", act.get(8).value);
        assertEquals("sign", act.get(9).kind);
        assertEquals("+", act.get(9).value);
        assertEquals("digit", act.get(10).kind);
        assertEquals("5", act.get(10).value);
    }

}
