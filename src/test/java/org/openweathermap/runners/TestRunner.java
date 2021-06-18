package org.openweathermap.runners;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/org/openweathermap/features/search",
        plugin = {
                "pretty", "json:target/cucumber-reports/cucumber.json", "html:target/cucumber-reports", "junit:target/cucumber-reports/Runner-Cucumber-Log.xml"},
        glue = {"org.openweathermap.web.stepdefinitions"},
        monochrome = true,
        tags = {"@RegressionTest"})

public class TestRunner {
}
