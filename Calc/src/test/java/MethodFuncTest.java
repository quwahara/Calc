
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MethodFuncTest {

    public static class Mock {

        public void m0() {
        }

        public void m1(Integer a) {
        }

        public void m1(Object a) {
        }

        public void m1(String a) {
        }

        public void m1(int a) {
        }

        public void m2(Object a, Object b) {
        }

        public void m2(Object a, int b) {
        }

        public void m2(Object a, Integer b) {
        }

        public void m2(Object a, String b) {
        }

        public void m2(int a, Object b) {
        }

        public void m2(int a, int b) {
        }

        public void m2(int a, Integer b) {
        }

        public void m2(int a, String b) {
        }

        public void m2(Integer a, Object b) {
        }

        public void m2(Integer a, int b) {
        }

        public void m2(Integer a, Integer b) {
        }

        public void m2(Integer a, String b) {
        }

        public void m2(String a, Object b) {
        }

        public void m2(String a, int b) {
        }

        public void m2(String a, Integer b) {
        }

        public void m2(String a, String b) {
        }
    }

    Interpreter.MethodFunc f;
    List<Method> methods;
    List<Class<?>> classes;

    @Before
    public void setUp() throws Exception {
        f = new Interpreter.MethodFunc();
        classes = new ArrayList<Class<?>>();
    }

    public List<Method> screenByName(String name) {
        List<Method> ms = new ArrayList<Method>();
        for (Method m : Mock.class.getMethods()) {
            if (m.getName().equals(name)) {
                ms.add(m);
            }
        }
        return ms;
    }

    public String join(List<String> ss) {
        StringBuilder b = new StringBuilder();
        if (ss.size() > 0) {
            b.append(ss.get(0));
        }
        for (int i = 1; i < ss.size(); ++i) {
            b.append(",").append(ss.get(i));
        }
        return b.toString();
    }

    public String csString(List<Method> cs) {
        List<String> sigs = new ArrayList<String>();
        for (Method m : cs) {
            List<String> ps = new ArrayList<String>();
            for (Class<?> c : m.getParameterTypes()) {
                ps.add(c.getSimpleName());
            }
            sigs.add("[" + join(ps) + "]");
        }
        Collections.sort(sigs);
        return join(sigs);
    }

    @Test
    public void methodByAbsoluteM0_1() {
        methods = screenByName("m0");
        List<Method> cs = f.methodByAbsolute(methods, classes);
        assertEquals("[]", csString(cs));
    }

    @Test
    public void methodByAbsoluteM1_000() {
        methods = screenByName("m1");
        classes.add(null);
        List<Method> cs = f.methodByAbsolute(methods, classes);
        assertEquals("", csString(cs));
    }

    @Test
    public void methodByAbsoluteM1_001() {
        methods = screenByName("m1");
        classes.add(Integer.class);
        List<Method> cs = f.methodByAbsolute(methods, classes);
        assertEquals("[Integer]", csString(cs));
    }

    @Test
    public void methodByAbsoluteM1_002() {
        methods = screenByName("m1");
        classes.add(Object.class);
        List<Method> cs = f.methodByAbsolute(methods, classes);
        assertEquals("[Object]", csString(cs));
    }

    @Test
    public void methodByAbsoluteM1_003() {
        methods = screenByName("m1");
        classes.add(String.class);
        List<Method> cs = f.methodByAbsolute(methods, classes);
        assertEquals("[String]", csString(cs));
    }

    @Test
    public void methodByAbsoluteM2_000() {
        methods = screenByName("m2");
        classes.add(null);
        classes.add(null);
        List<Method> cs = f.methodByAbsolute(methods, classes);
        assertEquals("", csString(cs));
    }

    @Test
    public void methodByAbsoluteM2_001() {
        methods = screenByName("m2");
        classes.add(Integer.class);
        classes.add(null);
        List<Method> cs = f.methodByAbsolute(methods, classes);
        assertEquals("", csString(cs));
    }

    @Test
    public void methodByAbsoluteM2_002() {
        methods = screenByName("m2");
        classes.add(null);
        classes.add(Integer.class);
        List<Method> cs = f.methodByAbsolute(methods, classes);
        assertEquals("", csString(cs));
    }

    @Test
    public void methodByAbsoluteM2_003() {
        methods = screenByName("m2");
        classes.add(Integer.class);
        classes.add(Integer.class);
        List<Method> cs = f.methodByAbsolute(methods, classes);
        assertEquals("[Integer,Integer]", csString(cs));
    }

    @Test
    public void methodByAbsoluteM2_004() {
        methods = screenByName("m2");
        classes.add(Object.class);
        classes.add(null);
        List<Method> cs = f.methodByAbsolute(methods, classes);
        assertEquals("", csString(cs));
    }

    @Test
    public void methodByAbsoluteM2_005() {
        methods = screenByName("m2");
        classes.add(null);
        classes.add(Object.class);
        List<Method> cs = f.methodByAbsolute(methods, classes);
        assertEquals("", csString(cs));
    }

    @Test
    public void methodByAbsoluteM2_006() {
        methods = screenByName("m2");
        classes.add(Object.class);
        classes.add(Object.class);
        List<Method> cs = f.methodByAbsolute(methods, classes);
        assertEquals("[Object,Object]", csString(cs));
    }

    @Test
    public void methodByAbsoluteM2_007() {
        methods = screenByName("m2");
        classes.add(String.class);
        classes.add(null);
        List<Method> cs = f.methodByAbsolute(methods, classes);
        assertEquals("", csString(cs));
    }

    @Test
    public void methodByAbsoluteM2_008() {
        methods = screenByName("m2");
        classes.add(null);
        classes.add(String.class);
        List<Method> cs = f.methodByAbsolute(methods, classes);
        assertEquals("", csString(cs));
    }

    @Test
    public void methodByAbsoluteM2_009() {
        methods = screenByName("m2");
        classes.add(String.class);
        classes.add(String.class);
        List<Method> cs = f.methodByAbsolute(methods, classes);
        assertEquals("[String,String]", csString(cs));
    }

    @Test
    public void methodByAbsoluteM2_010() {
        methods = screenByName("m2");
        classes.add(int.class);
        classes.add(null);
        List<Method> cs = f.methodByAbsolute(methods, classes);
        assertEquals("", csString(cs));
    }

    @Test
    public void methodByAbsoluteM2_011() {
        methods = screenByName("m2");
        classes.add(null);
        classes.add(int.class);
        List<Method> cs = f.methodByAbsolute(methods, classes);
        assertEquals("", csString(cs));
    }

    @Test
    public void methodByAbsoluteM2_012() {
        methods = screenByName("m2");
        classes.add(int.class);
        classes.add(int.class);
        List<Method> cs = f.methodByAbsolute(methods, classes);
        assertEquals("[int,int]", csString(cs));
    }

    @Test
    public void methodByAbsoluteM1_004() {
        methods = screenByName("m1");
        classes.add(int.class);
        List<Method> cs = f.methodByAbsolute(methods, classes);
        assertEquals("[int]", csString(cs));
    }

    @Test
    public void methodByAssignableM0_1() {
        methods = screenByName("m0");
        List<Method> cs = f.methodByAssignable(methods, classes);
        assertEquals("[]", csString(cs));
    }

    @Test
    public void methodByAssignableM1_1() {
        methods = screenByName("m1");
        classes.add(Object.class);
        List<Method> cs = f.methodByAssignable(methods, classes);
        assertEquals("[Object]", csString(cs));
    }

    @Test
    public void methodByAssignableM1_2() {
        methods = screenByName("m1");
        classes.add(int.class);
        List<Method> cs = f.methodByAssignable(methods, classes);
        assertEquals("[Integer],[Object],[int]", csString(cs));
    }

    @Test
    public void methodByAssignableM1_3() {
        methods = screenByName("m1");
        classes.add(Integer.class);
        List<Method> cs = f.methodByAssignable(methods, classes);
        assertEquals("[Integer],[Object],[int]", csString(cs));
    }

    @Test
    public void methodByAssignableM1_4() {
        methods = screenByName("m1");
        classes.add(String.class);
        List<Method> cs = f.methodByAssignable(methods, classes);
        assertEquals("[Object],[String]", csString(cs));
    }

    @Test
    public void methodByAssignableM1_5() {
        methods = screenByName("m1");
        classes.add(null);
        List<Method> cs = f.methodByAssignable(methods, classes);
        assertEquals("[Integer],[Object],[String],[int]", csString(cs));
    }

    @Test
    public void methodByAssignableM2_000() {
        methods = screenByName("m2");
        classes.add(null);
        classes.add(null);
        List<Method> cs = f.methodByAssignable(methods, classes);
        assertEquals(
                "[Integer,Integer],[Integer,Object],[Integer,String],[Integer,int],[Object,Integer],[Object,Object],[Object,String],[Object,int],[String,Integer],[String,Object],[String,String],[String,int],[int,Integer],[int,Object],[int,String],[int,int]",
                csString(cs));
    }

    @Test
    public void methodByAssignableM2_001() {
        methods = screenByName("m2");
        classes.add(Integer.class);
        classes.add(null);
        List<Method> cs = f.methodByAssignable(methods, classes);
        assertEquals(
                "[Integer,Integer],[Integer,Object],[Integer,String],[Integer,int],[Object,Integer],[Object,Object],[Object,String],[Object,int],[int,Integer],[int,Object],[int,String],[int,int]",
                csString(cs));
    }

    @Test
    public void methodByAssignableM2_002() {
        methods = screenByName("m2");
        classes.add(null);
        classes.add(Integer.class);
        List<Method> cs = f.methodByAssignable(methods, classes);
        assertEquals(
                "[Integer,Integer],[Integer,Object],[Integer,int],[Object,Integer],[Object,Object],[Object,int],[String,Integer],[String,Object],[String,int],[int,Integer],[int,Object],[int,int]",
                csString(cs));
    }

    @Test
    public void methodByAssignableM2_003() {
        methods = screenByName("m2");
        classes.add(Integer.class);
        classes.add(Integer.class);
        List<Method> cs = f.methodByAssignable(methods, classes);
        assertEquals(
                "[Integer,Integer],[Integer,Object],[Integer,int],[Object,Integer],[Object,Object],[Object,int],[int,Integer],[int,Object],[int,int]",
                csString(cs));
    }

    @Test
    public void methodByAssignableM2_004() {
        methods = screenByName("m2");
        classes.add(Object.class);
        classes.add(null);
        List<Method> cs = f.methodByAssignable(methods, classes);
        assertEquals("[Object,Integer],[Object,Object],[Object,String],[Object,int]", csString(cs));
    }

    @Test
    public void methodByAssignableM2_005() {
        methods = screenByName("m2");
        classes.add(null);
        classes.add(Object.class);
        List<Method> cs = f.methodByAssignable(methods, classes);
        assertEquals("[Integer,Object],[Object,Object],[String,Object],[int,Object]", csString(cs));
    }

    @Test
    public void methodByAssignableM2_006() {
        methods = screenByName("m2");
        classes.add(Object.class);
        classes.add(Object.class);
        List<Method> cs = f.methodByAssignable(methods, classes);
        assertEquals("[Object,Object]", csString(cs));
    }

    @Test
    public void methodByAssignableM2_007() {
        methods = screenByName("m2");
        classes.add(String.class);
        classes.add(null);
        List<Method> cs = f.methodByAssignable(methods, classes);
        assertEquals(
                "[Object,Integer],[Object,Object],[Object,String],[Object,int],[String,Integer],[String,Object],[String,String],[String,int]",
                csString(cs));
    }

    @Test
    public void methodByAssignableM2_008() {
        methods = screenByName("m2");
        classes.add(null);
        classes.add(String.class);
        List<Method> cs = f.methodByAssignable(methods, classes);
        assertEquals(
                "[Integer,Object],[Integer,String],[Object,Object],[Object,String],[String,Object],[String,String],[int,Object],[int,String]",
                csString(cs));
    }

    @Test
    public void methodByAssignableM2_009() {
        methods = screenByName("m2");
        classes.add(String.class);
        classes.add(String.class);
        List<Method> cs = f.methodByAssignable(methods, classes);
        assertEquals("[Object,Object],[Object,String],[String,Object],[String,String]", csString(cs));
    }

    @Test
    public void methodByAssignableM2_010() {
        methods = screenByName("m2");
        classes.add(int.class);
        classes.add(null);
        List<Method> cs = f.methodByAssignable(methods, classes);
        assertEquals(
                "[Integer,Integer],[Integer,Object],[Integer,String],[Integer,int],[Object,Integer],[Object,Object],[Object,String],[Object,int],[int,Integer],[int,Object],[int,String],[int,int]",
                csString(cs));
    }

    @Test
    public void methodByAssignableM2_011() {
        methods = screenByName("m2");
        classes.add(null);
        classes.add(int.class);
        List<Method> cs = f.methodByAssignable(methods, classes);
        assertEquals(
                "[Integer,Integer],[Integer,Object],[Integer,int],[Object,Integer],[Object,Object],[Object,int],[String,Integer],[String,Object],[String,int],[int,Integer],[int,Object],[int,int]",
                csString(cs));
    }

    @Test
    public void methodByAssignableM2_012() {
        methods = screenByName("m2");
        classes.add(int.class);
        classes.add(int.class);
        List<Method> cs = f.methodByAssignable(methods, classes);
        assertEquals(
                "[Integer,Integer],[Integer,Object],[Integer,int],[Object,Integer],[Object,Object],[Object,int],[int,Integer],[int,Object],[int,int]",
                csString(cs));
    }
}
