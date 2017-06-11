import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Interpreter {

    Scope global;
    Scope local;
    List<Token> body;

    public Interpreter init(List<Token> body) {
        global = new Scope();
        local = global;
        Func f = new Println();
        global.functions.put(f.name, f);
        this.body = body;
        return this;
    }

    public Map<String, Variable> run() throws Exception {
        body(body, null, null);
        return global.variables;
    }

    public Object body(List<Token> body, boolean[] ret, boolean[] brk) throws Exception {
        for (Token exprs : body) {
            if (exprs.kind.equals("if")) {
                Object val = if_(exprs, ret, brk);
                if (ret != null && ret[0]) {
                    return val;
                }
            } else if (exprs.kind.equals("ret")) {
                if (ret == null) {
                    throw new Exception("Can not return");
                }
                ret[0] = true;
                if (exprs.left == null) {
                    return null;
                } else {
                    return expression(exprs.left);
                }
            } else if (exprs.kind.equals("while")) {
                Object val = while_(exprs, ret);
                if (ret != null && ret[0]) {
                    return val;
                }
            } else if (exprs.kind.equals("brk")) {
                if (brk == null) {
                    throw new Exception("Can not break");
                }
                brk[0] = true;
                return null;
            } else if (exprs.kind.equals("var")) {
                var(exprs);
            } else {
                expression(exprs);
            }
        }
        return null;
    }

    public Object ret(Token token) throws Exception {
        if (token.left == null) {
            return null;
        }
        return expression(token.left);
    }

    public Object if_(Token token, boolean[] ret, boolean[] brk) throws Exception {
        List<Token> block;
        if (isTrue(token.left)) {
            block = token.block;
        } else {
            block = token.blockOfElse;
        }
        if (block != null) {
            return body(block, ret, brk);
        } else {
            return null;
        }
    }

    public Object while_(Token token, boolean[] ret) throws Exception {
        boolean[] brk = new boolean[1];
        Object val;
        while (isTrue(token.left)) {
            val = body(token.block, ret, brk);
            if (ret != null && ret[0]) {
                return val;
            }
            if (brk[0]) {
                return null;
            }
        }
        return null;
    }

    public boolean isTrue(Token token) throws Exception {
        return isTrue(value(expression(token)));
    }

    public boolean isTrue(Object value) throws Exception {
        if (value instanceof Integer) {
            return 0 != ((Integer) value);
        } else if (value instanceof String) {
            return !"".equals(value);
        } else if (value instanceof Func) {
            return true;
        } else {
            return false;
        }
    }

    public Object var(Token token) throws Exception {
        for (Token item : token.block) {
            String name;
            Token expr;
            if (item.kind.equals("ident")) {
                name = item.value;
                expr = null;
            } else if (item.kind.equals("sign") && item.value.equals("=")) {
                name = item.left.value;
                expr = item;
            } else {
                throw new Exception("var error");
            }
            if (!local.variables.containsKey(name)) {
                newVariable(name);
            }
            if (expr != null) {
                expression(expr);
            }
        }
        return null;
    }

    public Variable newVariable(String name) {
        Variable v = new Variable();
        v.name = name;
        v.value = 0;
        local.variables.put(name, v);
        return v;
    }

    public Object expression(Token expr) throws Exception {
        if (expr.kind.equals("digit")) {
            return digit(expr);
        } else if (expr.kind.equals("string")) {
            return string(expr);
        } else if (expr.kind.equals("ident")) {
            return ident(expr);
        } else if (expr.kind.equals("blank")) {
            return blank(expr);
            // Add
        } else if (expr.kind.equals("newMap")) {
            return newMap(expr);
        } else if (expr.kind.equals("newArray")) {
            return newArray(expr);
            // Update
        } else if (expr.kind.equals("bracket")) {
            return accessArrayOrMap(expr);
        } else if (expr.kind.equals("func")) {
            return func(expr);
        } else if (expr.kind.equals("fexpr")) {
            return fexpr(expr);
        } else if (expr.kind.equals("paren")) {
            return invoke(expr);
        } else if (expr.kind.equals("sign") && expr.value.equals("=")) {
            return assign(expr);
        } else if (expr.kind.equals("unary")) {
            return unaryCalc(expr);
        } else if (expr.kind.equals("sign")) {
            return calc(expr);
        } else if (expr.kind.equals("dot")) {
            return dot(expr);
        } else {
            throw new Exception("Expression error");
        }
    }

    public Object dot(Token token) throws Exception {
        Dotted d = new Dotted();
        d.left = value(expression(token.left));
        d.right = token.right;
        return d;
    }

    public static class Dotted {
        public Object left;
        public Token right;
    }

    public Integer digit(Token token) {
        return Integer.decode(token.value);
    }

    public String string(Token token) {
        return token.value;
    }

    public Object ident(Token token) {
        String name = token.value;
        Scope scope = local;
        while (scope != null) {
            if (scope.functions.containsKey(name)) {
                return scope.functions.get(name);
            }
            if (scope.variables.containsKey(name)) {
                return scope.variables.get(name);
            }
            scope = scope.parent;
        }
        return newVariable(name);
    }

    public Object blank(Token token) {
        return null;
    }

    public Object newArray(Token expr) throws Exception {
        List<Object> a = new ArrayList<>();
        for (Token item : expr.params) {
            a.add(value(expression(item)));
        }
        return a;
    }

    public Object newMap(Token expr) throws Exception {
        Map<String, Object> m = new LinkedHashMap<>();
        for (Token item : expr.params) {
            String key = identOrString(item.left);
            Object value = value(expression(item.right));
            m.put(key, value);
        }
        return m;
    }

    public Object func(Token token) throws Exception {
        String name = token.ident.value;
        if (local.functions.containsKey(name)) {
            throw new Exception("Name was used");
        }
        if (local.variables.containsKey(name)) {
            throw new Exception("Name was used");
        }
        List<String> paramCheckList = new ArrayList<String>();
        for (Token p : token.params) {
            String param = p.value;
            if (paramCheckList.contains(param)) {
                throw new Exception("Parameter name was used");
            }
            paramCheckList.add(param);
        }
        DynamicFunc func = new DynamicFunc();
        func.context = new Interpreter();
        func.context.global = global;
        func.context.local = local;
        func.context.body = body;
        func.name = name;
        func.params = token.params;
        func.block = token.block;
        local.functions.put(name, func);
        return null;
    }

    public Object fexpr(Token token) throws Exception {
        List<String> paramCheckList = new ArrayList<String>();
        for (Token p : token.params) {
            String param = p.value;
            if (paramCheckList.contains(param)) {
                throw new Exception("Parameter name was used");
            }
            paramCheckList.add(param);
        }
        DynamicFunc func = new DynamicFunc();
        func.context = new Interpreter();
        func.context.global = global;
        func.context.local = local;
        func.context.body = body;
        func.params = token.params;
        func.block = token.block;
        return func;
    }

    public Variable assign(Token expr) throws Exception {
        Variable variable = variable(expression(expr.left));
        variable.value = value(expression(expr.right));
        return variable;
    }

    public Variable variable(Object value) throws Exception {
        if (value instanceof Variable) {
            return (Variable) value;
        } else {
            throw new Exception("left value error");
        }
    }

    public Object value(Object value) throws Exception {
        if (value instanceof Integer) {
            return value;
        } else if (value instanceof String) {
            return value;
        } else if (value instanceof List<?>) {
            return value;
            // Add
        } else if (value instanceof Map<?, ?>) {
            return value;
        } else if (value == null) {
            return value;
        } else if (value instanceof Func) {
            return value;
        } else if (value instanceof Variable) {
            Variable v = (Variable) value;
            return value(v.value);
        }
        throw new Exception("right value error");
    }

    public Integer integer(Object value) throws Exception {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            return Integer.decode((String) value);
        } else if (value instanceof Variable) {
            Variable v = (Variable) value;
            return integer(v.value);
        }
        throw new Exception("right value error");
    }

    public String string(Object value) throws Exception {
        if (value instanceof String) {
            return (String) value;
        } else if (value instanceof Integer) {
            return value.toString();
        } else if (value instanceof Variable) {
            Variable v = (Variable) value;
            return string(v.value);
        }
        throw new Exception("right value error");
    }

    public String identOrString(Token expr) throws Exception {
        if (expr.kind.equals("ident")) {
            return expr.value;
        } else {
            return string(expression(expr));
        }
    }

    @SuppressWarnings("unchecked")
    public List<Object> array(Object value) throws Exception {
        if (value instanceof List<?>) {
            return (List<Object>) value;
        } else if (value instanceof Variable) {
            Variable v = (Variable) value;
            return array(v.value);
        }
        throw new Exception("right value error");
    }

    public Object unaryCalc(Token expr) throws Exception {
        Object value = value(expression(expr.left));
        if (value instanceof Integer) {
            return unaryCalcInteger(expr.value, (Integer) value);
        } else if (value instanceof String) {
            return unaryCalcString(expr.value, (String) value);
        } else {
            throw new Exception("unaryCalc error");
        }
    }

    public Object unaryCalcInteger(String sign, Integer left) throws Exception {
        if (sign.equals("+")) {
            return left;
        } else if (sign.equals("-")) {
            return -left;
        } else if (sign.equals("!")) {
            return toInteger(!isTrue(left));
        } else {
            throw new Exception("unaryCalcInteger error");
        }
    }

    public Object unaryCalcString(String sign, String left) throws Exception {
        if (sign.equals("!")) {
            return toInteger(!isTrue(left));
        } else {
            throw new Exception("unaryCalcString error");
        }
    }

    @SuppressWarnings("unchecked")
    public Object accessArrayOrMap(Token expr) throws Exception {
        Object v = value(expression(expr.left));
        if (v instanceof List<?>) {
            List<Object> ar = (List<Object>) v;
            Integer index = integer(expression(expr.right));
            return ar.get(index);
        } else if (v instanceof Map<?, ?>) {
            Map<String, Object> map = (Map<String, Object>) v;
            String key = string(expression(expr.right));
            return map.get(key);
        } else {
            throw new Exception("accessArrayOrMap error");
        }
    }

    public Object calc(Token expr) throws Exception {
        Object left = value(expression(expr.left));
        Object right = value(expression(expr.right));
        Integer ileft = null;
        String sleft = null;

        if (left instanceof Integer) {
            ileft = (Integer) left;
        } else if (left instanceof String) {
            sleft = (String) left;
        }

        if (ileft != null) {
            return calcInteger(expr.value, ileft, right);
        } else if (sleft != null) {
            return calcString(expr.value, sleft, right);
        } else {
            throw new Exception("calc error");
        }
    }

    public Object calcInteger(String sign, Integer left, Object right) throws Exception {
        if (sign.equals("+")) {
            return left + integer(right);
        } else if (sign.equals("-")) {
            return left - integer(right);
        } else if (sign.equals("*")) {
            return left * integer(right);
        } else if (sign.equals("/")) {
            return left / integer(right);
        } else if (sign.equals("==")) {
            return toInteger(left == integer(right));
        } else if (sign.equals("!=")) {
            return toInteger(left != integer(right));
        } else if (sign.equals("<")) {
            return toInteger(left < integer(right));
        } else if (sign.equals("<=")) {
            return toInteger(left <= integer(right));
        } else if (sign.equals(">")) {
            return toInteger(left > integer(right));
        } else if (sign.equals(">=")) {
            return toInteger(left >= integer(right));
        } else if (sign.equals("&&")) {
            if (!isTrue(left)) {
                return left;
            }
            return right;
        } else if (sign.equals("||")) {
            if (isTrue(left)) {
                return left;
            }
            return right;
        } else {
            throw new Exception("calcIteger error");
        }
    }

    public Object calcString(String sign, String left, Object right) throws Exception {
        if (sign.equals("+")) {
            return left + string(right);
        } else if (sign.equals("==")) {
            return toInteger(left.equals(string(right)));
        } else if (sign.equals("!=")) {
            return toInteger(!left.equals(string(right)));
        } else if (sign.equals("<")) {
            return toInteger(left.compareTo(string(right)) < 0);
        } else if (sign.equals("<=")) {
            return toInteger(left.compareTo(string(right)) <= 0);
        } else if (sign.equals(">")) {
            return toInteger(left.compareTo(string(right)) > 0);
        } else if (sign.equals(">=")) {
            return toInteger(left.compareTo(string(right)) >= 0);
        } else if (sign.equals("&&")) {
            if (!isTrue(left)) {
                return left;
            }
            return right;
        } else if (sign.equals("||")) {
            if (isTrue(left)) {
                return left;
            }
            return right;
        } else {
            throw new Exception("calcString error");
        }
    }

    public Integer toInteger(boolean b) {
        return b ? 1 : 0;
    }

    private Object invoke(Token expr) throws Exception {
        Func f = func(expression(expr.left));
        List<Object> values = new ArrayList<Object>();
        for (Token arg : expr.params) {
            values.add(value(expression(arg)));
        }
        return f.invoke(values);
    }

    public Func func(Object value) throws Exception {
        if (value instanceof Func) {
            return (Func) value;
        } else if (value instanceof Dotted) {
            Dotted d = (Dotted) value;
            MethodFunc mf = new MethodFunc();
            mf.name = d.right.value;
            mf.class_ = d.left.getClass();
            mf.target = d.left;
            return mf;
        } else if (value instanceof Variable) {
            Variable v = (Variable) value;
            return func(v.value);
        } else {
            throw new Exception("Not a function");
        }
    }

    public static class Scope {

        public Scope parent;
        public Map<String, Func> functions;
        public Map<String, Variable> variables;

        public Scope() {
            functions = new HashMap<>();
            variables = new HashMap<>();
        }
    }

    public static class Variable {
        public String name;
        public Object value;

        @Override
        public String toString() {
            return name + " " + value;
        }
    }

    public static abstract class Func {
        public String name;

        abstract public Object invoke(List<Object> args) throws Exception;
    }

    public static class Println extends Func {
        public Println() {
            name = "println";
        }

        @Override
        public Object invoke(List<Object> args) throws Exception {
            Object arg = args.size() > 0 ? args.get(0) : null;
            System.out.println(arg);
            return null;
        }
    }

    public static class DynamicFunc extends Func {

        public Interpreter context;
        public List<Token> params;
        public List<Token> block;

        @Override
        public Object invoke(List<Object> args) throws Exception {
            Scope parent = context.local;
            context.local = new Scope();
            context.local.parent = parent;
            for (int i = 0; i < params.size(); ++i) {
                Token param = params.get(i);
                Variable v = context.newVariable(param.value);
                if (i < args.size()) {
                    v.value = context.value(args.get(i));
                } else {
                    v.value = null;
                }
            }
            Object val;
            boolean[] ret = new boolean[1];
            val = context.body(block, ret, null);
            context.local = parent;
            return val;
        }
    }

    public static class MethodFunc extends Func {

        // メソッド呼び出し対象の型を表します
        public Class<?> class_;
        // メソッド呼び出し対象のインスタンスを表します
        public Object target;

        @Override
        public Object invoke(List<Object> args) throws Exception {
            
            // 引数から引数の型の一覧を作ります
            List<Class<?>> aClasses = argClasses(args);
            
            // メソッド呼び出し対象の型が持つメソッド情報の一覧を、
            // このMethodFuncの名前と同じもののみに絞った一覧にします
            List<Method> mByName = methodByName(class_.getMethods(), name);
            
            // 名前で絞ったメソッド情報の一覧を、
            // 引数の型の一覧が代入可能なシグニチャーになっているもののみに絞った一覧にします
            List<Method> mByAssignable = methodByAssignable(mByName, aClasses);
            
            // 絞った結果、該当するメソッド情報がなかったらエラー
            if (mByAssignable.size() == 0) {
                throw new Exception("MethodFunc.invoke error");
            }
            
            Method method;
            if (mByAssignable.size() == 1) {
                
                // 絞った結果、該当するメソッド情報が1つだったら、
                // それが呼び出し対象のメソッド情報
                method = mByAssignable.get(0);
            
            } else {
                
                // 絞った結果、該当するメソッド情報が2つ以上だったら、さらに絞り込みます。
                // 代入可能なシグニチャーで絞ったメソッド情報の一覧を、
                // 引数の型の一覧が完全に一致するシグニチャーになっているもののみに絞り込みます。
                List<Method> mByAbsolute = methodByAbsolute(mByAssignable, aClasses);
                
                // 絞った結果、該当するメソッド情報が1つにならなかったらエラー
                if (mByAbsolute.size() != 1) {
                    throw new Exception("MethodFunc.invoke error");
                }
                
                // 絞った結果、該当するメソッド情報が1つだったら、
                // それが呼び出し対象のメソッド情報
                method = mByAbsolute.get(0);
                
            }
            
            // 1つに絞れたメソッド情報を使って、メソッド呼び出しを行う
            Object val = method.invoke(target, args.toArray());
            return val;
        }

        public List<Class<?>> argClasses(List<Object> args) {
            List<Class<?>> classes = new ArrayList<Class<?>>();
            int psize = args.size();
            for (int i = 0; i < psize; ++i) {
                Object a = args.get(i);
                if (a != null) {
                    classes.add(a.getClass());
                } else {
                    classes.add(null);
                }
            }
            return classes;
        }

        public List<Method> methodByName(Method[] methods, String name) {
            List<Method> ms = new ArrayList<Method>();
            for (Method m : methods) {
                if (m.getName().equals(name)) {
                    ms.add(m);
                }
            }
            return ms;
        }

        public List<Method> methodByAssignable(List<Method> methods, List<Class<?>> aClasses) {
            List<Method> candidates = new ArrayList<Method>();

            int aSize = aClasses.size();
            for (Method m : methods) {
                Class<?>[] pTypes = m.getParameterTypes();

                if (pTypes.length != aSize) {
                    continue;
                }

                Boolean allAssignable = true;
                for (int i = 0; i < aSize; ++i) {
                    Class<?> c = pTypes[i];
                    Class<?> cc;
                    if (c == int.class) {
                        cc = Integer.class;
                    } else {
                        cc = c;
                    }
                    Class<?> ac = aClasses.get(i);
                    if (ac != null) {
                        Class<?> acc;
                        if (ac == int.class) {
                            acc = Integer.class;
                        } else {
                            acc = ac;
                        }
                        allAssignable &= cc.isAssignableFrom(acc);
                    }
                    if (!allAssignable) {
                        break;
                    }
                }
                if (allAssignable) {
                    candidates.add(m);
                }
            }
            return candidates;
        }

        public List<Method> methodByAbsolute(List<Method> candidates, List<Class<?>> aClasses) {
            List<Method> screened = new ArrayList<Method>();
            int aSize = aClasses.size();
            for (int i = 0; i < aSize; ++i) {
                Class<?> ac = aClasses.get(i);
                if (ac == null) {
                    return screened;
                }
            }
            for (Method m : candidates) {
                Class<?>[] pTypes = m.getParameterTypes();
                Boolean allEquals = true;
                for (int i = 0; i < aSize; ++i) {
                    Class<?> c = pTypes[i];
                    Class<?> ac = aClasses.get(i);
                    allEquals &= c == ac;
                    if (!allEquals) {
                        break;
                    }
                }
                if (allEquals) {
                    screened.add(m);
                }
            }
            return screened;
        }
    }

    public static void main(String[] args) throws Exception {
        String text = "";
        text += "var m = {";
        text += "  key1: \"v1\",";
        text += "  key2: \"v2\",";
        text += "}";
        text += "println(m.size())";
        text += "println(m[\"key2\"])";
        List<Token> tokens = new Lexer().init(text).tokenize();
        List<Token> blk = new Parser().init(tokens).block();
        new Interpreter().init(blk).run();
        // --> 2
        // --> v2
    }
}
