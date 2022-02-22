package server;

import server.database.DatabaseController;
import server.database.JsonDatabase;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final int PORT = 8050;
    private static final File dbFile = new File("/Users/akot/IdeaProjects/JSON Database/JSON Database/task/src/server/data/db.json");

    public static void main(String[] args) {
        System.out.println("Server started!");

        int poolSize = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        JsonDatabase db = new JsonDatabase(dbFile);

        try (ServerSocket server = new ServerSocket(PORT)) {
            while (!server.isClosed()) {
                Socket socket = server.accept();
                executor.submit(() -> {
                    try (
                            DataInputStream input = new DataInputStream(socket.getInputStream());
                            DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                        ) {
                            String clientRequest = input.readUTF();
                            DatabaseController controller = new DatabaseController(server, db);
                            controller.setCommand(clientRequest);
                            String response = controller.executeCommand();
                            output.writeUTF(response);
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }
}
