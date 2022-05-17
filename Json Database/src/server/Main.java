package server;

import server.database.DatabaseController;
import server.database.JsonDatabase;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        Integer port = 8050;
        File dbFile = new File("/Users/akot/IdeaProjects/JSON Database/JSON Database/task/src/server/data/db.json");

        for (int i = 0; i < args.length; i += 2) {
            if ( i + 1 == args.length) {
                throw new java.lang.RuntimeException("Error");
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
