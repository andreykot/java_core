package core;

import java.util.*;

public class InvertedIndexSearch implements SearchEngine {
    private final String[] input;
    private final HashMap<String, ArrayList<Integer>> invertedIndex = new HashMap<>();

    public InvertedIndexSearch(String[] input) {
        this.input = input;
        populateIndex(input);
    }

    private void populateIndex(String[] input) {
        for (int i = 0; i < input.length; i++) {
            String[] parsedLine = input[i].toLowerCase().split(" ");
            for (String rawItem: parsedLine) {
                String item = rawItem.trim().toLowerCase();
                if (invertedIndex.containsKey(item)) {
                    invertedIndex.get(item).add(i);
                } else {
                    ArrayList<Integer> array = new ArrayList<>();
                    array.add(i);
                    invertedIndex.put(item, array);
                }
            }

        }
    }

    private String[] parsePivot(String pivot) {
        String[] parsedLine = pivot.toLowerCase().split(" ");
        Arrays.parallelSetAll(parsedLine, (i) -> parsedLine[i].trim().toLowerCase());
        return parsedLine;
    }

    @Override
    public String[] findLine(String pivot) {
        pivot = pivot.toLowerCase();
        if (invertedIndex.containsKey(pivot)) {
            ArrayList<Integer> indexes = invertedIndex.get(pivot);
            String[] result = new String[indexes.size()];
            for (int i = 0; i < indexes.size(); i++) {
                result[i] = input[indexes.get(i)];
            }
            return result;
        } else {
            return new String[0];
        }
    }

    @Override
    public String[] findAll(String pivot) {
        String[] pivots = parsePivot(pivot);
        HashSet<Integer> resultIndexes = new HashSet<>();
        for (String word: pivots) {
            if (resultIndexes.isEmpty() && invertedIndex.containsKey(word)) {
                resultIndexes.addAll(invertedIndex.get(word));
            } else if (invertedIndex.containsKey(word)) {
                resultIndexes.retainAll(invertedIndex.get(word));
            } else {
                return new String[0];
            }
        }
        ArrayList<String> result = new ArrayList<>();
        for (int i: resultIndexes) {
            result.add(input[i]);
        }
        return result.toArray(String[]::new);

    }

    @Override
    public String[] findAny(String pivot) {
        String[] pivots = parsePivot(pivot);
        HashSet<Integer> resultIndexes = new HashSet<>();
        for (String word: pivots) {
            if (invertedIndex.containsKey(word)) {
                resultIndexes.addAll(invertedIndex.get(word));
            }
        }
        ArrayList<String> result = new ArrayList<>();
        for (int i: resultIndexes) {
            result.add(input[i]);
        }
        return result.toArray(String[]::new);

    }

    @Override
    public String[] findDifferent(String pivot) {
        String[] pivots = parsePivot(pivot);
        HashSet<Integer> resultIndexes = new HashSet<>();
        for (String word: pivots) {
            if (invertedIndex.containsKey(word)) {
                resultIndexes.addAll(invertedIndex.get(word));
            }
        }

        HashSet<Integer> differentIndexes = new HashSet<>();
        for (int i = 0; i < input.length; i++) {
            if (!resultIndexes.contains(i)) {
                differentIndexes.add(i);
            }
        }

        ArrayList<String> result = new ArrayList<>();
        for (int i: differentIndexes) {
            result.add(input[i]);
        }
        return result.toArray(String[]::new);

    }

    @Override
    public String[] getAll() {
        return input;
    }
}
