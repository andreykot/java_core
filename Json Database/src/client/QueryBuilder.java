package client;

import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;


public class QueryBuilder {

    @Parameter(names = "-t", description = "Request type")
    public String type;

    @Parameter(names = "-k", description = "Key")
    public String key;

    @Parameter(names = "-v", description = "Value")
    public String value;

    @Parameter(names = "-in", description = "Input file with request")
    public String input;

    public String build() {
        Gson gson = new Gson();

        if (input == null) {
            HashMap<String, String> query = new HashMap<>();
            query.put("type", type);
            query.put("key", key);
            query.put("value", value);
            return gson.toJson(query);
        } else {
            Path path = java.nio.file.Paths.get(input);
            try (BufferedReader reader = new BufferedReader(new FileReader(path.toString()))) {
                JsonElement data = gson.fromJson(reader, JsonElement.class);
                return gson.toJson(data);
            } catch (IOException e) {
                throw new java.lang.RuntimeException("Error during reading client's file");
            }
        }
    }
}