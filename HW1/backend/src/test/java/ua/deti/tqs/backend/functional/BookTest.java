package ua.deti.tqs.backend.functional;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

public class BookTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @Given("I navigate to {string}")
    public void iNavigateTo(String url) {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("-headless");

        driver = new FirefoxDriver(options);
        driver.get(url);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // wait for the backend to be ready
    }

    @When("I fill in name with {string}")
    public void iFillInNameWith(String name) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='username-input']")
        ));
        element.sendKeys(name);
    }

    @And("I fill in password with {string}")
    public void iFillInPasswordWith(String password) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='password-input']")
        ));
        element.sendKeys(password);
    }

    @And("I click on the signup button")
    public void iClickOnSignupButton() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("[data-testid='login-button']")
        ));
        element.click();
    }

    @And("I search for the location: {string}")
    public void iSearchForTheLocation(String location) {
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='restaurant-search-input']")
        ));
        searchInput.sendKeys(location);
    }

    @And("I click on the 'View Meals' button")
    public void iClickOnTheViewMealsButton() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("[data-testid='view-meals-button']")
        ));
        element.click();
    }

    @And("I click on the 'Book' button")
    public void iClickOnTheBookButton() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("[data-testid='book-button']")
        ));
        element.click();
    }

    @When("I click on the 'My Meals' button")
    public void iClickOnTheMyMealsButton() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("[data-testid='my-meals-button']")
        ));
        element.click();
    }

    @Then("I click on the 'Cancel Reservation' button and can no longer see the meal")
    public void iClickOnTheCancelReservationButton() {
        WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("[data-testid='cancel-button']")
        ));
        cancelButton.click();

        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[data-testid='meal-name']")
        ));

        List<WebElement> currentMealsAfter = driver.findElements(By.cssSelector("[data-testid='meal-name']"));
        assertThat(currentMealsAfter)
                .as("Expected no meals to be displayed after cancellation")
                .isEmpty();
    }

    @Then("I should see the reservation code")
    public void iShouldSeeTheReservationCode() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='reservation-code']")
        ));
        String reservationCode = element.getText();
        assertThat(reservationCode).isNotEmpty();
    }

}