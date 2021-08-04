import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import util.CommonMethods;
import util.MockResponseFileLoader;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class GetLatestAdjustedPriceTest {
    private static final ResponseDefinitionBuilder response = new ResponseDefinitionBuilder();


    @BeforeAll
    public static void setUp() {
        CommonMethods.setUp();

        stubFor(
                get("/latest-price/EURJPY")
                        .willReturn(response.withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(MockResponseFileLoader.getResponseBody("LATEST_EUR_JPY")))
        );
        stubFor(
                get("/latest-price/GBPUSD")
                        .willReturn(response.withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(MockResponseFileLoader.getResponseBody("LATEST_GBP_USD")))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"EURJPY", "GBPUSD"})
    public void getLatestPriceTest(String instrumentName) {
        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get(String.format("http://localhost:8089/latest-price/%s", instrumentName));
        checkLatestPriceResponse(response.body().prettyPrint());
    }

    private void checkLatestPriceResponse(String response) {
        JsonObject responseJson = JsonParser.parseString(response).getAsJsonObject();
        switch (responseJson.get("INSTRUMENT").getAsString()) {
            case "EUR/JPY":
                assertEquals("107.65", responseJson.get("ADJUSTED_BID").toString());
                assertEquals("131.901", responseJson.get("ADJUSTED_ASK").toString());
                break;
            case "GBP/USD":
                assertEquals("1.1249", responseJson.get("ADJUSTED_BID").toString());
                assertEquals("1.3817", responseJson.get("ADJUSTED_ASK").toString());
                break;
        }

    }

}
