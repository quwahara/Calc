import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Driver {

    Lexer lexer;
    Parser parser;
    Interpreter interpreter;
    
    public Driver() {
        lexer = new Lexer();
        parser = new Parser();
        interpreter = new Interpreter();
    }
    
    public Map<String, Interpreter.Variable> variables;
    public Exception exception;
    
    public int execute(String[] args) {
        try {
            validateArguments(args);
            String path = args[0];
            String text = readText(path);
            variables = run(text);
            return 0;
        } catch (Exception e) {
            exception = e;
            System.err.println(e.getMessage());
            return -1;
        }
    }

    public static final String ARGS_NULL = "Arguments were null.";
    public static final String NO_ARGS = "No Arguments.";
    public static final String FILE_NOT_FOUND = "File not found.: ";
    public static final String PATH_NOT_FILE = "The path was not a file.: ";
    
    public void validateArguments(String[] args) throws Exception {
        if (args == null) {
            throw new Exception(ARGS_NULL);
        }
        if (args.length != 1) {
            throw new Exception(NO_ARGS);
        }
        String path = args[0];
        File f = new File(path);
        if (!f.exists()) {
            throw new FileNotFoundException(FILE_NOT_FOUND + path);
        }
        if (!f.isFile()) {
            throw new Exception(PATH_NOT_FILE + path);
        }
    }
    
    public String readText(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }
    
    public Map<String, Interpreter.Variable> run(String text) throws Exception {
        exception = null;
        variables = null;
        List<Token> tokens = lexer.init(text).tokenize();
        List<Token> blk = parser.init(tokens).block();
        return interpreter.init(blk).run();
    }
    
    public static void main(String[] args) {
        Driver driver = new Driver();
        int status = driver.execute(args);
        System.exit(status);
    }
    
}
