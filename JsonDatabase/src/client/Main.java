package client;

import com.beust.jcommander.JCommander;

public class Main {

    public static void main(String[] args) {
        QueryBuilder query = new QueryBuilder();
        JCommander.newBuilder()
                .addObject(query)
                .build()
                .parse(args);

        String request = query.build();

        Client client = new Client();
        client.setCommand(request);
        client.executeCommand();
    }
}
