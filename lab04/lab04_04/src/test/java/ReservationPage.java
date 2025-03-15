
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

// page_url = https://blazedemo.com/purchase.php
public class ReservationPage {
    @FindBy(id = "inputName")
    private WebElement nameInput;

    @FindBy(css = ".btn-primary")
    private WebElement submit;

    public ReservationPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void clickNameInput() {
        nameInput.click();
    }

    public void setNameInput(String name) {
        nameInput.sendKeys(name);
    }

    public void submitForm() {
        submit.click();
    }
}