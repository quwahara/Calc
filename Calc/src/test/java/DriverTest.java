import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Test;

public class DriverTest {
    
    Driver driver;

    @Before
    public void setUp() throws Exception {
        driver = new Driver();
    }

    @Test
    public void testValidateArguments_argsNull() {
        try {
            driver.validateArguments(null);
            fail();
        } catch (Exception e) {
            assertTrue(e.getMessage().startsWith(Driver.ARGS_NULL));
        }
    }

    @Test
    public void testValidateArguments_noArgs() {
        try {
            driver.validateArguments(new String[0]);
            fail();
        } catch (Exception e) {
            assertTrue(e.getMessage().startsWith(Driver.NO_ARGS));
        }
    }

    @Test
    public void testValidateArguments_fileNotFound() {
        try {
            String[] args = new String[1];
            args[0] = "x";
            driver.validateArguments(args);
            fail();
        } catch (Exception e) {
            assertTrue(e.getMessage().startsWith(Driver.FILE_NOT_FOUND));
        }
    }

    @Test
    public void testValidateArguments_PathNotFile() throws IOException {
        Path tmpdir = Files.createTempDirectory("");
        try {
            String[] args = new String[1];
            args[0] = tmpdir.toRealPath(LinkOption.NOFOLLOW_LINKS).toString();
            driver.validateArguments(args);
            fail();
            
            
        } catch (Exception e) {
            assertTrue(e.getMessage().startsWith(Driver.PATH_NOT_FILE));
        } finally {
            Files.delete(tmpdir);
        }
    }

    @Test
    public void testReadText() throws IOException {
        String expected = "text";
        File tmpf = File.createTempFile("tmp", null);     
        Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpf), "UTF8"));
        try {
            w.append(expected);
            w.flush();
            w.close();
            String actual = driver.readText(tmpf.getAbsolutePath());
            assertEquals(expected, actual);
        } finally {
            tmpf.delete();
        }
    }

    @Test
    public void testExecute_error() throws IOException {
        String expected = "v = ";
        File tmpf = File.createTempFile("tmp", null);     
        Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpf), "UTF8"));
        try {
            w.append(expected);
            w.flush();
            w.close();
            String[] args = new String[1];
            args[0] = tmpf.getAbsolutePath();
            int status = driver.execute(args);
            assertEquals(-1, status);
        } finally {
            tmpf.delete();
        }
    }

    @Test
    public void testExecute_ok() throws IOException {
        String expected = "v = 1";
        File tmpf = File.createTempFile("tmp", null);     
        Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpf), "UTF8"));
        try {
            w.append(expected);
            w.flush();
            w.close();
            String[] args = new String[1];
            args[0] = tmpf.getAbsolutePath();
            int status = driver.execute(args);
            assertEquals(0, status);
            assertEquals(1, driver.variables.get("v").value);
        } finally {
            tmpf.delete();
        }
    }

}
