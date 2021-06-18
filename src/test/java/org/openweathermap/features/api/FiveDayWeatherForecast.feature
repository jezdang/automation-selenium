Feature: Retrieve 5 day 3 hour forecast from weather api
  Everybody wants to know the forecast

  @APITest
  Scenario: Retrieve hourly forecast for city name "Istanbul"
    When User sets up request with city name "Istanbul"
    Then Response status code should be OK

  @APITest
  Scenario: Retrieve 5 day 3 hour forecast for "Istanbul" by zip code
    Given User sets up request with zip code "96797"
    Then Response status code should be OK
    And City name should be "Waipahu"
