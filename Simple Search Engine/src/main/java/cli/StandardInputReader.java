package cli;

import java.util.Scanner;

class StandardInputReader implements InputDataReader {
    private Scanner scanner;
    private String[] data;

    public StandardInputReader(Scanner scanner) {
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
