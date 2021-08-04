# Market Price Handler Test Automation

## Integration Test Scenarios
1. Check whether the service processes all the prices that are fed
2. Check whether the service processes the price in sequence
3. Check whether the processed bid price is less than the ask price
4. Check -0.1 margin is applied to bid price and +0.1 margin is applied to the ask price
5. Check whether the service sends the latest price for an instrument

## Framework Details

The above scenarios have been automated Rest Assured, Junit. Wiremock is used to mock the service.

### Code Structure

This project can be imported as a maven in the IDE.

Consists of mainly two folders:

*main* folder includes mock responses in JSON format. This is used by wiremock to send the response based on the request URI.

*test* folder includes junit tests. Wiremock stubs are used to simulate the service behaviour. Rest Assured APIs are 
used to test the service endpoints.

*Endpoints*
- **/all-prices** - fetches all the prices
- **/latest-price/<instrument-name>** - fetches the latest price for an instrument

### Commands
Following maven command can be used to run the tests.

`mvn clean verify`
