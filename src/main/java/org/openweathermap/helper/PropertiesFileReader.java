package org.openweathermap.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesFileReader {

    Properties prop;

    public PropertiesFileReader() {

        try {
            String filename = Paths.get(System.getProperty("user.dir")) + "\\src\\test\\java\\org\\openweathermap\\properties\\config.properties";
            InputStream input = new FileInputStream(filename);
            prop = new Properties();

            if (input != null) {
                prop.load(input);
            } else
                LoggerHelper.error("No properties found");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public String getProperty(String key) { return prop.getProperty(key); }
    public String getUrl() {
        return prop.getProperty("URL");
    }

}
