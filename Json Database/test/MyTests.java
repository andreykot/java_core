import client.Client;
import client.QueryBuilder;
import com.beust.jcommander.JCommander;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import server.Main;

import java.util.HashMap;

public class MyTests {

    String TEST_SERVER_ADDRESS = "127.0.0.1";
    int TEST_SERVER_PORT = 8052;
    String TEST_DB_FILE = "./test/data/db.json";
    String TEST_REQUEST = "./test/data/test_request.json";

    private Thread createTestServer() {
        Thread server = new Thread(() -> {
            Main.main(new String[] {
                    "-port", Integer.toString(TEST_SERVER_PORT),
                    "-dbfile", TEST_DB_FILE
            });
        });
        server.start();
        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return server;
    }

    @Test
    public void TestClientQueryBuilder() {
        // test String input
        QueryBuilder q1 = new QueryBuilder();
        JCommander.newBuilder()
                .addObject(q1)
                .build()
                .parse("-t", "set",
                        "-k", "1",
                        "-v", "hello world"
                );
        HashMap<String, String> request1 = new Gson().fromJson(q1.build(), HashMap.class);
        Assert.assertEquals(
                request1,
                new HashMap<String, String>() {{
                    put("type", "set");
                    put("key", "1");
                    put("value", "hello world");
                }}
        );

        // test File input
        QueryBuilder q2 = new QueryBuilder();
        JCommander.newBuilder()
                .addObject(q2)
                .build()
                .parse("-in", TEST_REQUEST);
        HashMap<String, String> request = new Gson().fromJson(q2.build(), HashMap.class);
        Assert.assertEquals(
                request,
                new HashMap<String, String>() {{
                    put("type", "set");
                    put("key", "1");
                    put("value", "hello world");
                }}
        );
    }

    @Test
    public void TestClientServerConnection() {
        Thread server = createTestServer();

        try {
            Client client = new Client(TEST_SERVER_ADDRESS, TEST_SERVER_PORT);
            client.setCommand(new String[] {"-t", "Invalid Type"});
            String response = client.executeCommand();
            HashMap<String, String> res = new Gson().fromJson(response, HashMap.class);
            Assert.assertEquals(
                    res,
                    new HashMap<String, String>() {{
                        put("response", "ERROR");
                        put("reason", "Invalid type of request");
                    }}
            );
        } finally {
            server.interrupt();
        }

    }

    @Test
    public void TestCloseServer() {
        Thread server = createTestServer();

        try {
            // send request from a client
            Client client = new Client(TEST_SERVER_ADDRESS, TEST_SERVER_PORT);
            client.setCommand(new String[] {"-t", "exit"});
            String response = client.executeCommand();
            HashMap<String, String> res = new Gson().fromJson(response, HashMap.class);
            Assert.assertEquals(
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
            if (server.isAlive()) {
                throw new AssertionError("Server is not closed");
            }
        } catch (Exception e) {
            server.interrupt();
            e.printStackTrace();
            throw new AssertionError();
        }

    }

    @Test
    public void TestGetSetDelete() {
        Thread server = createTestServer();

        System.out.println("Start...");

        try {
            Client client = new Client(TEST_SERVER_ADDRESS, TEST_SERVER_PORT);

            // set key test1
            client.setCommand(new String[] {"-t", "set", "-k", "test1", "-v", "Test 1"});
            String response1 = client.executeCommand();
            HashMap<String, String> res1 = new Gson().fromJson(response1, HashMap.class);
            Assert.assertEquals(
                    res1,
                    new HashMap<String, String>() {{
                        put("response", "OK");
                    }}
            );

            // get key test1
            client.setCommand(new String[] {"-t", "get", "-k", "test1"});
            String response2 = client.executeCommand();
            HashMap<String, String> res2 = new Gson().fromJson(response2, HashMap.class);
            Assert.assertEquals(
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
            Assert.assertEquals(
                    res3,
                    new HashMap<String, String>() {{
                        put("response", "OK");
                    }}
            );

            // check if key test1 doesn't exist in db
            client.setCommand(new String[] {"-t", "get", "-k", "test1"});
            String response4 = client.executeCommand();
            HashMap<String, String> res4 = new Gson().fromJson(response4, HashMap.class);
            Assert.assertEquals(
                    res4,
                    new HashMap<String, String>() {{
                        put("response", "ERROR");
                        put("reason", "No such key");
                    }}
            );
        }
        finally {
            server.interrupt();
        }

    }

}
