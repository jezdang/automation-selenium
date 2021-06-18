package org.openweathermap.common;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;

import java.util.ArrayList;
import java.util.List;

public class WebHook {

    private static final Logger LOGGER = Logger.getLogger(WebHook.class);

    @Before("@RegressionTest")
    public void deleteAllCookies() {
        DriverFactory.webDriver.manage().deleteAllCookies();
    }

    @After("@RegressionTest")
    public void afterScenario(Scenario scenario) {
        List<String> browserTabs = new ArrayList<>(DriverFactory.webDriver.getWindowHandles());
        if(browserTabs.size() > 1) {
            DriverFactory.webDriver.close();
            DriverFactory.webDriver.switchTo().window(browserTabs.get(0));
        }

        try {
            final byte[] screenshot = ((TakesScreenshot) DriverFactory.webDriver).getScreenshotAs(OutputType.BYTES);
            scenario.embed(screenshot, "image/png");
        } catch (WebDriverException somePlatformsDontSupportScreenshots) {
            System.err.println(somePlatformsDontSupportScreenshots.getMessage());
        }
    }
}
