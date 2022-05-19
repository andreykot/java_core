import cli.CommandLineSearch;

public class Main {

    public static void main(String[] args) {
        String inputFile = "";
        for (int i = 0; i < args.length; i += 2) {
            if ( i + 1 == args.length) {
                throw new RuntimeException("Invalid arguments.");
            }
            switch (args[i]) {
                case "--data":
                    inputFile = args[i + 1];
                    break;
            }
        }

        CommandLineSearch program;
        if (inputFile.isEmpty()) {
            program = new CommandLineSearch();
        } else {
            program = new CommandLineSearch(inputFile);
        }
        program.start();

    }
}
