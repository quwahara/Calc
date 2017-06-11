
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    String text;
    String exp;
    Lexer lexer = new Lexer();
    Parser parser = new Parser();
    List<Token> block;

    @Before
    public void setUp() throws Exception {
        text = "";
        exp = "";
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
    public void line1_compare1() throws Exception {
        text = "a == b";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("(a == b)", block.get(0).paren());
    }

    @Test
    public void line1_compare2() throws Exception {
        text = "a == b != c";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("((a == b) != c)", block.get(0).paren());
    }

    @Test
    public void line1_compare3() throws Exception {
        text = "a + 1 < b * 2";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("((a + 1) < (b * 2))", block.get(0).paren());
    }

    @Test
    public void line1_compare4() throws Exception {
        text = "a + 1 <= b > c * 2 >= d";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("((((a + 1) <= b) > (c * 2)) >= d)", block.get(0).paren());
    }

    @Test
    public void line1_logical1() throws Exception {
        text = "a && b";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("(a && b)", block.get(0).paren());
    }

    @Test
    public void line1_logical2() throws Exception {
        text = "a && b || c";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("((a && b) || c)", block.get(0).paren());
    }

    @Test
    public void line1_logical3() throws Exception {
        text = "a == b && c || d != e";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("(((a == b) && c) || (d != e))", block.get(0).paren());
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
    public void line1_unaryOperator4() throws Exception {
        text = "!1";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("!1", block.get(0).paren());
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
        exp += "func \"function\"\n";
        exp += "[ident]\n";
        exp += "  ident \"f\"\n";
        exp += "[params]\n";
        exp += "  ident \"a\"\n";
        exp += "[block]\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void func8() throws Exception {
        text = "function f() {}";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "func \"function\"\n";
        exp += "[ident]\n";
        exp += "  ident \"f\"\n";
        exp += "[params]\n";
        exp += "[block]\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void func9() throws Exception {
        text = "function f(a, b) {}";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "func \"function\"\n";
        exp += "[ident]\n";
        exp += "  ident \"f\"\n";
        exp += "[params]\n";
        exp += "  ident \"a\"\n";
        exp += "  ident \"b\"\n";
        exp += "[block]\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void func10() throws Exception {
        text = "function f(a, b, c) {}";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "func \"function\"\n";
        exp += "[ident]\n";
        exp += "  ident \"f\"\n";
        exp += "[params]\n";
        exp += "  ident \"a\"\n";
        exp += "  ident \"b\"\n";
        exp += "  ident \"c\"\n";
        exp += "[block]\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void func11() throws Exception {
        text = "function f() { return }";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "func \"function\"\n";
        exp += "[ident]\n";
        exp += "  ident \"f\"\n";
        exp += "[params]\n";
        exp += "[block]\n";
        exp += "  ret \"return\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void func12() throws Exception {
        text = "function f() { return 0 }";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "func \"function\"\n";
        exp += "[ident]\n";
        exp += "  ident \"f\"\n";
        exp += "[params]\n";
        exp += "[block]\n";
        exp += "  ret \"return\"\n";
        exp += "  [left]\n";
        exp += "    digit \"0\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void funcExpr1() throws Exception {
        text = "function() { return 0 }";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "fexpr \"function\"\n";
        exp += "[params]\n";
        exp += "[block]\n";
        exp += "  ret \"return\"\n";
        exp += "  [left]\n";
        exp += "    digit \"0\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void funcExpr2() {
        text = "function(";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void funcExpr3() {
        text = "function()";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void funcExpr4() {
        text = "function() {";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
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
        exp += "paren \"(\"\n";
        exp += "[left]\n";
        exp += "  ident \"f\"\n";
        exp += "[params]\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void invoke8() throws Exception {
        text = "f(a)";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "paren \"(\"\n";
        exp += "[left]\n";
        exp += "  ident \"f\"\n";
        exp += "[params]\n";
        exp += "  ident \"a\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void invoke9() throws Exception {
        text = "f(a,b)";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "paren \"(\"\n";
        exp += "[left]\n";
        exp += "  ident \"f\"\n";
        exp += "[params]\n";
        exp += "  ident \"a\"\n";
        exp += "  ident \"b\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void invoke10() throws Exception {
        text = "f(a,b,c)";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "paren \"(\"\n";
        exp += "[left]\n";
        exp += "  ident \"f\"\n";
        exp += "[params]\n";
        exp += "  ident \"a\"\n";
        exp += "  ident \"b\"\n";
        exp += "  ident \"c\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void if1() {
        text = "if";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void if2() {
        text = "if(";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void if3() {
        text = "if(1";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void if4() {
        text = "if(1)";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void if5() {
        text = "if(1){";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void if6() throws Exception {
        text = "if(1){}";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "if \"if\"\n";
        exp += "[left]\n";
        exp += "  digit \"1\"\n";
        exp += "[block]\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void if7() throws Exception {
        text = "if(1)1";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "if \"if\"\n";
        exp += "[left]\n";
        exp += "  digit \"1\"\n";
        exp += "[block]\n";
        exp += "  digit \"1\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void if8() {
        text = "if(1){}else";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void if9() throws Exception {
        text = "if(1){}else 1";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "if \"if\"\n";
        exp += "[left]\n";
        exp += "  digit \"1\"\n";
        exp += "[block]\n";
        exp += "[blockOfElse]\n";
        exp += "  digit \"1\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void if10() {
        text = "if(1){}else{";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void if11() throws Exception {
        text = "if(1){}else{}";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "if \"if\"\n";
        exp += "[left]\n";
        exp += "  digit \"1\"\n";
        exp += "[block]\n";
        exp += "[blockOfElse]\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void while1() {
        text = "while";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void while2() {
        text = "while(";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void while3() {
        text = "while(1";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void while4() {
        text = "while(1)";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void while5() {
        text = "while(1){";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void while6() throws Exception {
        text = "while(1){}";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "while \"while\"\n";
        exp += "[left]\n";
        exp += "  digit \"1\"\n";
        exp += "[block]\n";
        exp += "";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void while7() throws Exception {
        text = "while(1) 3";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "while \"while\"\n";
        exp += "[left]\n";
        exp += "  digit \"1\"\n";
        exp += "[block]\n";
        exp += "  digit \"3\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void var1() {
        text = "var";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void var2() throws Exception {
        text = "var a";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "var \"var\"\n";
        exp += "[block]\n";
        exp += "  ident \"a\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void var3() {
        text = "var a =";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void var4() throws Exception {
        text = "var a = 0";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "var \"var\"\n";
        exp += "[block]\n";
        exp += "  sign \"=\"\n";
        exp += "  [left]\n";
        exp += "    ident \"a\"\n";
        exp += "  [right]\n";
        exp += "    digit \"0\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void var5() {
        text = "var a,";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void var6() throws Exception {
        text = "var a, b";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "var \"var\"\n";
        exp += "[block]\n";
        exp += "  ident \"a\"\n";
        exp += "  ident \"b\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void var7() {
        text = "var a, b,";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void var8() {
        text = "var a = 0,";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void var9() throws Exception {
        text = "var a = 0, b = 0";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "var \"var\"\n";
        exp += "[block]\n";
        exp += "  sign \"=\"\n";
        exp += "  [left]\n";
        exp += "    ident \"a\"\n";
        exp += "  [right]\n";
        exp += "    digit \"0\"\n";
        exp += "  sign \"=\"\n";
        exp += "  [left]\n";
        exp += "    ident \"b\"\n";
        exp += "  [right]\n";
        exp += "    digit \"0\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void newArray1() {
        text = "[";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newArray2() throws Exception {
        text = "[]";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "newArray \"[\"\n";
        exp += "[params]\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void newArray3() {
        text = "[a";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newArray4() throws Exception {
        text = "[a]";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "newArray \"[\"\n";
        exp += "[params]\n";
        exp += "  ident \"a\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void newArray5() {
        text = "[,";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newArray6() throws Exception {
        text = "[,]";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "newArray \"[\"\n";
        exp += "[params]\n";
        exp += "  blank \"\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void newArray7() {
        text = "[a,";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newArray8() throws Exception {
        text = "[a,]";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "newArray \"[\"\n";
        exp += "[params]\n";
        exp += "  ident \"a\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void newArray9() {
        text = "[a,b";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newArray10() throws Exception {
        text = "[a,b]";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "newArray \"[\"\n";
        exp += "[params]\n";
        exp += "  ident \"a\"\n";
        exp += "  ident \"b\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void newArray11() {
        text = "[,,";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newArray12() throws Exception {
        text = "[,,]";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "newArray \"[\"\n";
        exp += "[params]\n";
        exp += "  blank \"\"\n";
        exp += "  blank \"\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void newArray13() {
        text = "[a,,";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newArray14() throws Exception {
        text = "[a,,]";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "newArray \"[\"\n";
        exp += "[params]\n";
        exp += "  ident \"a\"\n";
        exp += "  blank \"\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void newArray15() {
        text = "[,b,";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newArray16() throws Exception {
        text = "[,b,]";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "newArray \"[\"\n";
        exp += "[params]\n";
        exp += "  blank \"\"\n";
        exp += "  ident \"b\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void newArray17() {
        text = "[,,c";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newArray18() throws Exception {
        text = "[,,c]";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "newArray \"[\"\n";
        exp += "[params]\n";
        exp += "  blank \"\"\n";
        exp += "  blank \"\"\n";
        exp += "  ident \"c\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }
    
    @Test
    public void newArray19() {
        text = "[a,b,";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newArray20() throws Exception {
        text = "[a,b,]";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "newArray \"[\"\n";
        exp += "[params]\n";
        exp += "  ident \"a\"\n";
        exp += "  ident \"b\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void newArray21() {
        text = "[,b,c";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newArray22() throws Exception {
        text = "[,b,c]";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "newArray \"[\"\n";
        exp += "[params]\n";
        exp += "  blank \"\"\n";
        exp += "  ident \"b\"\n";
        exp += "  ident \"c\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }

    @Test
    public void newArray23() {
        text = "[a,,c";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newArray24() throws Exception {
        text = "[a,,c]";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "newArray \"[\"\n";
        exp += "[params]\n";
        exp += "  ident \"a\"\n";
        exp += "  blank \"\"\n";
        exp += "  ident \"c\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }
    
    @Test
    public void newArray25() {
        text = "[a,b,c";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newArray26() throws Exception {
        text = "[a,b,c]";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "newArray \"[\"\n";
        exp += "[params]\n";
        exp += "  ident \"a\"\n";
        exp += "  ident \"b\"\n";
        exp += "  ident \"c\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }
    
    @Test
    public void newMap1() {
        text = "{";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newMap2() {
        text = "{,";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newMap3() {
        text = "{a";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newMap4() {
        text = "{a:";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newMap5() {
        text = "{a:0";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newMap6() throws Exception {
        text = "{a:0}";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "newMap \"{\"\n";
        exp += "[params]\n";
        exp += "  symbol \":\"\n";
        exp += "  [left]\n";
        exp += "    ident \"a\"\n";
        exp += "  [right]\n";
        exp += "    digit \"0\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }
    
    @Test
    public void newMap7() throws Exception {
        text = "{a:0,}";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "newMap \"{\"\n";
        exp += "[params]\n";
        exp += "  symbol \":\"\n";
        exp += "  [left]\n";
        exp += "    ident \"a\"\n";
        exp += "  [right]\n";
        exp += "    digit \"0\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }
    
    @Test
    public void newMap8() {
        text = "{a:0,b";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newMap9() {
        text = "{a:0,b:";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newMap10() {
        text = "{a:0, b:1";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newMap11() throws Exception {
        text = "{a:0, b:0}";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "newMap \"{\"\n";
        exp += "[params]\n";
        exp += "  symbol \":\"\n";
        exp += "  [left]\n";
        exp += "    ident \"a\"\n";
        exp += "  [right]\n";
        exp += "    digit \"0\"\n";
        exp += "  symbol \":\"\n";
        exp += "  [left]\n";
        exp += "    ident \"b\"\n";
        exp += "  [right]\n";
        exp += "    digit \"0\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }
    
    @Test
    public void newMap12() throws Exception {
        text = "{a:0, b:0,}";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "newMap \"{\"\n";
        exp += "[params]\n";
        exp += "  symbol \":\"\n";
        exp += "  [left]\n";
        exp += "    ident \"a\"\n";
        exp += "  [right]\n";
        exp += "    digit \"0\"\n";
        exp += "  symbol \":\"\n";
        exp += "  [left]\n";
        exp += "    ident \"b\"\n";
        exp += "  [right]\n";
        exp += "    digit \"0\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }
    
    @Test
    public void newMap13() {
        text = "{a:0, , b:1";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void newMap14() throws Exception {
        text = "{a:0, b:0, c:0}";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "newMap \"{\"\n";
        exp += "[params]\n";
        exp += "  symbol \":\"\n";
        exp += "  [left]\n";
        exp += "    ident \"a\"\n";
        exp += "  [right]\n";
        exp += "    digit \"0\"\n";
        exp += "  symbol \":\"\n";
        exp += "  [left]\n";
        exp += "    ident \"b\"\n";
        exp += "  [right]\n";
        exp += "    digit \"0\"\n";
        exp += "  symbol \":\"\n";
        exp += "  [left]\n";
        exp += "    ident \"c\"\n";
        exp += "  [right]\n";
        exp += "    digit \"0\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }
    
    @Test
    public void newMap15() throws Exception {
        text = "{}";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "newMap \"{\"\n";
        exp += "[params]\n";
        assertEquals(exp, block.get(0).indent(""));
    }
    
    @Test
    public void bracket1() {
        text = "a[";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void bracket2() {
        text = "a[0";
        try {
            block = parser.init(lexer.init(text).tokenize()).block();
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void bracket3() throws Exception {
        text = "a[0]";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        exp += "bracket \"[\"\n";
        exp += "[left]\n";
        exp += "  ident \"a\"\n";
        exp += "[right]\n";
        exp += "  digit \"0\"\n";
        assertEquals(exp, block.get(0).indent(""));
    }
    
}
