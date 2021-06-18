package org.openweathermap.web.pageobjects;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openweathermap.helper.LoggerHelper;

import java.time.Duration;
import java.util.List;

public class SearchPage extends BasePage {

    public SearchPage() {
        waitForPageLoad();
    }

    @FindBy(xpath = "//h3[contains(text(),'Search engine is very flexible. How it works:')]")
    WebElement noSearchResultText;

    @FindBy(xpath = "//a[contains(@href,'city/')]")
    List<WebElement> cityNameLinks;

    @Override
    protected void waitForPageLoad() {
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(webDriver).withTimeout(Duration.ofSeconds(5))
                .ignoring(StaleElementReferenceException.class);
        wait.until(webDriver -> {
            String syntaxStr = " - ";
            String titlePage = getPageTitle();
            if (titlePage.contains(syntaxStr)) {
                titlePage = titlePage.split(syntaxStr)[0];
            }
            return titlePage.matches("Find");
        });
    }

    public void verifyNumberOfCitiesAfterSearch(int nbCities) {
        LoggerHelper.check(String.format("Verify there are [%s] searched results displayed", nbCities));
        WebDriverWait wait = new WebDriverWait(webDriver, 15);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("searchform")));
        Assert.assertEquals(cityNameLinks.size(), nbCities);
    }

    public void verifySearchResultContainText(int indexStartFrom1, String text) {
        LoggerHelper.check(String.format("Verify search result at index [%s] contains [%s]", indexStartFrom1, text));
        String actualSearchResultText = cityNameLinks.get(indexStartFrom1-1).getText();
        Assert.assertTrue(actualSearchResultText.contains(text));
    }
}
