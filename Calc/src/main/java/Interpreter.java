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
        } else if (expr.kind.equals("ident")) { // <-- Update
            return ident(expr);
        } else if (expr.kind.equals("paren")) { // <-- Add
            return invoke(expr);
        } else if (expr.kind.equals("sign") && expr.value.equals("=")) {
            return assign(expr);
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
        Integer value = value(expression(expr.right));
        return f.invoke(value);
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

        abstract public Object invoke(Object arg);
    }

    public static class Println extends Func {
        public Println() {
            name = "println";
        }

        @Override
        public Object invoke(Object arg) {
            System.out.println(arg);
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String text = "a = (3 + 4) * 5";
        text += "println(a)";
        List<Token> tokens = new Lexer().init(text).tokenize();
        List<Token> blk = new Parser().init(tokens).block();
        new Interpreter().init(blk).run();
        // --> 35
    }
}
