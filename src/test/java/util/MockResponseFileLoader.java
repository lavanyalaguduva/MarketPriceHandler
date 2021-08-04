package util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MockResponseFileLoader {

    private static final String mockResponseDataFilePath = System.getProperty("user.dir") +
            String.format("/%s", "src/main/resources/MockResponse.json");
    private static JsonObject parser;

    public static void loadMockResponseFile() {
        try (Reader reader = Files.newBufferedReader(Paths.get(mockResponseDataFilePath))) {
            parser = JsonParser.parseReader(reader).getAsJsonObject();

        } catch (Exception e) {
            System.out.println("Json read error" + e);
            e.printStackTrace();
        }
    }
    public static String getResponseBody(String responseString) {
        return parser.get(responseString).toString();
    }
}
