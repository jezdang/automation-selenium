package org.openweathermap.api.apistepdefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import org.json.simple.JSONObject;
import org.openweathermap.api.apiobjects.StationFieldsEnum;
import org.openweathermap.api.apiobjects.WeatherStationAPIObject;
import org.openweathermap.helper.PropertiesFileReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.isNull;
import static org.junit.Assert.assertEquals;

public class RegisterWeatherStationStep {

    private static final String APIKEY = new PropertiesFileReader().getProperty("API_KEY");
    private static ResponseOptions<Response> currentResponse;
    WeatherStationAPIObject weatherStationAPIObjects = new WeatherStationAPIObject();
    String latestStationID = "";

    @When("^I have registered a new station to OpenWhetherMap with values of \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\"$")
    public void iHaveRegisteredANewStationToOpenWhetherMapWithValuesOf(String external_id, String name, String longitude, String latitude, String altitude)  {
        JSONObject requestJson = weatherStationAPIObjects.createWeatherStationRequest(
                Arrays.asList(external_id, name, Float.parseFloat(longitude), Float.parseFloat(latitude), Float.parseFloat(altitude)));
        currentResponse = weatherStationAPIObjects.postStation(APIKEY, requestJson);
        latestStationID = weatherStationAPIObjects.getValueByKey(currentResponse, "ID"); // After register a station, keep 'latestStationID'
    }

    @Then("^I have received HTTP response code of \"([^\"]*)\"$")
    public void iHaveReceivedHTTPResponseCodeOf(String expectedHTTPCode)  {
        String actualHTTPCode = isNull(currentResponse) ? "" : weatherStationAPIObjects.getStatusCode(currentResponse);
        assertEquals("ERROR: The actual Code is " + actualHTTPCode, expectedHTTPCode, actualHTTPCode);
    }

    @Then("^I see my new station with values of \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\"$")
    public void iSeeMyNewStationWithValuesOf(String strExternal_ID, String strName, String strLatitude, String strLongitude, String strAltitude)  {
        // Guard: to make sure currentResponse not null.
        if (isNull(currentResponse)) { return; }

        List<String> actualList = new ArrayList<>();
        actualList.add(weatherStationAPIObjects.getValueByKey(currentResponse, StationFieldsEnum.stationFields.EXTERNAL_ID.toString()));
        actualList.add(weatherStationAPIObjects.getValueByKey(currentResponse,StationFieldsEnum.stationFields.NAME.toString()));
        actualList.add(weatherStationAPIObjects.getValueByKey(currentResponse,StationFieldsEnum.stationFields.LATITUDE.toString()));
        actualList.add(weatherStationAPIObjects.getValueByKey(currentResponse,StationFieldsEnum.stationFields.LONGITUDE.toString()));
        actualList.add(weatherStationAPIObjects.getValueByKey(currentResponse,StationFieldsEnum.stationFields.ALTITUDE.toString()));

        List<String> expectedList = Arrays.asList(strExternal_ID,strName, strLatitude, strLongitude, strAltitude);
        assertEquals("ERROR: The values from new station are not matching; Actual: " + actualList.toString() ,
                expectedList, actualList);
    }
}
