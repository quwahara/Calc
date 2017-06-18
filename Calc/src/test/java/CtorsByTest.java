import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CtorsByTest {
    
    public static class Mock {

        public Mock() {
        }

        public Mock(Integer a) {
        }

        public Mock(Object a) {
        }

        public Mock(String a) {
        }

        public Mock(int a) {
        }

        public Mock(Object a, Object b) {
        }

        public Mock(Object a, int b) {
        }

        public Mock(Object a, Integer b) {
        }

        public Mock(Object a, String b) {
        }

        public Mock(int a, Object b) {
        }

        public Mock(int a, int b) {
        }

        public Mock(int a, Integer b) {
        }

        public Mock(int a, String b) {
        }

        public Mock(Integer a, Object b) {
        }

        public Mock(Integer a, int b) {
        }

        public Mock(Integer a, Integer b) {
        }

        public Mock(Integer a, String b) {
        }

        public Mock(String a, Object b) {
        }

        public Mock(String a, int b) {
        }

        public Mock(String a, Integer b) {
        }

        public Mock(String a, String b) {
        }
    }

    Interpreter.MethodFunc f;
    Constructor<?>[] ctors;
    List<Constructor<?>> ctorList;
    List<Class<?>> classes;

    @Before
    public void setUp() throws Exception {
        f = new Interpreter.MethodFunc();
        ctors = Mock.class.getConstructors();
        ctorList = Arrays.asList(ctors);
        classes = new ArrayList<Class<?>>();
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

    public String csString(List<Constructor<?>> cs) {
        List<String> sigs = new ArrayList<String>();
        for (Constructor<?> ctor : cs) {
            List<String> ps = new ArrayList<String>();
            for (Class<?> c : ctor.getParameterTypes()) {
                ps.add(c.getSimpleName());
            }
            sigs.add("[" + join(ps) + "]");
        }
        Collections.sort(sigs);
        return join(sigs);
    }

    @Test
    public void ctorsByAssignable() {
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[]", csString(cs));
    }
    
    @Test
    public void ctorsByAssignable_Ig() {
        classes.add(Integer.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Integer],[Object],[int]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_nu() {
        classes.add(null);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Integer],[Object],[String],[int]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_Ob() {
        classes.add(Object.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Object]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_St() {
        classes.add(String.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Object],[String]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_it() {
        classes.add(int.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Integer],[Object],[int]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_Ig_Ig() {
        classes.add(Integer.class);
        classes.add(Integer.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Integer,Integer],[Integer,Object],[Integer,int],[Object,Integer],[Object,Object],[Object,int],[int,Integer],[int,Object],[int,int]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_Ig_nu() {
        classes.add(Integer.class);
        classes.add(null);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Integer,Integer],[Integer,Object],[Integer,String],[Integer,int],[Object,Integer],[Object,Object],[Object,String],[Object,int],[int,Integer],[int,Object],[int,String],[int,int]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_Ig_Ob() {
        classes.add(Integer.class);
        classes.add(Object.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Integer,Object],[Object,Object],[int,Object]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_Ig_St() {
        classes.add(Integer.class);
        classes.add(String.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Integer,Object],[Integer,String],[Object,Object],[Object,String],[int,Object],[int,String]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_Ig_in() {
        classes.add(Integer.class);
        classes.add(int.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Integer,Integer],[Integer,Object],[Integer,int],[Object,Integer],[Object,Object],[Object,int],[int,Integer],[int,Object],[int,int]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_nu_Ig() {
        classes.add(null);
        classes.add(Integer.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Integer,Integer],[Integer,Object],[Integer,int],[Object,Integer],[Object,Object],[Object,int],[String,Integer],[String,Object],[String,int],[int,Integer],[int,Object],[int,int]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_nu_nu() {
        classes.add(null);
        classes.add(null);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Integer,Integer],[Integer,Object],[Integer,String],[Integer,int],[Object,Integer],[Object,Object],[Object,String],[Object,int],[String,Integer],[String,Object],[String,String],[String,int],[int,Integer],[int,Object],[int,String],[int,int]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_nu_Ob() {
        classes.add(null);
        classes.add(Object.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Integer,Object],[Object,Object],[String,Object],[int,Object]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_nu_St() {
        classes.add(null);
        classes.add(String.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Integer,Object],[Integer,String],[Object,Object],[Object,String],[String,Object],[String,String],[int,Object],[int,String]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_nu_in() {
        classes.add(null);
        classes.add(int.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Integer,Integer],[Integer,Object],[Integer,int],[Object,Integer],[Object,Object],[Object,int],[String,Integer],[String,Object],[String,int],[int,Integer],[int,Object],[int,int]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_Ob_Ig() {
        classes.add(Object.class);
        classes.add(Integer.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Object,Integer],[Object,Object],[Object,int]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_Ob_nu() {
        classes.add(Object.class);
        classes.add(null);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Object,Integer],[Object,Object],[Object,String],[Object,int]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_Ob_Ob() {
        classes.add(Object.class);
        classes.add(Object.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Object,Object]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_Ob_St() {
        classes.add(Object.class);
        classes.add(String.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Object,Object],[Object,String]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_Ob_in() {
        classes.add(Object.class);
        classes.add(int.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Object,Integer],[Object,Object],[Object,int]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_St_Ig() {
        classes.add(String.class);
        classes.add(Integer.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Object,Integer],[Object,Object],[Object,int],[String,Integer],[String,Object],[String,int]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_St_nu() {
        classes.add(String.class);
        classes.add(null);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Object,Integer],[Object,Object],[Object,String],[Object,int],[String,Integer],[String,Object],[String,String],[String,int]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_St_Ob() {
        classes.add(String.class);
        classes.add(Object.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Object,Object],[String,Object]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_St_St() {
        classes.add(String.class);
        classes.add(String.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Object,Object],[Object,String],[String,Object],[String,String]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_St_in() {
        classes.add(String.class);
        classes.add(int.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Object,Integer],[Object,Object],[Object,int],[String,Integer],[String,Object],[String,int]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_in_Ig() {
        classes.add(int.class);
        classes.add(Integer.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Integer,Integer],[Integer,Object],[Integer,int],[Object,Integer],[Object,Object],[Object,int],[int,Integer],[int,Object],[int,int]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_in_nu() {
        classes.add(int.class);
        classes.add(null);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Integer,Integer],[Integer,Object],[Integer,String],[Integer,int],[Object,Integer],[Object,Object],[Object,String],[Object,int],[int,Integer],[int,Object],[int,String],[int,int]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_in_Ob() {
        classes.add(int.class);
        classes.add(Object.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Integer,Object],[Object,Object],[int,Object]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_in_St() {
        classes.add(int.class);
        classes.add(String.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Integer,Object],[Integer,String],[Object,Object],[Object,String],[int,Object],[int,String]", csString(cs));
    }

    @Test
    public void ctorsByAssignable_in_in() {
        classes.add(int.class);
        classes.add(int.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAssignable(ctors, classes);
        assertEquals("[Integer,Integer],[Integer,Object],[Integer,int],[Object,Integer],[Object,Object],[Object,int],[int,Integer],[int,Object],[int,int]", csString(cs));
    }

    @Test
    public void ctorByAbsolute() {
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[]", csString(cs));
    }
    
    @Test
    public void ctorsByAbsolute_Ig() {
        classes.add(Integer.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[Integer]", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_nu() {
        classes.add(null);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_Ob() {
        classes.add(Object.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[Object]", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_St() {
        classes.add(String.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[String]", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_it() {
        classes.add(int.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[int]", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_Ig_Ig() {
        classes.add(Integer.class);
        classes.add(Integer.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[Integer,Integer]", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_Ig_nu() {
        classes.add(Integer.class);
        classes.add(null);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_Ig_Ob() {
        classes.add(Integer.class);
        classes.add(Object.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[Integer,Object]", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_Ig_St() {
        classes.add(Integer.class);
        classes.add(String.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[Integer,String]", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_Ig_in() {
        classes.add(Integer.class);
        classes.add(int.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[Integer,int]", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_nu_Ig() {
        classes.add(null);
        classes.add(Integer.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_nu_nu() {
        classes.add(null);
        classes.add(null);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_nu_Ob() {
        classes.add(null);
        classes.add(Object.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_nu_St() {
        classes.add(null);
        classes.add(String.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_nu_in() {
        classes.add(null);
        classes.add(int.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_Ob_Ig() {
        classes.add(Object.class);
        classes.add(Integer.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[Object,Integer]", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_Ob_nu() {
        classes.add(Object.class);
        classes.add(null);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_Ob_Ob() {
        classes.add(Object.class);
        classes.add(Object.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[Object,Object]", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_Ob_St() {
        classes.add(Object.class);
        classes.add(String.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[Object,String]", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_Ob_in() {
        classes.add(Object.class);
        classes.add(int.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[Object,int]", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_St_Ig() {
        classes.add(String.class);
        classes.add(Integer.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[String,Integer]", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_St_nu() {
        classes.add(String.class);
        classes.add(null);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_St_Ob() {
        classes.add(String.class);
        classes.add(Object.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[String,Object]", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_St_St() {
        classes.add(String.class);
        classes.add(String.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[String,String]", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_St_in() {
        classes.add(String.class);
        classes.add(int.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[String,int]", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_in_Ig() {
        classes.add(int.class);
        classes.add(Integer.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[int,Integer]", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_in_nu() {
        classes.add(int.class);
        classes.add(null);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_in_Ob() {
        classes.add(int.class);
        classes.add(Object.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[int,Object]", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_in_St() {
        classes.add(int.class);
        classes.add(String.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[int,String]", csString(cs));
    }

    @Test
    public void ctorsByAbsolute_in_in() {
        classes.add(int.class);
        classes.add(int.class);
        List<Constructor<?>> cs = Interpreter.ctorsByAbsolute(ctorList, classes);
        assertEquals("[int,int]", csString(cs));
    }

}
