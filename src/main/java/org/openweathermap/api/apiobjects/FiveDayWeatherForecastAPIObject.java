package org.openweathermap.api.apiobjects;

import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;
import org.openweathermap.helper.PropertiesFileReader;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class FiveDayWeatherForecastAPIObject extends BaseAPI {

    private static RequestSpecification request = null;
    private static final String FORECAST_URI = new PropertiesFileReader().getProperty("API_URL") + "2.5/forecast";

    public FiveDayWeatherForecastAPIObject(){
        request = given().baseUri(FORECAST_URI);
    }

    public static RequestSpecification createWeatherForecastRequest(String apiKey, HashMap<String, String> parameters){
        request.queryParams(parameters).queryParam("APPID", apiKey);
        return request;
    }

    public static ResponseOptions<Response> getForecast(RequestSpecification request) {
        ResponseOptions<Response> responseOptionReturn = null;
        try {
            responseOptionReturn = request.when().log().all().get();
        } catch (NumberFormatException ex) {
            System.out.println("EXCEPTION!: Number Format Exception: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("EXCEPTION!: " + ex.getMessage());
        }
        // Extract Response into an Object "ResponseOptions<Response> response "
        return responseOptionReturn;
    }
}
