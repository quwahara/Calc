import java.util.List;

public class Token {

    public String kind;
    public String value;
    public Token left;
    public Token right;
    public Token ident;
    public Token param;
    public List<Token> block;
    
    @Override
    public String toString() {
        return kind + " \"" + value + "\"";
    }

    public String indent(String parentIndent) {
        String indent = "  ";
        StringBuilder b = new StringBuilder();
        b.append(parentIndent).append(toString()).append("\n");
        if (left != null) {
            b.append(parentIndent).append("[left]\n").append(left.indent(parentIndent + indent));
        }
        if (right != null) {
            b.append(parentIndent).append("[right]\n").append(right.indent(parentIndent + indent));
        }
        if (ident != null) {
            b.append(parentIndent).append("[ident]\n").append(ident.indent(parentIndent + indent));
        }
        if (param != null) {
            b.append(parentIndent).append("[param]\n").append(param.indent(parentIndent + indent));
        }
        if (block != null) {
            b.append(parentIndent).append("[block]\n");
            for (Token expr : block) {
                b.append(expr.indent(parentIndent + indent));
            }
        }
        return b.toString();
    }

    public String paren() {
        if (left == null && right == null) {
            return value;
        } else if (left != null && right == null) {
            return value + left.paren();
        } else {
            StringBuilder b = new StringBuilder();
            b.append("(");
            if (left != null) {
                b.append(left.paren()).append(" ");
            }
            b.append(value);
            if (right != null) {
                b.append(" ").append(right.paren());
            }
            b.append(")");
            return b.toString();
        }
    }

}
