package search.cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

interface InputData {
    void read();
    String[] getData();
}

class FileInput implements InputData {
    String filePath;
    String[] data;

    public FileInput(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void read() {
        try {
            String input = new String(Files.readAllBytes(Paths.get(filePath)));
            data = input.split("\\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String[] getData() {
        return data != null ? data : new String[0];
    }
}

class StandardInput implements InputData {
    Scanner scanner;
    String[] data;

    public StandardInput(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void read() {
        System.out.println("Enter the number of people:");
        int inputCount = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter all people:");
        data = new String[inputCount];
        for (int i = 0; i < inputCount; i++) {
            data[i] = scanner.nextLine();
        }
    }

    @Override
    public String[] getData() {
        return data != null ? data : new String[0];
    }
}
