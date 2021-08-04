import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.CommonMethods;
import util.MockResponseFileLoader;

import java.text.DecimalFormat;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetAllAdjustedPricesTest {
    private static final ResponseDefinitionBuilder response = new ResponseDefinitionBuilder();
    private final DecimalFormat df = new DecimalFormat("###.####");
    private Response httpResponse;

    @BeforeAll
    public static void setUp() {
        CommonMethods.setUp();
        stubFor(
                get("/all-prices")
                        .willReturn(response.withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(MockResponseFileLoader.getResponseBody("ALL_PRICES")))
        );
    }

    @Test
    public void checkPricesAreProcessed() {
        httpResponse = sendAllPricesRequest();
        JsonArray jsonArray = getResponseJsonArray();
        assertEquals(5, jsonArray.size(), "The number of processed prices do not match" );
        for (JsonElement responseObject : jsonArray) {
            assertTrue(responseObject.getAsJsonObject().has("ADJUSTED_BID"));
            assertTrue(responseObject.getAsJsonObject().has("ADJUSTED_ASK"));
        }
    }


    @Test
    public void checkPricesAreProcessedInSequence() {
        httpResponse = sendAllPricesRequest();
        JsonArray jsonArray = getResponseJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            if (i + 1 <= jsonArray.size() - 1) {
                String firstObjectAdjustedTimeStamp = jsonArray.get(i).getAsJsonObject().get("ADJUSTED_TIMESTAMP").getAsString();
                String secondObjectAdjustedTimeStamp = jsonArray.get(i + 1).getAsJsonObject().get("ADJUSTED_TIMESTAMP").getAsString();
                int compare = firstObjectAdjustedTimeStamp.compareTo(secondObjectAdjustedTimeStamp);
                assertTrue(compare < 0, "Prices are not processed in sequence");
            }
        }
    }


    @Test
    public void compareBidAndAskPrices() {
        httpResponse = sendAllPricesRequest();
        JsonArray jsonArray = getResponseJsonArray();
        for (JsonElement responseObject : jsonArray) {
            double bidPrice = responseObject.getAsJsonObject().get("ADJUSTED_BID").getAsDouble();
            double askPrice = responseObject.getAsJsonObject().get("ADJUSTED_ASK").getAsDouble();
            assertTrue(bidPrice < askPrice, "bid price is greater than ask price");
        }

    }


    @Test
    public void checkCommisionIsApplied() {
        httpResponse = sendAllPricesRequest();
        JsonArray jsonArray = getResponseJsonArray();
        for (JsonElement responseObject : jsonArray) {
            double receivedBidPrice = responseObject.getAsJsonObject().get("RECEIVED_BID").getAsDouble();
            double adjustedBidPrice = responseObject.getAsJsonObject().get("ADJUSTED_BID").getAsDouble();
            String expectedAdjustedBidPrice = df.format(applyBidMargin(receivedBidPrice));
            assertEquals( expectedAdjustedBidPrice, df.format(adjustedBidPrice), "bid margin is not applied correctly");

            double receivedAskPrice = responseObject.getAsJsonObject().get("RECEIVED_ASK").getAsDouble();
            double adjustedAskPrice = responseObject.getAsJsonObject().get("ADJUSTED_ASK").getAsDouble();
            String expectedAdjustedAskPrice = df.format(applyAskMargin(receivedAskPrice));
            assertEquals( expectedAdjustedAskPrice, df.format(adjustedAskPrice), "ask margin is not applied correctly");
        }
    }
//
//    @AfterAll
//    public static void tearDown() {
//        CommonMethods.tearDown();
//    }

    private Response sendAllPricesRequest() {
        return RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get("http://localhost:8089/all-prices");
    }

    private JsonArray getResponseJsonArray() {
        return JsonParser.parseString(sendAllPricesRequest().body().prettyPrint()).getAsJsonArray();
    }

    private double applyBidMargin(double receivedBidPrice) {
        return receivedBidPrice - (receivedBidPrice * 0.1);
    }

    private double applyAskMargin(double receivedAskPrice) {
        return receivedAskPrice + (receivedAskPrice * 0.1);
    }
}
