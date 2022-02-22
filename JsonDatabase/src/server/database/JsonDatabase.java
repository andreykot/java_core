package server.database;

import com.google.gson.*;

import java.io.*;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JsonDatabase implements Database {

    File dbFile;

    private final Gson gson = new Gson();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    // private final HashMap<String, String> storage = new HashMap<>();

    public JsonDatabase(File file) {
        this.dbFile = file;
        if (!this.dbFile.exists()) {
            try {
                boolean res = this.dbFile.createNewFile();
                writeDbFile(JsonParser.parseString("{}").getAsJsonObject());
            } catch (IOException e) {
                throw new java.lang.RuntimeException("Unable to create the file.");
            }
        }
    }

    private JsonObject readDbFile() {
        String path = dbFile.getPath();
        try (Reader reader = new BufferedReader(new FileReader(path))) {
            return gson.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            throw new java.lang.RuntimeException("Error during reading the DB file");
        }
    }

    private void writeDbFile(JsonObject json) {
        String path = dbFile.getPath();
        try (Writer writer = new FileWriter(path)) {
            writer.write(json.toString());
        } catch (IOException e) {
            throw new java.lang.RuntimeException("Error during writing the DB file");
        }
    }

    @Override
    public int getSize() {
        JsonObject storage = readDbFile();
        return storage.size();
    }


    @Override
    public boolean set(JsonElement key, JsonElement value) {
        lock.writeLock().lock();
        try {
            JsonObject storage = readDbFile();
            if (key.isJsonPrimitive()) {
                storage.add(key.getAsString(), value);
            } else if (key.isJsonArray()) {
                JsonArray keys = key.getAsJsonArray();
                String toAdd = keys.remove(keys.size() - 1).getAsString();
                JsonElement selectedElement = getElement(storage, keys, true);
                selectedElement.getAsJsonObject().add(toAdd, value);
            } else {
                throw new java.lang.RuntimeException("Unexpected input key value");
            }
            writeDbFile(storage);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public JsonElement get(JsonElement key) {
        lock.readLock().lock();
        try {
            JsonElement result = new JsonPrimitive("");
            JsonObject storage = readDbFile();
            if (key.isJsonPrimitive() && storage.has(key.getAsString())) {
                result = storage.get(key.getAsString());
            } else if (key.isJsonArray()) {
                JsonArray keys = key.getAsJsonArray();
                // result = getElement(storage, keys, false).getAsString();
                result = getElement(storage, keys, false);
            }
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean delete(JsonElement key) {
        lock.writeLock().lock();
        try {
            JsonObject storage = readDbFile();
            if (key.isJsonPrimitive() && storage.has(key.getAsString())) {
                storage.remove(key.getAsString());
            } else if (key.isJsonArray()) {
                JsonArray keys = key.getAsJsonArray();
                String toDelete = keys.remove(keys.size() - 1).getAsString();
                getElement(storage, keys, true).getAsJsonObject().remove(toDelete);
            } else {
                return false;
            }
            writeDbFile(storage);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private JsonElement getElement (JsonObject storage, JsonArray keys, boolean createIfAbsent){
        JsonElement tmp = storage;
        for (JsonElement key: keys) {
            if ( createIfAbsent && (!tmp.getAsJsonObject().has(key.getAsString())) ){
                tmp.getAsJsonObject().add(key.getAsString(), new JsonObject());
            } else if (!createIfAbsent && (!tmp.getAsJsonObject().has(key.getAsString()))) {
                throw new java.lang.RuntimeException("No such key in the database.");
            }
            tmp = tmp.getAsJsonObject().get(key.getAsString());
        }
        return tmp;
    }
}
