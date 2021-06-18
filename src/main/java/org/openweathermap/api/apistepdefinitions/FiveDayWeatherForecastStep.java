package org.openweathermap.api.apistepdefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.openweathermap.api.apiobjects.FiveDayWeatherForecastAPIObject;
import org.openweathermap.helper.PropertiesFileReader;

import java.util.HashMap;

public class FiveDayWeatherForecastStep {

    private static final String APIKEY = new PropertiesFileReader().getProperty("API_KEY");
    private static ResponseOptions<Response> currentResponse;
    FiveDayWeatherForecastAPIObject weatherForecastAPIObject = new FiveDayWeatherForecastAPIObject();

    @When("^User sets up request with city name \"([^\"]*)\"$")
    public void userSetsUpRequestWithCityName(String cityName) {
        HashMap<String, String> paramCityName = new HashMap<String, String>();
        paramCityName.put("q", cityName);
        currentResponse = weatherForecastAPIObject.getForecast(weatherForecastAPIObject.createWeatherForecastRequest(APIKEY, paramCityName));
    }

    @Then("^Response status code should be OK")
    public void status_code_should_be() {
        Assert.assertEquals(HttpStatus.SC_OK, currentResponse.statusCode());
    }

    @Given("^User sets up request with zip code \"([^\"]*)\"$")
    public void userSetsUpRequestWithZipCode(String zipCode) {
        HashMap<String, String> paramZipCode = new HashMap<String, String>();
        paramZipCode.put("zip", zipCode);
        currentResponse = weatherForecastAPIObject.getForecast(weatherForecastAPIObject.createWeatherForecastRequest(APIKEY, paramZipCode));
    }

    @And("^City name should be \"([^\"]*)\"$")
    public void verifyCityNameInResponse(String cityName) {
        String cityInfo = currentResponse.getBody().asString();
        String expectedKeyAndValue = String.format("\"name\":\"%s\"",cityName);
        Assert.assertTrue(cityInfo.contains(expectedKeyAndValue));
    }
}
