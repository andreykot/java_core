import client.Client;
import com.google.gson.Gson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import java.util.HashMap;

public class TestClientServer {

    @Test
    public void TestClientServerConnection() {
        ServerThread testServer = new ServerThread();
        Thread serverInstance = testServer.createTestServer();

        try {
            Client client = new Client(testServer.getServerAddress(), testServer.getServerPort());
            client.setCommand(new String[] {"-t", "Invalid Type"});
            String response = client.executeCommand();
            HashMap<String, String> res = new Gson().fromJson(response, HashMap.class);
            assertEquals(
                    res,
                    new HashMap<String, String>() {{
                        put("response", "ERROR");
                        put("reason", "Invalid type of request");
                    }}
            );
        } finally {
            serverInstance.interrupt();
        }

    }

    @Test
    public void TestCloseServer() {
        ServerThread testServer = new ServerThread();
        Thread serverInstance = testServer.createTestServer();

        try {
            // send request from a client
            Client client = new Client(testServer.getServerAddress(), testServer.getServerPort());
            client.setCommand(new String[] {"-t", "exit"});
            String response = client.executeCommand();
            HashMap<String, String> res = new Gson().fromJson(response, HashMap.class);
            assertEquals(
                    res,
                    new HashMap<String, String>() {{
                        put("response", "OK");
                    }}
            );

            // wait closing the server (5 seconds)
            try {
                Thread.sleep(5000);
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            // check if server is closed
            if (serverInstance.isAlive()) {
                throw new AssertionError("Server is not closed");
            }
        } catch (Exception e) {
            serverInstance.interrupt();
            e.printStackTrace();
            throw new AssertionError();
        }

    }

    @Test
    public void TestGetSetDelete() {
        ServerThread testServer = new ServerThread();
        Thread serverInstance = testServer.createTestServer();

        try {
            Client client = new Client(testServer.getServerAddress(), testServer.getServerPort());

            // set key test1
            client.setCommand(new String[] {"-t", "set", "-k", "test1", "-v", "Test 1"});
            String response1 = client.executeCommand();
            HashMap<String, String> res1 = new Gson().fromJson(response1, HashMap.class);
            assertEquals(
                    res1,
                    new HashMap<String, String>() {{
                        put("response", "OK");
                    }}
            );

            // get key test1
            client.setCommand(new String[] {"-t", "get", "-k", "test1"});
            String response2 = client.executeCommand();
            HashMap<String, String> res2 = new Gson().fromJson(response2, HashMap.class);
            assertEquals(
                    res2,
                    new HashMap<String, String>() {{
                        put("response", "OK");
                        put("value", "Test 1");
                    }}
            );

            // delete key test1
            client.setCommand(new String[] {"-t", "delete", "-k", "test1"});
            String response3 = client.executeCommand();
            HashMap<String, String> res3 = new Gson().fromJson(response3, HashMap.class);
            assertEquals(
                    res3,
                    new HashMap<String, String>() {{
                        put("response", "OK");
                    }}
            );

            // check if key test1 doesn't exist in db
            client.setCommand(new String[] {"-t", "get", "-k", "test1"});
            String response4 = client.executeCommand();
            HashMap<String, String> res4 = new Gson().fromJson(response4, HashMap.class);
            assertEquals(
                    res4,
                    new HashMap<String, String>() {{
                        put("response", "ERROR");
                        put("reason", "No such key");
                    }}
            );
        }
        finally {
            serverInstance.interrupt();
        }

    }

    @Test
    public void TestClientRequestFromFile() {
        ServerThread testServer = new ServerThread();
        Thread serverInstance = testServer.createTestServer();

        try {
            Client client = new Client(testServer.getServerAddress(), testServer.getServerPort());

            client.setCommand(new String[] {"-in", "src/test/resources/test_request_dict.json"});
            String response = client.executeCommand();
            JsonObject res = new Gson().fromJson(response, JsonObject.class);
            JsonObject value = res.get("value").getAsJsonObject();
            assertTrue(value.isJsonObject());
            assertEquals(value.get("name").getAsString(), "Elon Musk");
        }
        finally {
            serverInstance.interrupt();
        }
    }

}
