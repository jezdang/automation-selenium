package org.openweathermap.web.pageobjects;

import com.google.common.base.Function;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openweathermap.helper.LoggerHelper;

import java.time.Duration;

public class HomePage extends BasePage {

    public HomePage() {
        waitForPageLoad();
        waitForMaskDisappeared();
    }

    @FindBy(xpath = "//*[@class='logo']//img[contains(@src,'logo')]")
    WebElement logoIcon;

    @FindBy(name = "q")
    WebElement searchCityTextFieldInHeader;

    @FindBy(css = ".search-block input")
    WebElement searchCityTextField;

    @FindBy(css = ".search-block button")
    WebElement searchButton;

    @FindBy(xpath = "//*[@class='switch-container']//*[@class='option' and contains(.,'Metric')]")
    private WebElement switchCDegreeButton;

    @FindBy(xpath = "//*[@class='switch-container']//*[@class='option' and contains(.,'Imperial')]")
    private WebElement switchFDegreeButton;

    @FindBy(css = ".switch-container #selected")
    private WebElement selectedTempUnitButton;

    @FindBy(id = "weather-widget")
    private WebElement weatherWidget;

    @FindBy(css = ".current-container h2")
    private WebElement currentCityText;

    @FindBy(css = ".current-temp .heading")
    private WebElement currentTempText;

    @Override
    protected void waitForPageLoad() {
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(webDriver).withTimeout(Duration.ofSeconds(15))
                .ignoring(StaleElementReferenceException.class);
        wait.until(webDriver -> {
            String syntaxStr = " - ";
            String titlePage = getPageTitle();
            if (titlePage.contains(syntaxStr)) {
                titlePage = titlePage.split(syntaxStr)[0];
            }
            return titlePage.matches("Сurrent weather and forecast");
        });
    }

    private void waitForMaskDisappeared(){
        WebDriverWait wait = new WebDriverWait(webDriver, 15);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".owm-loader-container svg")));
    }

    public String getTitle() {
        return webDriver.getTitle();
    }

    public SearchPage searchCityFromHeader(String city) {
        LoggerHelper.action(String.format("Search weather of city [%s] in header", city));
        searchCityTextFieldInHeader.sendKeys(city);
        searchCityTextFieldInHeader.submit();
        return new SearchPage();
    }

    private By getSuggestItemLocator(String itemText) {
        String xpath = String.format("//*[@class='search-block']//*[@class='search-dropdown-menu' and contains(.,'%s')]", itemText);
        return By.xpath(String.format(xpath, itemText));
    }

    public void setSearchCity(String city) {
        searchCityTextField.sendKeys(city);
    }

    public void selectSearchCity(String city) {
        final By suggestItemLocator = getSuggestItemLocator(city);
        final FluentWait<WebDriver> wait = new FluentWait<>(webDriver).withTimeout(Duration.ofSeconds(10))
                .ignoring(WebDriverException.class);
        wait.until((Function<WebDriver, Boolean>) driver -> {
            try {
                driver.findElement(suggestItemLocator).click();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(suggestItemLocator));
                return true;

            } catch (Exception e) {
                LoggerHelper.info(String.format("Ignore exception: %s", e.getMessage()));
                return false;
            }
        });
    }

    public void clickSearchButton() {
        LoggerHelper.action("Click Search button");
        searchButton.click();
    }

    public void switchToCelsiusDegree() {
        LoggerHelper.action("Switch to Celsius Degree");
        switchCDegreeButton.click();
        selectedTempUnitButton.getAttribute("style");
        WebDriverWait wait = new WebDriverWait(webDriver, 5);
        wait.until(ExpectedConditions.attributeContains(selectedTempUnitButton, "style", "left: 2pt"));
    }

    public void switchToFahrenheitDegree() {
        LoggerHelper.action("Switch to Fahrenheit Degree");
        switchFDegreeButton.click();
        selectedTempUnitButton.getAttribute("style");
        WebDriverWait wait = new WebDriverWait(webDriver, 5);
        wait.until(ExpectedConditions.attributeContains(selectedTempUnitButton, "style", "left: 72pt"));
    }

    private boolean isCityNotFound() {
        return !webDriver.findElements(By.cssSelector(".search-block .notFoundOpen")).isEmpty();
    }

    public void verifyNoCityFound() {
        LoggerHelper.check("Verify no city found");
        Assert.assertTrue(isCityNotFound());
    }

    public void verifyNotFoundMessageDisplayed(String message) {
        LoggerHelper.check(String.format("Verify not found message [%s] displayed", message));
        if (isCityNotFound())
            Assert.assertTrue(webDriver.findElement(By.cssSelector(".search-block .not-found")).getText().contains(message));
        else
            LoggerHelper.error("There is a city found, so there is no message displayed");
    }

    public void verifyCityNameDisplayed(String city) {
        LoggerHelper.check(String.format("Verify city [%s] found and its details displayed", city));
        Assert.assertTrue(currentCityText.getText().toUpperCase().contains(city.toUpperCase()));
    }

    private String getCurrentTemperature() {
        return currentTempText.getText();
    }

    private String getCurrentTemperatureByCity(String city) {
        String xpathExpression = String.format("//*[contains(@class,'current-container') and contains(., '%s')]" +
                "//*[@class='current-temp']//*[@class='heading']", city);
        return webDriver.findElement(By.xpath(xpathExpression)).getText();
    }

    public void verifyCurrentTemperatureDegree(String city, String unit) {
        LoggerHelper.check(String.format("Verify temperature unit is [%s]", unit));
        String actualTemperature;

        if(city != null)
            actualTemperature = getCurrentTemperatureByCity(city);
        else
            actualTemperature = getCurrentTemperature();

        if (unit == "Celsius")
            Assert.assertTrue(actualTemperature.contains("°C"));
        else if (unit == "Fahrenheit")
            Assert.assertTrue(actualTemperature.contains("°F"));
    }
}
