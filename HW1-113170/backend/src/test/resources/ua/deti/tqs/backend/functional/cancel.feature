Feature: Cancel a Meal Reservation

  Background:
    Given I navigate to "http://localhost"
    When I fill in name with "teste"
    And I fill in password with "teste"
    And I click on the signup button

  Scenario: UnBook a meal
    When I click on the 'My Meals' button
    Then I click on the 'Cancel Reservation' button and can no longer see the meal