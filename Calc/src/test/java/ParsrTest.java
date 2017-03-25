

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
        } catch(Exception e) {
            
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
        } catch(Exception e) {
            
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
        } catch(Exception e) {
            
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
        } catch(Exception e) {
            
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
        assertEquals("(a = ((1 + b) = (((((2 - 4) + ((((5 * 6) / 7) * 8) / 9)) - ((((10 / 12) * 13) / 14) * 15)) + 16) - 17)))", block.get(0).paren());
    }
    
    @Test
    public void line1_println() throws Exception {
        text = "println(1)";
        block = parser.init(lexer.init(text).tokenize()).block();
        assertEquals(1, block.size());
        assertEquals("(println ( 1)", block.get(0).paren());
    }
    
}
