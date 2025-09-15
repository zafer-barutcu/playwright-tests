@smart
Feature: Smart Locator Functionality

  Scenario: Smart Locator Scenario
    Given I navigate to "https://example.com"
    Then Verify page title should be "Example Domain"
    Then Verify link text should be "More information..."