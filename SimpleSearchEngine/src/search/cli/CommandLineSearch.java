package search.cli;

import search.core.InvertedIndexSearch;
import search.core.Search;

import java.util.Scanner;

public class CommandLineSearch {

    Scanner scanner = new Scanner(System.in);
    String inputFile;

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
        InputData input;
        if (inputFile == null) {
            input = new StandardInput(scanner);
        } else {
            input = new FileInput(inputFile);
        }
        input.read();
        return input.getData();
    }
}
