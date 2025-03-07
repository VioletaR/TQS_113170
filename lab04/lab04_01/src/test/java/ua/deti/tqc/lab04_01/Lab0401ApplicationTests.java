package ua.deti.tqc.lab04_01;


import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;

import static java.lang.invoke.MethodHandles.lookup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

@ExtendWith(SeleniumJupiter.class)
class Lab0401ApplicationTests {

    static final Logger log = getLogger(lookup().lookupClass());
    private static final String SUT_URL = "https://bonigarcia.dev/selenium-webdriver-java/";

    @Test
    void testTitle(FirefoxDriver driver) {
        // Exercise
        driver.get(SUT_URL);
        String title = driver.getTitle();
        log.debug("The title of {} is {}", SUT_URL, title);

        // Verify
        assertThat(title).isEqualTo("Hands-On Selenium WebDriver with Java");
    }

    @Test
    void testCalculator(FirefoxDriver driver) {
        // Exercise
        driver.get(SUT_URL);
        driver.findElement(By.linkText("Slow calculator")).click();
        String url = driver.getCurrentUrl();

        // Verify
        assertThat(url).isEqualTo("https://bonigarcia.dev/selenium-webdriver-java/slow-calculator.html");
    }

}
