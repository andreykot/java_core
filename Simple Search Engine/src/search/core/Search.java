package search.core;

public class Search {
    SearchEngine engine;
    SearchType type;

    public void setEngine(SearchEngine engine) {
        this.engine = engine;
    }

    public void setType(SearchType type) {
        this.type = type;
    }

    public String[] find(String pivot) {
        switch (type) {
            case ALL:
                return engine.findAll(pivot);
            case NONE:
                return engine.findDifferent(pivot);
            default:
                return engine.findAny(pivot);
        }
    }

    public String[] getAll() {
        return engine.getAll();
    }
}
