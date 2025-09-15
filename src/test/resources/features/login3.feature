Feature: Login Functionality

  Scenario: Verify home page title
    Given I navigate to "https://example.com"
    Then the page title should be "Example Domain"
    And wait 3
