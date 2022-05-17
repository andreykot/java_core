package search.core;

import java.util.ArrayList;
import java.util.Arrays;

@Deprecated
// public class LinearSearch implements SearchEngine {
public class LinearSearch {
    String[] input;
    ArrayList<String[]> parsedInput;

    public LinearSearch(String[] input) {
        this.input = input;
        this.parsedInput = parseInputData(input);
    }

    private ArrayList<String[]> parseInputData(String[] input) {
        ArrayList<String[]> parsedData = new ArrayList<>();
        for (String line: input) {
            String[] parsedLine = line.toLowerCase().split(" ");
            Arrays.parallelSetAll(parsedLine, (i) -> parsedLine[i].trim());
            parsedData.add(parsedLine);
        }
        return parsedData;
    }

    // @Override
    public String[] find(String pivot) {
        pivot = pivot.toLowerCase();
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            String[] line = parsedInput.get(i);
            for (String item: line) {
                if (item.equals(pivot)) {
                    result.add(input[i]);
                    break;
                }
            }
        }
        return result.toArray(String[]::new);
    }

    // @Override
    public String[] getAll() {
        return input;
    }
}
