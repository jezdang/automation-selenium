package org.openweathermap.runners;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/org/openweathermap/features/api",
        plugin = {
                "pretty",
                "json:target/cucumber-reports/cucumber-api.json",
                "html:target/cucumber-reports", "junit:target/cucumber-reports/Runner-Cucumber-API-Log.xml"},
        glue = {"org.openweathermap.api.apistepdefinitions"},
        monochrome = true,
        tags = {"@APITest"})

public class APITestRunner {
}
