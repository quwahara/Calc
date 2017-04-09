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
        assertEquals(1, actual.size());
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
        assertEquals(1, actual.size());
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
        assertEquals(1, actual.size());
        assertEquals(6, (int) actual.get("v").value);
    }

    @Test
    public void testBody_22() throws Exception {
        text += "function f(a, b, c) {";
        text += "  return a + b + c";
        text += "}";
        text += "v = f(1, 2, 3)";
        actual = run();
        assertEquals(1, actual.size());
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
        assertEquals(1, actual.size());
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
        assertEquals(1, actual.size());
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
        assertEquals(1, actual.size());
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

    @Test
    public void testBody_28() throws Exception {
        text += "v = 0";
        text += "if(0) {";
        text += "  v = 1";
        text += "}";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_29() throws Exception {
        text += "v = 0";
        text += "if(1) {";
        text += "  v = 1";
        text += "}";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_30() throws Exception {
        text += "v = 0";
        text += "if(0) {";
        text += "  v = 1";
        text += "} else {";
        text += "  v = 2";
        text += "}";
        actual = run();
        assertEquals(2, (int) actual.get("v").value);
    }

    @Test
    public void testBody_31() throws Exception {
        text += "v = 0";
        text += "if(1) {";
        text += "  v = 1";
        text += "} else {";
        text += "  v = 2";
        text += "}";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_32() throws Exception {
        text += "function f(a) {";
        text += "  if(a) {";
        text += "    return 1";
        text += "  }";
        text += "  return 2";
        text += "}";
        text += "v = f(0)";
        actual = run();
        assertEquals(2, (int) actual.get("v").value);
    }

    @Test
    public void testBody_33() throws Exception {
        text += "function f(a) {";
        text += "  if(a) {";
        text += "    return 1";
        text += "  }";
        text += "  return 2";
        text += "}";
        text += "v = f(1)";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_34() throws Exception {
        text += "function f(a) {";
        text += "  if(a) {";
        text += "    return 1";
        text += "  } else {";
        text += "    return 2";
        text += "  }";
        text += "}";
        text += "v = f(0)";
        actual = run();
        assertEquals(2, (int) actual.get("v").value);
    }

    @Test
    public void testBody_35() throws Exception {
        text += "function f(a) {";
        text += "  if(a) {";
        text += "    return 1";
        text += "  } else {";
        text += "    return 2";
        text += "  }";
        text += "}";
        text += "v = f(1)";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_36() throws Exception {
        text += "function f(a, b) {";
        text += "  if(a) {";
        text += "    if(b) {";
        text += "      return 1";
        text += "    }";
        text += "    return 2";
        text += "  }";
        text += "  return 3";
        text += "}";
        text += "v = f(0, 0)";
        actual = run();
        assertEquals(3, (int) actual.get("v").value);
    }

    @Test
    public void testBody_37() throws Exception {
        text += "function f(a, b) {";
        text += "  if(a) {";
        text += "    if(b) {";
        text += "      return 1";
        text += "    }";
        text += "    return 2";
        text += "  }";
        text += "  return 3";
        text += "}";
        text += "v = f(1, 0)";
        actual = run();
        assertEquals(2, (int) actual.get("v").value);
    }

    @Test
    public void testBody_38() throws Exception {
        text += "function f(a, b) {";
        text += "  if(a) {";
        text += "    if(b) {";
        text += "      return 1";
        text += "    }";
        text += "    return 2";
        text += "  }";
        text += "  return 3";
        text += "}";
        text += "v = f(1, 1)";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_39() throws Exception {
        text += "function f(a, b, c) {";
        text += "  if(a) {";
        text += "    if(b) {";
        text += "      return 1";
        text += "    } else {";
        text += "      return 2";
        text += "    }";
        text += "    return -1";
        text += "  } else {";
        text += "    if(c) {";
        text += "      return 3";
        text += "    } else {";
        text += "      return 4";
        text += "    }";
        text += "    return -1";
        text += "  }";
        text += "}";
        text += "v = f(0, 0, 0)";
        actual = run();
        assertEquals(4, (int) actual.get("v").value);
    }

    @Test
    public void testBody_40() throws Exception {
        text += "function f(a, b, c) {";
        text += "  if(a) {";
        text += "    if(b) {";
        text += "      return 1";
        text += "    } else {";
        text += "      return 2";
        text += "    }";
        text += "    return -1";
        text += "  } else {";
        text += "    if(c) {";
        text += "      return 3";
        text += "    } else {";
        text += "      return 4";
        text += "    }";
        text += "    return -1";
        text += "  }";
        text += "}";
        text += "v = f(0, 0, 1)";
        actual = run();
        assertEquals(3, (int) actual.get("v").value);
    }

    @Test
    public void testBody_41() throws Exception {
        text += "function f(a, b, c) {";
        text += "  if(a) {";
        text += "    if(b) {";
        text += "      return 1";
        text += "    } else {";
        text += "      return 2";
        text += "    }";
        text += "    return -1";
        text += "  } else {";
        text += "    if(c) {";
        text += "      return 3";
        text += "    } else {";
        text += "      return 4";
        text += "    }";
        text += "    return -1";
        text += "  }";
        text += "}";
        text += "v = f(1, 0, 0)";
        actual = run();
        assertEquals(2, (int) actual.get("v").value);
    }

    @Test
    public void testBody_42() throws Exception {
        text += "function f(a, b, c) {";
        text += "  if(a) {";
        text += "    if(b) {";
        text += "      return 1";
        text += "    } else {";
        text += "      return 2";
        text += "    }";
        text += "    return -1";
        text += "  } else {";
        text += "    if(c) {";
        text += "      return 3";
        text += "    } else {";
        text += "      return 4";
        text += "    }";
        text += "    return -1";
        text += "  }";
        text += "}";
        text += "v = f(1, 1, 0)";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_43() throws Exception {
        text += "v = 0 == 0";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_44() throws Exception {
        text += "v = 0 == 1";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_45() throws Exception {
        text += "v = 1 == 1";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_46() throws Exception {
        text += "v = 0 != 0";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_47() throws Exception {
        text += "v = 0 != 1";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_48() throws Exception {
        text += "v = 1 != 1";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_49() throws Exception {
        text += "v = 0 < 0";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_50() throws Exception {
        text += "v = 0 < 1";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_51() throws Exception {
        text += "v = 1 < 0";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_52() throws Exception {
        text += "v = 0 <= 0";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_53() throws Exception {
        text += "v = 0 <= 1";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_54() throws Exception {
        text += "v = 1 <= 0";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_55() throws Exception {
        text += "v = 0 > 0";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_56() throws Exception {
        text += "v = 0 > 1";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_57() throws Exception {
        text += "v = 1 > 0";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_58() throws Exception {
        text += "v = 0 >= 0";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_59() throws Exception {
        text += "v = 0 >= 1";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_60() throws Exception {
        text += "v = 1 >= 0";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_61() throws Exception {
        text += "v = 0 && 0";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_62() throws Exception {
        text += "v = 0 && 1";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_63() throws Exception {
        text += "v = 1 && 1";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_64() throws Exception {
        text += "v = 0 || 0";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_65() throws Exception {
        text += "v = 0 || 1";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_66() throws Exception {
        text += "v = 1 || 1";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_67() throws Exception {
        text += "v = !0";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_68() throws Exception {
        text += "v = !1";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_69() throws Exception {
        text += "v = 0";
        text += "while(0) {";
        text += "  v = v + 1";
        text += "}";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_70() throws Exception {
        text += "v = 0";
        text += "while(v < 1) {";
        text += "  v = v + 1";
        text += "}";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_71() throws Exception {
        text += "v = 0";
        text += "while(v < 2) {";
        text += "  v = v + 1";
        text += "}";
        actual = run();
        assertEquals(2, (int) actual.get("v").value);
    }

    @Test
    public void testBody_72() throws Exception {
        text += "v = 0";
        text += "while(v < 3) {";
        text += "  v = v + 1";
        text += "}";
        actual = run();
        assertEquals(3, (int) actual.get("v").value);
    }

    @Test
    public void testBody_73() throws Exception {
        text += "v = 0";
        text += "while(v < 4) {";
        text += "  v = v + 1";
        text += "  if (v == 2) {";
        text += "    break";
        text += "  }";
        text += "}";
        actual = run();
        assertEquals(2, (int) actual.get("v").value);
    }

    @Test
    public void testBody_74() throws Exception {
        text += "i = 0";
        text += "v = 0";
        text += "while(i < 4) {";
        text += "  i = i + 1";
        text += "  v = v + 1";
        text += "  j = 0";
        text += "  while (j < 4) {";
        text += "    j = j + 1";
        text += "    if (j <= 2) {";
        text += "      v = v + 10";
        text += "      if (j == 2) {";
        text += "        v = v + 100";
        text += "        break";
        text += "      }";
        text += "    }";
        text += "  }";
        text += "  if (i == 2) {";
        text += "    v = v + 1000";
        text += "    break";
        text += "  }";
        text += "}";
        actual = run();
        assertEquals(1242, (int) actual.get("v").value);
    }

    @Test
    public void testBody_75() throws Exception {
        text += "a = 1";
        text += "function f() {";
        text += "  var a = 10";
        text += "}";
        text += "f()";
        actual = run();
        assertEquals(1, (int) actual.get("a").value);
    }

    @Test
    public void testBody_76() throws Exception {
        text += "a = 1";
        text += "function f(a) {";
        text += "  a = 10";
        text += "}";
        text += "f()";
        actual = run();
        assertEquals(1, (int) actual.get("a").value);
    }

    @Test
    public void testBody_77() throws Exception {
        text += "a = 1";
        text += "function f() {";
        text += "  a = 10";
        text += "}";
        text += "f()";
        actual = run();
        assertEquals(10, (int) actual.get("a").value);
    }

    @Test
    public void testBody_78() throws Exception {
        text += "f = function() {";
        text += "  return 1";
        text += "}";
        text += "a = f()";
        actual = run();
        assertEquals(1, (int) actual.get("a").value);
    }

    @Test
    public void testBody_79() throws Exception {
        text += "f = (function() {";
        text += "  var c = 0";
        text += "  return function() {";
        text += "    c = c + 1";
        text += "    return c";
        text += "  }";
        text += "})()";
        text += "f()";
        text += "f()";
        text += "a = f()";
        actual = run();
        assertEquals(3, (int) actual.get("a").value);
    }

    @Test
    public void testBody_140() throws Exception {
        try {
            text = "v = 1 + \"a\"";
            actual = run();
            fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void testBody_141() throws Exception {
        text += "v = 1 + \"2\"";
        actual = run();
        assertEquals(3, (int) actual.get("v").value);
    }

    @Test
    public void testBody_142() throws Exception {
        text += "v = \"1\" + \"2\"";
        actual = run();
        assertEquals("12", actual.get("v").value);
    }

    @Test
    public void testBody_143() throws Exception {
        text += "v = \"\" == \"\"";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_144() throws Exception {
        text += "v = \"\" == \"1\"";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_145() throws Exception {
        text += "v = \"1\" == \"1\"";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_146() throws Exception {
        text += "v = \"\" != \"\"";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_147() throws Exception {
        text += "v = \"\" != \"1\"";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_148() throws Exception {
        text += "v = \"1\" != \"1\"";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_149() throws Exception {
        text += "v = \"0\" < \"0\"";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_150() throws Exception {
        text += "v = \"0\" < \"1\"";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_151() throws Exception {
        text += "v = \"1\" < \"0\"";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_152() throws Exception {
        text += "v = \"0\" <= \"0\"";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_153() throws Exception {
        text += "v = \"0\" <= \"1\"";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_154() throws Exception {
        text += "v = \"1\" <= \"0\"";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_155() throws Exception {
        text += "v = \"0\" > \"0\"";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_156() throws Exception {
        text += "v = \"0\" > \"1\"";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_157() throws Exception {
        text += "v = \"1\" > \"0\"";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_158() throws Exception {
        text += "v = \"0\" >= \"0\"";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_159() throws Exception {
        text += "v = \"0\" >= \"1\"";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    @Test
    public void testBody_160() throws Exception {
        text += "v = \"1\" >= \"0\"";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_161() throws Exception {
        text += "v = \"\" && \"\"";
        actual = run();
        assertEquals("", actual.get("v").value);
    }

    @Test
    public void testBody_162() throws Exception {
        text += "v = \"\" && \"1\"";
        actual = run();
        assertEquals("", actual.get("v").value);
    }

    @Test
    public void testBody_163() throws Exception {
        text += "v = \"1\" && \"2\"";
        actual = run();
        assertEquals("2", actual.get("v").value);
    }

    @Test
    public void testBody_164() throws Exception {
        text += "v = \"1\" || \"\"";
        actual = run();
        assertEquals("1", actual.get("v").value);
    }

    @Test
    public void testBody_165() throws Exception {
        text += "v = \"\" || \"1\"";
        actual = run();
        assertEquals("1", actual.get("v").value);
    }

    @Test
    public void testBody_166() throws Exception {
        text += "v = \"1\" || \"2\"";
        actual = run();
        assertEquals("1", actual.get("v").value);
    }

    @Test
    public void testBody_167() throws Exception {
        text += "v = !\"\"";
        actual = run();
        assertEquals(1, (int) actual.get("v").value);
    }

    @Test
    public void testBody_168() throws Exception {
        text += "v = !\"1\"";
        actual = run();
        assertEquals(0, (int) actual.get("v").value);
    }

    private Map<String, Interpreter.Variable> run() throws Exception {
        List<Token> tokens = lexer.init(text).tokenize();
        List<Token> blk = parser.init(tokens).block();
        return interpreter.init(blk).run();
    }

}
