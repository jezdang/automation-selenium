Feature: API Testing for OpenWeatherMap

  @APITest
  Scenario Outline: Register weather station successfully
    When I have registered a new station to OpenWhetherMap with values of "<external_id>","<name>","<latitude>","<longitude>","<altitude>"
    Then I have received HTTP response code of "201"
    Then I see my new station with values of "<external_id>","<name>","<latitude>","<longitude>","<altitude>"

    Examples:
      | external_id | name                 | latitude | longitude | altitude |
      | API_TEST001 | API Test Station 001 | 37.76    | -122.43   | 150      |