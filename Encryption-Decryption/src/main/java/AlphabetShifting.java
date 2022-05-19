class AlphabetShifting extends EncryptionAlgorithm {

    public AlphabetShifting(int key) {
        super(key);
    }

    @Override
    public String encode(String input) {
        StringBuilder encrypted = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char input_val = input.charAt(i);

            if (Character.isAlphabetic(input_val)) {
                char result;
                if (Character.isUpperCase(input_val)) {
                    int shifted_val = input_val - 65 + key;
                    int encoded_val = shifted_val >= 0 ? shifted_val : 26 + shifted_val;
                    result = (char) (encoded_val % 26 + 65);
                } else {
                    int shifted_val = input_val - 97 + key;
                    int encoded_val = shifted_val >= 0 ? shifted_val : 26 + shifted_val;
                    result = (char) (encoded_val % 26 + 97);
                }
                encrypted.append(result);
            } else {
                encrypted.append(input_val);
            }
        }
        return encrypted.toString();
    }

    @Override
    public String decode(String input) {
        StringBuilder encrypted = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char input_val = input.charAt(i);

            if (Character.isAlphabetic(input_val)) {
                char result;
                if (Character.isUpperCase(input_val)) {
                    int shifted_val = input_val - 65 - key;
                    int encoded_val = shifted_val >= 0 ? shifted_val : 26 + shifted_val;
                    result = (char) (encoded_val % 26 + 65);
                } else {
                    int shifted_val = input_val - 97 - key;
                    int encoded_val = shifted_val >= 0 ? shifted_val : 26 + shifted_val;
                    result = (char) (encoded_val % 26 + 97);
                }
                encrypted.append(result);
            } else {
                encrypted.append(input_val);
            }
        }
        return encrypted.toString();
    }
}
