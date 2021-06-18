package org.openweathermap.api.apiobjects;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.openweathermap.helper.PropertiesFileReader;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

public class WeatherStationAPIObject extends BaseAPI {

    private static RequestSpecification request = null;
    private static final String BASE_STATION_URI = new PropertiesFileReader().getProperty("API_URL") + "3.0/stations";

    public WeatherStationAPIObject(){
        request = given().contentType(ContentType.JSON);
    }

    public static ResponseOptions<Response> postStation(String apiKey, HashMap<String, Object> json ) {
        ResponseOptions<Response> responseOptionReturn = null;
        try {
            String path = BASE_STATION_URI + "?APPID=" + apiKey;
            System.out.println(path);
            responseOptionReturn = request.with().body(json).
                    when().post(path);
        } catch (NumberFormatException ex) {
            System.out.println("EXCEPTION!: Number Format Exception: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("EXCEPTION!: " + ex.getMessage());
        }
        // Extract Response into an Object "ResponseOptions<Response> response "
        return responseOptionReturn;
    }

    public JSONObject createWeatherStationRequest(List<Object> listValues) {
        List<String> enumStationNames = Stream.of(StationFieldsEnum.stationFields.values())
                .map(StationFieldsEnum.stationFields::name)
                .collect(Collectors.toList());
        JSONObject requestJson = createRequestJsonFromList(enumStationNames, listValues);
        return requestJson;
    }
}
