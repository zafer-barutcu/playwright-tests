@smartGoogle
Feature: Google Search Functionality

  Scenario: Google Search Scenario
    Given I navigate to "https://www.google.com/"
    And User enters search value as "Fidelis Security"
    And User click "Enter" button on keyboard
    And wait 5

