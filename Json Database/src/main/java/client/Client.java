package client;

import com.beust.jcommander.JCommander;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    final private String serverAddress;
    final private int serverPort;
    private String request;

    public Client (String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void setCommand(String[] args) {
        QueryBuilder query = new QueryBuilder();
        JCommander.newBuilder()
                .addObject(query)
                .build()
                .parse(args);

        this.request = query.build();
    }

    public String executeCommand() {
        try (
                Socket socket = new Socket(serverAddress, serverPort);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client started!");

            output.writeUTF(request);
            System.out.println("Sent: " + request);

            String receivedMsg = input.readUTF();
            System.out.println("Received: " + receivedMsg);

            return receivedMsg;

        } catch (IOException e) {
            System.out.println("ERROR. Server is offline.");
            e.printStackTrace();
            return "";
        }
    }
}
