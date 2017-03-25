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
        body(body);
        return variables;
    }

    public void body(List<Token> body) throws Exception {
        for (Token exprs : body) {
            expression(exprs);
        }
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
        variables.put(variable.name, variable);
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
            context.body(block);
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String text = "";
        text += "v = 0";
        text += "function add3(a1, a2, a3) {";
        text += "  v = a1 + a2 + a3";
        text += "}";
        text += "add3(1,2,3)";
        text += "println(v)";
        List<Token> tokens = new Lexer().init(text).tokenize();
        List<Token> blk = new Parser().init(tokens).block();
        new Interpreter().init(blk).run();
        // --> 6
    }
}
