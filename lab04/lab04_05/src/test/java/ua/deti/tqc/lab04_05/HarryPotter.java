package  ua.deti.tqc.lab04_05;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HarryPotter {

  @Test
  public void FindHarryPotter() throws Exception {
    URL remoteUrl = new URL("http://selenium-firefox:4444/wd/hub");

    RemoteWebDriver driver = new RemoteWebDriver(remoteUrl, new FirefoxOptions());
    try {
      driver.get("https://cover-bookstore.onrender.com/");
      driver.manage().window().setSize(new Dimension(1918, 538));

      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

      WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-testid='book-search-input']")));
      searchInput.sendKeys("Harry Potter", Keys.ENTER);

      WebElement bookSearchResult = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid='book-search-item']")));
      bookSearchResult.click();

      WebElement bookDetailTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".BookDetails_bookTitle__1eJ1S")));
      assertTrue(bookDetailTitle.getText().contains("Harry Potter"), "Book title does not contain 'Harry Potter'");

      System.out.println("Test passed");
    } finally {
      driver.quit();
    }
  }
}
