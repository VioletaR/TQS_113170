package ua.deti.tqc.lab04_03;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SeleniumJupiter.class)
public class HarryPotter {
  @Test
  public void FindHarryPotter(FirefoxDriver driver) {
    driver.get("https://cover-bookstore.onrender.com/");
    driver.manage().window().setSize(new Dimension(1918, 538));

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-testid='book-search-input']")));
    searchInput.click();
    searchInput.sendKeys("Harry Potter");
    searchInput.sendKeys(Keys.ENTER);

    WebElement bookSearchResult = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid='book-search-item']")));
    bookSearchResult.click();

    WebElement bookDetailTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".BookDetails_bookTitle__1eJ1S")));

    assertTrue(bookDetailTitle.getText().contains("Harry Potter"));

  }
}

