
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {

    private Map<String, Integer> degrees;
    private List<String> factorKinds;
    private List<String> binaryKinds;
    private List<String> rightAssocs;
    private List<String> unaryOperators;
    private List<String> reserved;

    public Parser() {
        degrees = new HashMap<>();
        degrees.put("(", 80);
        degrees.put("*", 60);
        degrees.put("/", 60);
        degrees.put("+", 50);
        degrees.put("-", 50);
        degrees.put("==", 40);
        degrees.put("!=", 40);
        degrees.put("<", 40);
        degrees.put("<=", 40);
        degrees.put(">", 40);
        degrees.put(">=", 40);
        degrees.put("&&", 30);
        degrees.put("||", 30);
        degrees.put("=", 10);
        factorKinds = Arrays.asList(new String[] { "digit", "ident" });
        binaryKinds = Arrays.asList(new String[] { "sign" });
        rightAssocs = Arrays.asList(new String[] { "=" });
        unaryOperators = Arrays.asList(new String[] { "+", "-", "!" });
        // Update
        reserved = Arrays.asList(new String[] { "function", "return", "if", "else", "while", "break", "var" });
    }

    private List<Token> tokens;
    private int i;

    public Parser init(List<Token> tokens) {
        i = 0;
        this.tokens = new ArrayList<Token>(tokens);
        Token eob = new Token();
        eob.kind = "eob";
        eob.value = "(eob)";
        this.tokens.add(eob);
        return this;
    }

    private Token token() throws Exception {
        if (tokens.size() <= i) {
            throw new Exception("No more token");
        }
        return tokens.get(i);
    }

    private Token next() throws Exception {
        Token t = token();
        ++i;
        return t;
    }

    private Token consume(String expectedValue) throws Exception {
        if (!expectedValue.equals(token().value)) {
            throw new Exception("Not expected value");
        }
        return next();
    }

    private Token lead(Token token) throws Exception {
        if (token.kind.equals("ident") && token.value.equals("function")) {
            return func(token);
        } else if (token.kind.equals("ident") && token.value.equals("return")) {
            token.kind = "ret";
            if (!token().kind.equals("eob")) {
                token.left = expression(0);
            }
            return token;
        } else if (token.kind.equals("ident") && token.value.equals("if")) {
            return if_(token);
        } else if (token.kind.equals("ident") && token.value.equals("while")) {
            return while_(token);
        } else if (token.kind.equals("ident") && token.value.equals("break")) {
            token.kind = "brk";
            return token;
            // Add
        } else if (token.kind.equals("ident") && token.value.equals("var")) {
            return var(token);
        } else if (factorKinds.contains(token.kind)) {
            return token;
        } else if (unaryOperators.contains(token.value)) {
            token.kind = "unary";
            token.left = expression(70);
            return token;
        } else if (token.kind.equals("paren") && token.value.equals("(")) {
            Token expr = expression(0);
            consume(")");
            return expr;
        } else {
            throw new Exception("The token cannot place there.");
        }
    }

    private Token func(Token token) throws Exception {
        if (token().value.equals("(")) {
            token.kind = "fexpr";
        } else {
            token.kind = "func";
            token.ident = ident();
        }
        consume("(");
        token.params = new ArrayList<Token>();
        if (!token().value.equals(")")) {
            token.params.add(ident());
            while (!token().value.equals(")")) {
                consume(",");
                token.params.add(ident());
            }
        }
        consume(")");
        token.block = body();
        return token;
    }

    private Token if_(Token token) throws Exception {
        token.kind = "if";
        consume("(");
        token.left = expression(0);
        consume(")");
        if (token().value.equals("{")) {
            token.block = body();
        } else {
            token.block = new ArrayList<Token>();
            token.block.add(expression(0));
        }
        if (token().value.equals("else")) {
            consume("else");
            if (token().value.equals("{")) {
                token.blockOfElse = body();
            } else {
                token.blockOfElse = new ArrayList<Token>();
                token.blockOfElse.add(expression(0));
            }
        }
        return token;
    }

    private Token while_(Token token) throws Exception {
        token.kind = "while";
        consume("(");
        token.left = expression(0);
        consume(")");
        if (token().value.equals("{")) {
            token.block = body();
        } else {
            token.block = new ArrayList<Token>();
            token.block.add(expression(0));
        }
        return token;
    }

    private Token var(Token token) throws Exception {
        token.kind = "var";
        token.block = new ArrayList<Token>();
        Token item;
        Token ident = ident();
        if (token().value.equals("=")) {
            Token operator = next();
            item = bind(ident, operator);
        } else {
            item = ident;
        }
        token.block.add(item);
        while (token().value.equals(",")) {
            next();
            ident = ident();
            if (token().value.equals("=")) {
                Token operator = next();
                item = bind(ident, operator);
            } else {
                item = ident;
            }
            token.block.add(item);
        }
        return token;
    }

    private Token ident() throws Exception {
        Token id = next();
        if (!id.kind.equals("ident")) {
            throw new Exception("Not an identical token.");
        }
        if (reserved.contains(id.value)) {
            throw new Exception("The token was reserved.");
        }
        return id;
    }

    private int degree(Token t) {
        if (degrees.containsKey(t.value)) {
            return degrees.get(t.value);
        } else {
            return 0;
        }
    }

    private Token bind(Token left, Token operator) throws Exception {
        if (binaryKinds.contains(operator.kind)) {
            operator.left = left;
            int leftDegree = degree(operator);
            if (rightAssocs.contains(operator.value)) {
                leftDegree -= 1;
            }
            operator.right = expression(leftDegree);
            return operator;
        } else if (operator.kind.equals("paren") && operator.value.equals("(")) {
            operator.left = left;
            operator.params = new ArrayList<Token>();
            if (!token().value.equals(")")) {
                operator.params.add(expression(0));
                while (!token().value.equals(")")) {
                    consume(",");
                    operator.params.add(expression(0));
                }
            }
            consume(")");
            return operator;
        } else {
            throw new Exception("The token cannot place there.");
        }
    }

    public Token expression(int leftDegree) throws Exception {
        Token left = lead(next());
        int rightDegree = degree(token());
        while (leftDegree < rightDegree) {
            Token operator = next();
            left = bind(left, operator);
            rightDegree = degree(token());
        }
        return left;
    }

    private List<Token> body() throws Exception {
        consume("{");
        List<Token> block = block();
        consume("}");
        return block;
    }

    public List<Token> block() throws Exception {
        List<Token> blk = new ArrayList<Token>();
        while (!token().kind.equals("eob")) {
            blk.add(expression(0));
        }
        return blk;
    }

    public static void main(String[] args) throws Exception {
        String text = "a = (3 + 4) * 5";
        List<Token> tokens = new Lexer().init(text).tokenize();
        List<Token> blk = new Parser().init(tokens).block();
        for (Token ast : blk) {
            System.out.println(ast.paren());
        }
        // --> (a = (3 + (4 * 5)))
    }
}
