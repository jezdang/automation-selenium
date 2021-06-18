package org.openweathermap.report;

import net.masterthought.cucumber.ValidationException;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JsonFormatter {
    private static final Logger LOGGER = Logger.getLogger(JsonFormatter.class);
    private static List<String> elementsToRemove = Arrays.asList("before", "after");

    private JsonFormatter() {
        //do nothing
    }

    public static void parseJsonFiles(List<String> jsonFiles) throws IOException {
        if (jsonFiles.isEmpty()) {
            throw new ValidationException("None report file was added!");
        }
        for (int i = 0; i < jsonFiles.size(); i++) {
            String jsonFile = jsonFiles.get(i);
            removeOutSteps(jsonFile);
        }
    }

    private static void removeElementsHook(JSONObject elements) {
        for(String element : elementsToRemove) {
            if(elements.containsKey(element))
                elements.remove(element);
        }
    }

    private static boolean removeBackground(JSONArray elementsArray, boolean isRemoveBackground) {
        boolean isResult = isRemoveBackground;
        int counter = 0;
        while(counter < elementsArray.size()) {
            JSONObject elements = (JSONObject) elementsArray.get(counter);
            removeElementsHook(elements);
            if (!isResult) {
                counter ++;
                isResult = true;
            } else {
                if (elements.get("keyword").equals("Background")) {
                    elementsArray.remove(counter);
                } else {
                    counter ++;
                }
            }
        }
        return isResult;
    }

    private static void removeOutSteps(String jsonFile) throws IOException {
        JSONParser parser = new JSONParser();
        Object obj = new Object();
        try {
            obj = parser.parse(new FileReader(jsonFile));
            JSONArray jsonArray = (JSONArray) obj;
            for (int i = 0; i < jsonArray.size(); i++) {
                boolean isRemoveBackground = false;
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                JSONArray elementsArray = (JSONArray) jsonObject.get("elements");
                if (elementsArray.size() == 1) {
                    JSONObject elements = (JSONObject) elementsArray.get(0);
                    removeElementsHook(elements);
                } else {
                    isRemoveBackground = removeBackground(elementsArray, isRemoveBackground);
                }
            }
        } catch (IOException | ParseException ex) {
            LOGGER.error(ex);
        }

        try (FileWriter file = new FileWriter(jsonFile)){
            file.write(((JSONArray) obj).toJSONString());
        }
    }
}
