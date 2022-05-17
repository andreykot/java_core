import encryptdecrypt.Main;
import org.junit.Test;
import org.junit.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.nio.file.Files;

public class MyTests {

    @Test
    public void TestEmptyInput() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[] {
                "-mode", "enc",
                "-key", "-5",
                "-data", "",
                "-alg", "shift"
        });

        String correctOutput = "\n";
        Assert.assertEquals(correctOutput, outContent.toString());
    }

    @Test
    public void TestEncryptShift() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String correctOutput;

        Main.main(new String[] {
                "-mode", "enc",
                "-key", "5",
                "-data", "ABc xyz",
                "-alg", "shift"
        });

        correctOutput = "FGh cde\n";
        Assert.assertEquals(correctOutput, outContent.toString());
        outContent.reset();

        Main.main(new String[] {
                "-mode", "enc",
                "-key", "-5",
                "-data", "ABc xyz",
                "-alg", "shift"
        });

        correctOutput = "VWx stu\n";
        Assert.assertEquals(correctOutput, outContent.toString());
    }

    @Test
    public void TestDecryptShift() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String correctOutput = "ABc xyz\n";

        Main.main(new String[] {
                "-mode", "dec",
                "-key", "5",
                "-data", "FGh cde",
                "-alg", "shift"
        });
        Assert.assertEquals(correctOutput, outContent.toString());
        outContent.reset();

        Main.main(new String[] {
                "-mode", "dec",
                "-key", "-5",
                "-data", "VWx stu",
                "-alg", "shift"
        });
        Assert.assertEquals(correctOutput, outContent.toString());
    }

    @Test
    public void TestEncryptUnicode() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String correctOutput;

        Main.main(new String[] {
                "-mode", "enc",
                "-key", "5",
                "-data", "ABc xyz",
                "-alg", "unicode"
        });

        correctOutput = "\u0046\u0047\u0068\u0025\u007D\u007E\u007F\n";
        Assert.assertEquals(correctOutput, outContent.toString());
        outContent.reset();

        Main.main(new String[] {
                "-mode", "enc",
                "-key", "-5",
                "-data", "ABc xyz",
                "-alg", "unicode"
        });

        correctOutput = "\u003C\u003D\u005E\u001B\u0073\u0074\u0075\n";
        Assert.assertEquals(correctOutput, outContent.toString());
    }

    @Test
    public void TestDecryptUnicode() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String correctOutput = "\u0041\u0042\u0063\u0020\u0078\u0079\u007A\n";

        Main.main(new String[] {
                "-mode", "dec",
                "-key", "5",
                "-data", "\u0046\u0047\u0068\u0025\u007D\u007E\u007F",
                "-alg", "unicode"
        });

        Assert.assertEquals(correctOutput, outContent.toString());
        outContent.reset();

        Main.main(new String[] {
                "-mode", "dec",
                "-key", "-5",
                "-data", "\u003C\u003D\u005E\u001B\u0073\u0074\u0075",
                "-alg", "unicode"
        });
        Assert.assertEquals(correctOutput, outContent.toString());
    }

    @Test
    public void TestFilesUsage() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String correctOutput;

        correctOutput = "FGh cde";
        Main.main(new String[] {
                "-mode", "enc",
                "-key", "5",
                "-alg", "shift",
                "-in", "./test/data/test_data.txt"
        });
        Assert.assertEquals(correctOutput, outContent.toString().strip());

        Main.main(new String[] {
                "-mode", "enc",
                "-key", "5",
                "-alg", "shift",
                "-in", "./test/data/test_data.txt",
                "-out", "./test/data/output.txt"
        });

        try {
            String output = new String(Files.readAllBytes(Paths.get("./test/data/output.txt")));
            Assert.assertEquals(correctOutput, output.strip());
        } catch (IOException e) {
            throw new java.lang.RuntimeException();
        }

    }
}