
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

// page_url = https://blazedemo.com/reserve.php
public class FlightPage {
    @FindBy(css = "tr:nth-child(1) .btn")
    private WebElement chooseFlightButton;

    public FlightPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void chooseFlight() {
        chooseFlightButton.click();
    }
}