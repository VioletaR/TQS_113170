
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class MainPage {
    @FindBy(css = ".form-inline:nth-child(1) > option:nth-child(1)")
    private WebElement departure;

    @FindBy(name = "toPort")
    private WebElement destination;

    @FindBy(css = ".btn-primary")
    private WebElement findButton;

    public MainPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void setDepartureSelect() {
        departure.click();
    }

    public void setDestinationSelect() {
        destination.findElement(By.xpath("//option[. = 'Rome']")).click();
    }

    public void clickFindButton() {
        findButton.click();
    }
}