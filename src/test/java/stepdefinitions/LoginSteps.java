package stepdefinitions;

import hooks.CucumberHooks;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import pages.HomePage;


public class LoginSteps extends BaseTests {
    HomePage homePage = new HomePage(CucumberHooks.page.get());

    @Given("I navigate to {string}")
    public void iNavigateTo(String url) {
        homePage.navigateTo(url);
    }


    @Then("the page title should be {string}")
    public void thePageTitleShouldBe(String expectedTitle) {
        Assert.assertEquals(expectedTitle, homePage.getPageTitle());
    }

    @And("wait {int}")
    public void wait(int sec) throws InterruptedException {
        Thread.sleep(sec * 1000);
    }



}