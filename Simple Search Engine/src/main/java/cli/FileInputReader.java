package cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class FileInputReader implements InputDataReader {
    private final String filePath;
    private String[] data;

    public FileInputReader(String filePath) {
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
