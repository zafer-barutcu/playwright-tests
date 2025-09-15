package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

import static hooks.CucumberHooks.smartLocator;

public class GoogleSteps extends BaseTests{

    @Then("User enters search value as {string}")
    public void user_enters_search_value_as(String value) {
        fillField(smartLocator.find("googleSearchInput"),value);
    }

    @And("User click {string} button on keyboard")
    public void userClickButtonOnKeyboard(String buttonType) {
        hitKeyboardButton(buttonType);
    }
}
