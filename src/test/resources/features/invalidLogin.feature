Feature: Invalid Login Functionality

  Scenario: Verify home page title
    Given I navigate to "https://google.com"
    Then the page title should be "Google"
    And wait 3
