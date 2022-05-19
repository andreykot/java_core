class UnicodeShifting extends EncryptionAlgorithm {

    public UnicodeShifting(int key) {
        super(key);
    }

    @Override
    public String encode(String input) {
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char value = (char) (input.charAt(i) + key);
            encrypted.append(value);
        }
        return encrypted.toString();
    }

    @Override
    public String decode(String input) {
        StringBuilder decrypted = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char value = (char) (input.charAt(i) - key);
            decrypted.append(value);
        }
        return decrypted.toString();
    }
}
