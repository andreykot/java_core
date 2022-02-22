package server.database;

import com.google.gson.JsonElement;

interface Database {

    public int getSize();

    public boolean set(JsonElement key, JsonElement value);

    public JsonElement get(JsonElement key);

    public boolean delete(JsonElement key);

}
