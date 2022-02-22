package encryptdecrypt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        String mode = "enc";
        int key = 0;
        String data = "";
        String out = "";
        String in = "";
        String alg = "shift";

        for (int i = 0; i < args.length; i += 2) {
            if ( i + 1 == args.length) {
                throw new java.lang.RuntimeException("Error");
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

        EncryptionFactory algFactory = new EncryptionFactory();
        EncryptionAlgorithm algorithm = algFactory.getAlgorithm(alg, key);
        String input = prepareInput(data, in);

        String result;
        if (mode.equals("enc")) {
            result = algorithm.encode(input);
        } else if (mode.equals("dec")) {
            result = algorithm.decode(input);
        } else {
            throw new java.lang.RuntimeException("Invalid mode.");
        }

        makeOutput(out, result);
    }

    private static String prepareInput(String inputData, String inputFile) {
        if (!inputData.isEmpty()) {
            return inputData;
        } else if (!inputFile.isEmpty()) {
            try {
                return new String(Files.readAllBytes(Paths.get(inputFile)));
            } catch (IOException e) {
                throw new java.lang.RuntimeException("Input File Error");
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
                throw new java.lang.RuntimeException("Write Error");
            }
        }
    }
}

abstract class EncryptionAlgorithm {

    int key;

    public EncryptionAlgorithm(int key) {
        this.key = key;
    }

    abstract String encode(String input);

    abstract String decode(String input);
}

class AlphabetShifting extends EncryptionAlgorithm {

    public AlphabetShifting(int key) {
        super(key);
    }

    @Override
    public String encode(String input) {
        StringBuilder encrypted = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char input_val = input.charAt(i);

            if (Character.isAlphabetic(input_val)) {
                char result;
                if (Character.isUpperCase(input_val)) {
                    int shifted_val = input_val - 65 + key;
                    int encoded_val = shifted_val >= 0 ? shifted_val : 26 + shifted_val;
                    result = (char) (encoded_val % 26 + 65);
                } else {
                    int shifted_val = input_val - 97 + key;
                    int encoded_val = shifted_val >= 0 ? shifted_val : 26 + shifted_val;
                    result = (char) (encoded_val % 26 + 97);
                }
                encrypted.append(result);
            } else {
                encrypted.append(input_val);
            }
        }
        return encrypted.toString();
    }

    @Override
    public String decode(String input) {
        StringBuilder encrypted = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char input_val = input.charAt(i);

            if (Character.isAlphabetic(input_val)) {
                char result;
                if (Character.isUpperCase(input_val)) {
                    int shifted_val = input_val - 65 - key;
                    int encoded_val = shifted_val >= 0 ? shifted_val : 26 + shifted_val;
                    result = (char) (encoded_val % 26 + 65);
                } else {
                    int shifted_val = input_val - 97 - key;
                    int encoded_val = shifted_val >= 0 ? shifted_val : 26 + shifted_val;
                    result = (char) (encoded_val % 26 + 97);
                }
                encrypted.append(result);
            } else {
                encrypted.append(input_val);
            }
        }
        return encrypted.toString();
    }
    }

class UnicodeShifting extends EncryptionAlgorithm {

    public UnicodeShifting(int key) {
        super(key);
    }

    @Override
    public String encode(String input) {
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char value = (char) (input.charAt(i) + key);
            encrypted.append(value);
        }
        return encrypted.toString();
    }

    @Override
    public String decode(String input) {
        StringBuilder decrypted = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char value = (char) (input.charAt(i) - key);
            decrypted.append(value);
        }
        return decrypted.toString();
    }
}

class EncryptionFactory {
    public EncryptionAlgorithm getAlgorithm(String type, int key) {
        switch (type.toLowerCase()) {
            case "shift":
                return new AlphabetShifting(key);
            case "unicode":
                return new UnicodeShifting(key);
            default:
                throw new java.lang.RuntimeException(String.format("Unknown algorithm: %s", type));
        }
    }
}
