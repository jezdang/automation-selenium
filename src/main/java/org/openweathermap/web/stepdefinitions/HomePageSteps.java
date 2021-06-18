package org.openweathermap.web.stepdefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openweathermap.common.DriverFactory;
import org.openweathermap.web.pageobjects.HomePage;
import org.openweathermap.web.pageobjects.SearchPage;

public class HomePageSteps extends DriverFactory {

    HomePage homePage = new HomePage();
    SearchPage searchPage;

    @When("^I enter \"([^\"]*)\" to search on Header")
    public void searchCity(String city) {
        searchPage = homePage.searchCityFromHeader(city);
    }

    @And("^I enter \"([^\"]*)\" in the search$")
    public void enterSearch(String searchTerm) {
        homePage.setSearchCity(searchTerm);
    }

    @And("^I click on Search$")
    public void iClickOnSearch() {
        homePage.clickSearchButton();
    }

    @And("^I switch the weather to Fahrenheit$")
    public void switchToFahrenheit() {
        homePage.switchToFahrenheitDegree();
    }

    @And("^I switch the weather to Celsius$")
    public void switchToCelsius() {
        homePage.switchToCelsiusDegree();
    }

    @Then("^Openweathermap home page is displayed$")
    public void homePageIsDisplayed() {
        homePage.checkForTitle("Сurrent weather and forecast - OpenWeatherMap");
    }

    @Then("^No Results should be displayed$")
    public void noResultsShouldBeDisplayed() {
        homePage.verifyNoCityFound();
    }

    @Then("^\"([^\"]*)\" weather details should be displayed$")
    public void weatherDetailsOfCityDisplayed(String cityName) {
        homePage.verifyCityNameDisplayed(cityName);
    }

    @Then("^\"([^\"]*)\" City weather should be displayed in \"([^\"]*)\"$")
    public void cityWeatherShouldBeDisplayedInUnit(String cityName, String tempUnit) {
        homePage.verifyCurrentTemperatureDegree(cityName, tempUnit);
    }

    @And("^I click on Search and select \"([^\"]*)\" from suggestion$")
    public void clickOnSearchAndSelectFromSuggestion(String city) {
        homePage.clickSearchButton();
        homePage.selectSearchCity(city);
    }
}
