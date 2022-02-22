package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public String SERVER_ADDRESS ="127.0.0.1";
    public int SERVER_PORT = 8050;
    private String request;

    public void setCommand(String request) {
        this.request = request;
    }

    public void executeCommand() {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client started!");

            output.writeUTF(request);
            System.out.println("Sent: " + request);

            String receivedMsg = input.readUTF();
            System.out.println("Received: " + receivedMsg);

        } catch (IOException e) {
            System.out.println("ERROR. Server is offline.");
            e.printStackTrace();
        }
    }
}
