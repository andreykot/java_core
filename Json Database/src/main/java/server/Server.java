package server;

import server.database.DatabaseController;
import server.database.JsonDatabase;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final int port;
    private final File dbFile;

    public Server (Integer port, File dbFile) {
        this.port = port;
        this.dbFile = dbFile;
    }

    public void run() {
        System.out.println("Server started!");
        System.out.println("DB File Path: " + dbFile.toPath());

        int poolSize = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        JsonDatabase db = new JsonDatabase(dbFile);

        try (ServerSocket server = new ServerSocket(port)) {
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