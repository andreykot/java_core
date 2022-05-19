import server.Server;

import java.io.File;

public class ServerThread {
    String TEST_SERVER_ADDRESS = "127.0.0.1";
    int TEST_SERVER_PORT = 8052;
    String TEST_DB_FILE = "src/test/resources/db.json";

    public Thread createTestServer() {
        Thread serverThread = new Thread(() -> {
            Server server = new Server(TEST_SERVER_PORT, new File(TEST_DB_FILE));
            server.run();
        });
        serverThread.start();
        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return serverThread;
    }

    public String getServerAddress() {
        return TEST_SERVER_ADDRESS;
    }

    public int getServerPort() {
        return TEST_SERVER_PORT;
    }
}
