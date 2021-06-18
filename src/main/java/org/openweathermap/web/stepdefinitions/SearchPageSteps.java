package org.openweathermap.web.stepdefinitions;

import cucumber.api.java.en.Then;
import org.openweathermap.common.DriverFactory;
import org.openweathermap.web.pageobjects.SearchPage;

public class SearchPageSteps extends DriverFactory {

    SearchPage searchPage = new SearchPage();

    @Then("^\"(\\d+)\" citi\\(es\\) weather details should be displayed$")
    public void weatherDetailsOfCityDisplayed(int nbCities) {
        searchPage.verifyNumberOfCitiesAfterSearch(nbCities);
    }

}
