Feature: End to End test for Open weather map city search

  @RegressionTest
  Scenario: Verify home page of Open Weather Map
    Given Web browser is launched
    When  I navigate to the Openweathermap home page
    Then  Openweathermap home page is displayed

  @RegressionTest
  Scenario: Invalid City Search
    Given Web browser is launched
    When I navigate to the Openweathermap home page
    And I enter "hkjhk" in the search
    And I click on Search
    Then No Results should be displayed

  @RegressionTest
  Scenario: Valid City Search
    Given Web browser is launched
    When I navigate to the Openweathermap home page
    And I enter "Mumbai" in the search
    And I click on Search and select "Mumbai" from suggestion
    Then "Mumbai" weather details should be displayed

  @RegressionTest
  Scenario: Valid City Search displays multiple results
    Given Web browser is launched
    When I navigate to the Openweathermap home page
    And I enter "London" to search on Header
    Then "5" citi(es) weather details should be displayed

  @RegressionTest
  Scenario: Change temperature display from C to F
    Given Web browser is launched
    When I navigate to the Openweathermap home page
    And I enter "Paris 14" in the search
    And I click on Search and select "Paris" from suggestion
    And I switch the weather to Fahrenheit
    Then "Paris 14" City weather should be displayed in "Fahrenheit"