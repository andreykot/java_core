package cli;

import core.InvertedIndexSearch;
import core.Search;

import java.util.Scanner;

public class CommandLineSearch {

    private final Scanner scanner = new Scanner(System.in);
    private String inputFile;

    public CommandLineSearch() {}

    public CommandLineSearch(String inputFile) {
        this.inputFile = inputFile;
    }

    public void start() {
        String[] input = getInput();

        Search search = new Search();
        search.setEngine(new InvertedIndexSearch(input));

        Menu menu = new Menu(scanner, search);
        menu.run();
    }

    private String[] getInput() {
        InputDataReader input;
        if (inputFile == null) {
            input = new StandardInput(scanner);
        } else {
            input = new FileInput(inputFile);
        }
        input.read();
        return input.getData();
    }
}
