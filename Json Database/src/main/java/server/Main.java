package server;

import java.io.File;


public class Main {

    public static void main(String[] args) {

        int port = 8050;
        File dbFile = new File("src/main/resources/db.json");

        if (args.length > 0) {
            for (int i = 0; i < args.length; i += 2) {
                if ( i + 1 == args.length) {
                    throw new RuntimeException("Error");
                }
                switch (args[i]) {
                    case "-port":
                        port = Integer.parseInt(args[i + 1]);
                        break;
                    case "-dbfile":
                        dbFile = new File(args[i + 1]);
                        break;
                }
            }
        }

        Server server = new Server(port, dbFile);
        server.run();
    }
}
