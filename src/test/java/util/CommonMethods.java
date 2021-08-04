package util;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

public class CommonMethods {
    private static final int PORT = 8089;
    private static final String HOST = "localhost";
    private static final WireMockServer server = new WireMockServer(PORT);

    public static void setUp() {
        System.out.println("inside setup");
        server.start();
        MockResponseFileLoader.loadMockResponseFile();
        configureFor(HOST, PORT);
    }

    @AfterAll
    public static void tearDown() {
        if (server.isRunning())
            server.shutdownServer();
    }
}
