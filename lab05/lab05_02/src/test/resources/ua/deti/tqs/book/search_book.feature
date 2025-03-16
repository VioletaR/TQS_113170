Feature: Book Search
  As a customer
  I want to search for books by publication date
  So that I can find books published within a specific time range

  Scenario: Search for books within a date range
    Given I have a list of books
      | title      | author   | published  |
      | Book One   | Author A | 2020-05-10 |
      | Book Two   | Author B | 2019-07-15 |
      | Book Three | Author C | 2021-03-20 |
    When the customer searches for books published between 2019 and 2020
    Then 2 books should have been found
    And Book 1 should have the title "Book One"
    And Book 2 should have the title "Book Two"

  Scenario: Search for books outside of the date range
    Given I have a list of books
      | title      | author   | published  |
      | Book One   | Author A | 2020-05-10 |
      | Book Two   | Author B | 2019-07-15 |
      | Book Three | Author C | 2021-03-20 |
    When the customer searches for books published between 2017 and 2018
    Then 0 books should have been found

  Scenario: Search for books with invalid dates
    Given I have a list of books
      | title    | author   | published  |
      | Book One | Author A | 2020-05-10 |
    When the customer searches for books published between 0001 and 0000
    Then 0 books should have been found
    When the customer searches for books published between 0001 and 0001
    Then 0 books should have been found





