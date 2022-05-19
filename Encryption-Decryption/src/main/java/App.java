import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {

    String mode = "enc";
    int key = 0;
    String data = "";
    String out = "";
    String in = "";
    String alg = "shift";

    public void run() {
        EncryptionFactory algFactory = new EncryptionFactory();
        EncryptionAlgorithm algorithm = algFactory.getAlgorithm(alg, key);
        String input = prepareInput(data, in);

        String result;
        if (mode.equals("enc")) {
            result = algorithm.encode(input);
        } else if (mode.equals("dec")) {
            result = algorithm.decode(input);
        } else {
            throw new RuntimeException("Invalid mode.");
        }

        makeOutput(out, result);
    }

    public void initArgs(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            if ( i + 1 == args.length) {
                throw new RuntimeException("Error");
            }
            switch (args[i]) {
                case "-mode":
                    mode = args[i + 1];
                    break;
                case "-key":
                    key = Integer.parseInt(args[i + 1]);
                    break;
                case "-data":
                    data = args[i + 1];
                    break;
                case "-out":
                    out = args[i + 1];
                    break;
                case "-in":
                    in = args[i + 1];
                    break;
                case "-alg":
                    alg = args[i + 1];
            }
        }
    }

    private static String prepareInput(String inputData, String inputFile) {
        if (!inputData.isEmpty()) {
            return inputData;
        } else if (!inputFile.isEmpty()) {
            try {
                return new String(Files.readAllBytes(Paths.get(inputFile)));
            } catch (IOException e) {
                throw new RuntimeException("Input File Error");
            }
        } else {
            return "";
        }
    }

    private static void makeOutput(String outPath, String data) {
        if (outPath.equals("")) {
            System.out.println(data);
        } else {
            File outFile = new File(outPath);
            try (FileWriter writer = new FileWriter(outFile)) {
                writer.write(data);
            } catch (IOException e) {
                throw new RuntimeException("Write Error");
            }
        }
    }
}

