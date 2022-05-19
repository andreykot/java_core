import client.QueryBuilder;
import com.beust.jcommander.JCommander;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestClientQueryBuilder {
    @Test
    public void TestQueryBuilder() {
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
        assertEquals(
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
                .parse("-in", "src/test/resources/test_request.json");
        HashMap<String, String> request = new Gson().fromJson(q2.build(), HashMap.class);
        assertEquals(
                request,
                new HashMap<String, String>() {{
                    put("type", "set");
                    put("key", "1");
                    put("value", "hello world");
                }}
        );
    }
}
