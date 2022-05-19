class EncryptionFactory {
    public EncryptionAlgorithm getAlgorithm(String type, int key) {
        switch (type.toLowerCase()) {
            case "shift":
                return new AlphabetShifting(key);
            case "unicode":
                return new UnicodeShifting(key);
            default:
                throw new RuntimeException(String.format("Unknown algorithm: %s", type));
        }
    }
}
