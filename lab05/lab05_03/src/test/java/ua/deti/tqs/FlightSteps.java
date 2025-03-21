package ua.deti.tqs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlightSteps {

    private WebDriver driver;

    @When("I navigate to {string}")
    public void iNavigateTo(String url) {
        driver = WebDriverManager.firefoxdriver().create();
        driver.get(url);
    }

    @And("I click Submit")
    public void iPressEnter() {
        driver.findElement(By.cssSelector(".btn-primary")).click();
    }

    @Then("the title should be {string}")
    public void theTitleShouldBe(String arg0) {
        assertEquals(arg0, driver.getTitle());
    }

    @And("I set the name {string} in the input field")
    public void iSetTheNameInTheInputField(String arg0) {
        driver.findElement(By.id("inputName")).click();
        driver.findElement(By.id("inputName")).sendKeys(arg0);
    }

    @And("I set the departure to Paris")
    public void iSetTheDepartureToParis() {
        driver.findElement(By.cssSelector(".form-inline:nth-child(1) > option:nth-child(1)")).click();
    }

    @And("I set the arrival to Rome")
    public void iSetTheArrivalToRome() {
        WebElement dropdown = driver.findElement(By.name("toPort"));
        dropdown.findElement(By.xpath("//option[. = 'Rome']")).click();
    }

    @And("I choose the first flight")
    public void iChooseTheFirstFlight() {
        driver.findElement(By.cssSelector("tr:nth-child(2) .btn")).click();
    }
}