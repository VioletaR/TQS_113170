Feature: Book a Meal Reservation

  Background:
    Given I navigate to "http://localhost"
    When I fill in name with "teste"
    And I fill in password with "teste"
    And I click on the signup button
    And I search for the location: "Aveiro"
    And I click on the 'View Meals' button

  Scenario: Successfully book a meal
    When I click on the 'Book' button
    Then I should see the reservation code