import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter {

    public Map<String, Func> functions;
    public Map<String, Variable> variables;
    List<Token> body;

    public Interpreter init(List<Token> body) {
        functions = new HashMap<>();
        Func f = new Println();
        functions.put(f.name, f);
        variables = new HashMap<>();
        this.body = body;
        return this;
    }

    public Map<String, Variable> run() throws Exception {
        body(body, null, null);
        return variables;
    }

    public Object body(List<Token> body, boolean[] ret, boolean[] brk) throws Exception {
        for (Token exprs : body) {
            if (exprs.kind.equals("if")) {      // <-- Add
                Object val = if_(exprs, ret, brk);
                if (ret != null && ret[0]) {
                    return val;
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
        if (IsTrue(token.left)) {
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
        while (IsTrue(token.left)) {
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
    
    public boolean IsTrue(Token token) throws Exception {
        return 0 != value(expression(token));
    }

    public Object expression(Token expr) throws Exception {
        if (expr.kind.equals("digit")) {
            return digit(expr);
        } else if (expr.kind.equals("ident")) {
            return ident(expr);
        } else if (expr.kind.equals("func")) {
            return func(expr);
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
        if (functions.containsKey(name)) {
            return functions.get(name);
        }
        if (variables.containsKey(name)) {
            return variables.get(name);
        } else {
            Variable v = new Variable();
            v.name = name;
            v.value = 0;
            variables.put(name, v);
            return v;
        }
    }

    public Object func(Token token) throws Exception {
        String name = token.ident.value;
        if (functions.containsKey(name)) {
            throw new Exception("Name was used");
        }
        if (variables.containsKey(name)) {
            throw new Exception("Name was used");
        }
        for (Token p : token.params) {
            String param = p.value;
            if (functions.containsKey(param)) {
                throw new Exception("Parameter name was used");
            }
            if (variables.containsKey(param)) {
                throw new Exception("Parameter name was used");
            }
        }
        DynamicFunc func = new DynamicFunc();
        func.context = this;
        func.name = name;
        func.params = new ArrayList<Variable>();
        for (Token p : token.params) {
            func.params.add(variable(ident(p)));
        }
        func.block = token.block;
        functions.put(name, func);
        return null;
    }

    public Variable assign(Token expr) throws Exception {
        Variable variable = variable(expression(expr.left));
        Integer value = value(expression(expr.right));
        variable.value = value;
        return variable;
    }

    public Variable variable(Object value) throws Exception {
        if (value instanceof Variable) {
            return (Variable) value;
        } else {
            throw new Exception("left value error");
        }
    }

    public Integer value(Object value) throws Exception {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Variable) {
            Variable v = (Variable) value;
            return v.value;
        }
        throw new Exception("right value error");
    }

    public Object unaryCalc(Token expr) throws Exception {
        Integer left = value(expression(expr.left));
        if (expr.value.equals("+")) {
            return left;
        } else if (expr.value.equals("-")) {
            return -left;
        } else {
            throw new Exception("Unknown sign for unary calc");
        }
    }

    public Object calc(Token expr) throws Exception {
        Integer left = value(expression(expr.left));
        Integer right = value(expression(expr.right));
        if (expr.value.equals("+")) {
            return left + right;
        } else if (expr.value.equals("-")) {
            return left - right;
        } else if (expr.value.equals("*")) {
            return left * right;
        } else if (expr.value.equals("/")) {
            return left / right;
        } else {
            throw new Exception("Unknown sign for Calc");
        }
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
        } else {
            throw new Exception("Not a function");
        }
    }

    public static class Variable {
        public String name;
        public Integer value;

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
        public List<Variable> params;
        public List<Token> block;

        @Override
        public Object invoke(List<Object> args) throws Exception {
            for (int i = 0; i < params.size(); ++i) {
                Integer value;
                if (i < args.size()) {
                    value = context.value(args.get(i));
                } else {
                    value = null;
                }
                params.get(i).value = value;
            }
            boolean[] ret = new boolean[1];
            return context.body(block, ret, null);
        }
    }

    public static void main(String[] args) throws Exception {
        String text = "";
        text += "function f(a) {";
        text += "  if (a) {";
        text += "    return 3";
        text += "  } else {";
        text += "    return 4";
        text += "  }";
        text += "}";
        text += "println(f(1))";
        text += "println(f(0))";
        List<Token> tokens = new Lexer().init(text).tokenize();
        List<Token> blk = new Parser().init(tokens).block();
        new Interpreter().init(blk).run();
        // --> 3
        // --> 4
    }
}
