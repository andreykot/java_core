import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;


public class TestApp {

    @Test
    public void TestEmptyInput() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        App app = new App();
        app.initArgs(new String[] {
                "-mode", "enc",
                "-key", "-5",
                "-data", "",
                "-alg", "shift"
        });
        app.run();

        String correctOutput = "\n";
        assertEquals(correctOutput, outContent.toString());
    }

    @Test
    public void TestEncryptShift() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String correctOutput;

        App app1 = new App();
        app1.initArgs(new String[] {
                "-mode", "enc",
                "-key", "5",
                "-data", "ABc xyz",
                "-alg", "shift"
        });
        app1.run();

        correctOutput = "FGh cde\n";
        assertEquals(correctOutput, outContent.toString());
        outContent.reset();

        App app2 = new App();
        app2.initArgs(new String[] {
                "-mode", "enc",
                "-key", "-5",
                "-data", "ABc xyz",
                "-alg", "shift"
        });
        app2.run();

        correctOutput = "VWx stu\n";
        assertEquals(correctOutput, outContent.toString());
    }

    @Test
    public void TestDecryptShift() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String correctOutput = "ABc xyz\n";

        App app1 = new App();
        app1.initArgs(new String[] {
                "-mode", "dec",
                "-key", "5",
                "-data", "FGh cde",
                "-alg", "shift"
        });
        app1.run();

        assertEquals(correctOutput, outContent.toString());
        outContent.reset();

        App app2 = new App();
        app2.initArgs(new String[] {
                "-mode", "dec",
                "-key", "-5",
                "-data", "VWx stu",
                "-alg", "shift"
        });
        app2.run();
        assertEquals(correctOutput, outContent.toString());
    }

    @Test
    public void TestEncryptUnicode() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String correctOutput;

        App app1 = new App();
        app1.initArgs(new String[] {
                "-mode", "enc",
                "-key", "5",
                "-data", "ABc xyz",
                "-alg", "unicode"
        });
        app1.run();

        correctOutput = "\u0046\u0047\u0068\u0025\u007D\u007E\u007F\n";
        assertEquals(correctOutput, outContent.toString());
        outContent.reset();

        App app2 = new App();
        app2.initArgs(new String[] {
                "-mode", "enc",
                "-key", "-5",
                "-data", "ABc xyz",
                "-alg", "unicode"
        });
        app2.run();

        correctOutput = "\u003C\u003D\u005E\u001B\u0073\u0074\u0075\n";
        assertEquals(correctOutput, outContent.toString());
    }

    @Test
    public void TestDecryptUnicode() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String correctOutput = "\u0041\u0042\u0063\u0020\u0078\u0079\u007A\n";

        App app1 = new App();
        app1.initArgs(new String[] {
                "-mode", "dec",
                "-key", "5",
                "-data", "\u0046\u0047\u0068\u0025\u007D\u007E\u007F",
                "-alg", "unicode"
        });
        app1.run();

        assertEquals(correctOutput, outContent.toString());
        outContent.reset();

        App app2 = new App();
        app2.initArgs(new String[] {
                "-mode", "dec",
                "-key", "-5",
                "-data", "\u003C\u003D\u005E\u001B\u0073\u0074\u0075",
                "-alg", "unicode"
        });
        app2.run();
        assertEquals(correctOutput, outContent.toString());
    }

    @Test
    public void TestFilesUsage() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String correctOutput;

        correctOutput = "FGh cde";
        App app1 = new App();
        app1.initArgs(new String[] {
                "-mode", "enc",
                "-key", "5",
                "-alg", "shift",
                "-in", "src/test/resources/test_data.txt"
        });
        app1.run();
        assertEquals(correctOutput, outContent.toString().strip());

        App app2 = new App();
        app2.initArgs(new String[] {
                "-mode", "enc",
                "-key", "5",
                "-alg", "shift",
                "-in", "src/test/resources/test_data.txt",
                "-out", "src/test/resources/output.txt"
        });
        app2.run();
        try {
            String output = new String(Files.readAllBytes(Paths.get("src/test/resources/output.txt")));
            assertEquals(correctOutput, output.strip());
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }
}