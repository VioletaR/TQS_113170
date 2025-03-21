package ua.deti.tqs;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SeleniumJupiter.class)
public class BuyATripTest {
    @Test
    public void buyatrip(FirefoxDriver driver) {
        driver.get("https://blazedemo.com/");
        driver.manage().window().setSize(new Dimension(1200, 800));
        driver.findElement(By.cssSelector(".form-inline:nth-child(1) > option:nth-child(1)")).click();

        WebElement dropdown = driver.findElement(By.name("toPort"));
        dropdown.findElement(By.xpath("//option[. = 'Rome']")).click();

        driver.findElement(By.cssSelector(".form-inline:nth-child(4) > option:nth-child(2)")).click();
        driver.findElement(By.cssSelector(".btn-primary")).click();
        driver.findElement(By.cssSelector("tr:nth-child(2) .btn")).click();

        driver.findElement(By.id("inputName")).click();
        driver.findElement(By.id("inputName")).sendKeys("Rúben Garrido");

        driver.findElement(By.cssSelector(".btn-primary")).click();

        assertEquals("BlazeDemo Confirmation", driver.getTitle());
    }
}