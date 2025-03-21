Feature: Buy a trip

  Scenario: Successful buy
    When I navigate to "https://blazedemo.com/"
    And I set the departure to Paris
    And I set the arrival to Rome
    And I click Submit
    And I choose the first flight
    And I set the name "Rúben Garrido" in the input field
    And I click Submit
    Then the title should be "BlazeDemo Confirmation"