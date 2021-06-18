package org.openweathermap.web.stepdefinitions;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.openweathermap.common.DriverFactory;
import org.openweathermap.web.pageobjects.BasePage;
import org.openweathermap.web.pageobjects.HomePage;

public class CommonSteps extends DriverFactory {

    private String scenDesc;

    @Before
    public void before(Scenario scenario) {
        this.scenDesc = scenario.getName();
    }

    @Given("^Web browser is launched$")
    public void openWebBrowser() {
        //setUp(); -- we don't need this because it's already start browser from @Before method in Hook
    }

    @When("^I navigate to the Openweathermap home page$")
    public HomePage navigateToHomePage() {
        new BasePage().navigateToHomePage();
        return new HomePage();
    }
}
