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

*test* folder includes junit tests. Wiremock stubs are used to simulate the service behaviour. 
Rest Assured APIs are used to test the service endpoints.

*Endpoints*
- **/all-prices** - fetches all the prices
- **/latest-price/<instrument-name>** - fetches the latest price for an instrument

### Commands
Following maven command can be used to run the tests.

`mvn clean verify`

### Test Improvements
1. Feed the data in wrong format and check whether the service is throwing a valid error
2. Feed the data for a new instrument and check whether the service is able to process

### Mock Improvements
1. Add an endpoint to fetch price history between two timestamps
2. Add an endpoint to fetch price history for an instrument between two timestamps

##Test Strategy
###Goal
The goal of end-2-end test would to make sure the web/mobile application displays 
the new bid and ask price of an instrument at all times as and when the new prices are fed to the backend service.

###Types of testing
- Unit Testing
- Integration Testing
- UI Component Testing
- End-2-End Testing
- Performance Testing
- Load Testing
- Accessibility Testing

### End-2-End Test Scenarios
####Setup Assumptions
In the lower test environments the price data feed can be controlled and mimicked, 
so that the expected output can be figured out. 
The feed data can up uploaded through an interface

####Automation tools
Selenium
Java/Javascript/Python
Cucumber
Junit/Testng
Jenkins for CI/CD

####Scenarios
1. Feed the price data with multiple prices for each instrument through an interface 
   and check whether the latest price is displayed in the UI
2. Feed the updated price for an instrument and check the other instruments are displaying the valid price
3. Check whether the price transition in the UI is noticeable
   (Assuming the tile is displayed in red is price increases or green if price decreases)
4. Check whether tile for an instrument displays the adjusted bid price and ask price 
   and not the bid/ask prices that are fed
5. Check whether the UI is responsive when the browser is resized and prices are still clearly visible
6. Check whether the screen reader can rad out the instrument name followed by the prices when the tile is in focus