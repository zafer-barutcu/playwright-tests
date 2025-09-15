package stepdefinitions;

import io.cucumber.java.en.Then;

import static hooks.CucumberHooks.smartLocator;
import static org.junit.Assert.assertEquals;


public class SampleSteps extends BaseTests {

    @Then("Verify page title should be {string}")
    public void verifyPageTitleShouldBe(String title) {
        assertEquals(title,smartLocator.find("pageHeader").innerText());
    }

    @Then("Verify link text should be {string}")
    public void verifyLinkTextShouldBe(String linkText) {
        assertEquals(linkText,smartLocator.find("moreInformationLink").innerText());
    }
}
