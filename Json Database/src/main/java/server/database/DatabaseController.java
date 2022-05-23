package server.database;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;

public class DatabaseController {

    private final Gson gson = new Gson();

    private final Database db;
    private final ServerSocket server;

    private DatabaseRequest request;

    public DatabaseController(ServerSocket server, Database db) {
        this.server = server;
        this.db = db;
    }

    public void setCommand(String command){
        JsonElement input = gson.fromJson(command, JsonElement.class);
        request = new DatabaseRequest(input);
    }

    public String executeCommand() {
        boolean isCompleted;
        switch (request.type) {
            case "set":
                db.set(request.key, request.value);
                return buildOkResponse();
            case "get":
                JsonElement result = db.get(request.key);
                return result != null ? buildGetResponse(result) : buildErrorResponse("No such key");
            case "delete":
                isCompleted = db.delete(request.key);
                return isCompleted ? buildOkResponse() : buildErrorResponse("No such key");
            case "exit":
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return buildOkResponse();
            default:
                return buildErrorResponse("Invalid type of request");
        }
    }

    public String getRequestType() {
        return request.type;
    }

    public String buildOkResponse() {
        return gson.toJson(Map.of("response", "OK"));
    }

    public String buildGetResponse(JsonElement value) {
        return gson.toJson(
                Map.of(
                        "response", "OK",
                        "value", value
                )
        );
    }

    public String buildErrorResponse(String reason) {
        return gson.toJson(
                Map.of(
                        "response", "ERROR",
                        "reason", reason
                )
        );
    }
}
