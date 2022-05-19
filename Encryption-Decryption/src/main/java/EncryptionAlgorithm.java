abstract class EncryptionAlgorithm {

    int key;

    public EncryptionAlgorithm(int key) {
        this.key = key;
    }

    abstract String encode(String input);

    abstract String decode(String input);
}
