package core;

public interface SearchEngine {

    String[] findLine(String pivot);

    String[] findAll(String pivot);

    String[] findAny(String pivot);

    String[] findDifferent(String pivot);

    String[] getAll();

}
