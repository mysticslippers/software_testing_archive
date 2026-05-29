package me.ifmo;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public abstract class BaseTest {
    protected WebDriver driver;
    protected String browser = System.getProperty("browser");
    protected boolean isRemote = Boolean.parseBoolean(System.getProperty("remote"));

    @BeforeEach
    public void setUp() throws MalformedURLException {
        if(isRemote){
            URL seleniumUrl = new URL("http://localhost:4444/wd/hub");

            if(browser.equalsIgnoreCase("chrome")){
                driver = new RemoteWebDriver(seleniumUrl, new ChromeOptions());
            } else if(browser.equalsIgnoreCase("firefox")){
                driver = new RemoteWebDriver(seleniumUrl, new FirefoxOptions());
            } else {
                throw new MalformedURLException("Unsupported browser: " + browser);
            }
        } else {
            if(browser.equalsIgnoreCase("chrome")){
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            } else if(browser.equalsIgnoreCase("firefox")){
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            } else {
                throw new MalformedURLException("Unsupported browser: " + browser);
            }
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterEach
    public void tearDown() {
        if(driver != null){
            driver.quit();
        }
    }
}
