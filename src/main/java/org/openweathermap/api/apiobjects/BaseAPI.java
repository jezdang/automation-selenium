package org.openweathermap.api.apiobjects;

import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.List;

public class BaseAPI {

    protected JSONObject createRequestJsonFromList(List<String> keys, List<Object> values) {
        JSONObject requestJson = new JSONObject();
        for (int i = 0; i < keys.size(); i++) {
            requestJson.put(keys.get(i), values.get(i));
        }
        System.out.println(requestJson.toJSONString());
        return requestJson;
    }

    public String getValueByKey(ResponseOptions<Response> resp, String key) {
        String returnString = "";
        try {
                returnString = resp.getBody().jsonPath().get(key).toString();
        } catch (JsonPathException jsonEx) {
            System.out.println("EXCEPTION: JSON PATH NOT FOUND");
            returnString = "NA";
        }
        return returnString;
    }

    // Get the Status Code
    public String getStatusCode(ResponseOptions<Response> resp) {
        return String.valueOf(((RestAssuredResponseImpl) resp).then().extract().statusCode());
    }
}
