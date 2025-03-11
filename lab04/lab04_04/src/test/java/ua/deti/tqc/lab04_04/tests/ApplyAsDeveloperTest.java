package ua.deti.tqc.lab04_04.tests;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import ua.deti.tqc.lab04_04.webpages.DeveloperApplyPage;
import ua.deti.tqc.lab04_04.webpages.DeveloperPortalPage;
import ua.deti.tqc.lab04_04.webpages.HomePage;

import java.util.concurrent.TimeUnit;

public class ApplyAsDeveloperTest {
    WebDriver driver;

    @Before
    public void setup(){
        //use FF Driver
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void applyAsDeveloper() {
        //Create object of HomePage Class
        HomePage home = new HomePage(driver);
        home.clickOnDeveloperApplyButton();

        //Create object of DeveloperPortalPage
        DeveloperPortalPage devportal= new DeveloperPortalPage(driver);

        //Check if page is opened
        Assert.assertTrue(devportal.isPageOpened());

        //Click on Join Toptal
        devportal.clikOnJoin();

        //Create object of DeveloperApplyPage
        DeveloperApplyPage applyPage =new DeveloperApplyPage(driver);

        //Check if page is opened
        Assert.assertTrue(applyPage.isPageOpened());

        //Fill up data
        applyPage.setDeveloper_email("dejan@toptal.com");
        applyPage.setDeveloper_full_name("Dejan Zivanovic Automated Test");
        applyPage.setDeveloper_password("password123");
        applyPage.setDeveloper_password_confirmation("password123");
        applyPage.setDeveloper_skype("automated_test_skype");

        //Click on join
        //applyPage.clickOnJoin();
    }

    @After
    public void close(){
        driver.close();
    }
}