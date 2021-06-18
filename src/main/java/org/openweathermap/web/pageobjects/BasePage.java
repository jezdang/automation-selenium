package org.openweathermap.web.pageobjects;

import com.google.common.base.Function;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.FluentWait;
import org.openweathermap.common.DriverFactory;
import org.openweathermap.helper.LoggerHelper;
import org.openweathermap.helper.PropertiesFileReader;

import java.time.Duration;

public class BasePage extends DriverFactory {

    PropertiesFileReader propReader = new PropertiesFileReader();

    public BasePage() {
        if(webDriver == null)
            throw new IllegalArgumentException("Driver object is null");

        PageFactory.initElements(new AjaxElementLocatorFactory(webDriver, 10), this);
    }

    protected String getPageTitle() {
        return webDriver.getTitle();
    }

    protected void waitForPageLoad() {
    }

    protected void waitUntilDocumentReady() {
        try {
            final FluentWait<WebDriver> wait = new FluentWait<>(webDriver)
                    .withTimeout(Duration.ofSeconds(60L))
                    .withMessage("Time out while waiting for document ready state").ignoring(WebDriverException.class);
            wait.until((Function<WebDriver, Boolean>) driver -> String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
                    .equals("complete"));
        } catch (Exception e) {
            LogFactory.getLog(BasePage.class).warn("Cannot wait for document.readyState to complete. Continue test.", e);
        }

    }

    public void navigateToHomePage() {
        webDriver.get(propReader.getUrl());
    }

    public boolean checkForTitle(String title){
        LoggerHelper.info(title);
        if(title == null || title.isEmpty())
            throw new IllegalArgumentException(title);
        return getPageTitle().contains(title);
    }
}
