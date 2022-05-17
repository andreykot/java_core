package server.database;

import com.google.gson.*;

import java.util.Map;
import java.util.stream.Stream;

public class DatabaseRequest {
    String type;
    JsonElement key = JsonParser.parseString("{}");
    JsonElement value = JsonParser.parseString("{}");

    public DatabaseRequest(JsonElement json) {
        this.type = json.getAsJsonObject().get("type").getAsString();
        if (json.getAsJsonObject().has("key")) {
            JsonElement inputKey = json.getAsJsonObject().get("key");
            if (inputKey.isJsonPrimitive()) {
                this.key = inputKey.getAsJsonPrimitive();
            } else if (inputKey.isJsonArray()) {
                this.key = inputKey.getAsJsonArray();
            } else {
                throw new java.lang.RuntimeException("Unexpected error: invalid input key");
            }
        }
        if (json.getAsJsonObject().has("value")) {
            JsonElement inputValue = json.getAsJsonObject().get("value");
            if (inputValue.isJsonPrimitive()) {
                this.value = inputValue.getAsJsonPrimitive();
            } else if (inputValue.isJsonObject()) {
                this.value = inputValue.getAsJsonObject();
            } else {
                throw new java.lang.RuntimeException("Unexpected error: invalid input value");
            }
        }
    }
}
