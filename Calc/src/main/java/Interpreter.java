import java.util.ArrayList;
import java.util.HashMap;
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
            } else if (exprs.kind.equals("var")) { // <-- Add
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
        } else if (expr.kind.equals("ident")) {
            return ident(expr);
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
        } else {
            throw new Exception("Expression error");
        }
    }

    public Integer digit(Token token) {
        return Integer.decode(token.value);
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
        func.context = this;
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
        FuncExpr func = new FuncExpr();
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
            return (Integer) value;
        } else if (value instanceof FuncExpr) {
            return (FuncExpr) value;
        } else if (value instanceof Variable) {
            Variable v = (Variable) value;
            return value(v.value);
        }
        throw new Exception("right value error");
    }

    public Integer integer(Object value) throws Exception {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Variable) {
            Variable v = (Variable) value;
            return integer(v.value);
        }
        throw new Exception("right value error");
    }

    public Object unaryCalc(Token expr) throws Exception {
        Integer left = integer(expression(expr.left));
        if (expr.value.equals("+")) {
            return left;
        } else if (expr.value.equals("-")) {
            return -left;
        } else if (expr.value.equals("!")) {
            return toInteger(!isTrue(left));
        } else {
            throw new Exception("Unknown sign for unary calc");
        }
    }

    public Object calc(Token expr) throws Exception {
        Integer left = integer(expression(expr.left));
        Integer right = integer(expression(expr.right));
        if (expr.value.equals("+")) {
            return left + right;
        } else if (expr.value.equals("-")) {
            return left - right;
        } else if (expr.value.equals("*")) {
            return left * right;
        } else if (expr.value.equals("/")) {
            return left / right;
        } else if (expr.value.equals("==")) {
            return toInteger(left == right);
        } else if (expr.value.equals("!=")) {
            return toInteger(left != right);
        } else if (expr.value.equals("<")) {
            return toInteger(left < right);
        } else if (expr.value.equals("<=")) {
            return toInteger(left <= right);
        } else if (expr.value.equals(">")) {
            return toInteger(left > right);
        } else if (expr.value.equals(">=")) {
            return toInteger(left >= right);
        } else if (expr.value.equals("&&")) {
            return toInteger(isTrue(left) && isTrue(right));
        } else if (expr.value.equals("||")) {
            return toInteger(isTrue(left) || isTrue(right));
        } else {
            throw new Exception("Unknown sign for Calc");
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
        if (value instanceof Variable) {
            return func(((Variable) value).value);
        } else if (value instanceof Func) {
            return (Func) value;
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

    public static class FuncExpr extends Func {

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
                    v.value = context.integer(args.get(i));
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
        
    public static void main(String[] args) throws Exception {
        String text = "";
        text += "counter = (function() {";
        text += "  var c = 0";
        text += "  return function() {";
        text += "    c = c + 1";
        text += "    return c";
        text += "  }";
        text += "})()";
        text += "println(counter())";
        text += "println(counter())";
        List<Token> tokens = new Lexer().init(text).tokenize();
        List<Token> blk = new Parser().init(tokens).block();
        new Interpreter().init(blk).run();
        // --> 1
        // --> 2
    }
}
