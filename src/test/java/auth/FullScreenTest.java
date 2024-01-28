package auth;

import factory.DriverFactory;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tools.WaitTools;

// Задание 3. Открытие в режиме полного экрана
public class FullScreenTest {

    static Logger logger = LogManager.getLogger(FullScreenTest.class);
    private WebDriver driver;
    private WaitTools waitTools;

    private String baseUrl = System.getProperty("base.url");
    private String login = System.getProperty("login");
    private String password = System.getProperty("password");

    @BeforeEach
    public void initDriver() {
        driver = new DriverFactory("--start-fullscrean").create();
        logger.info("Start driver and open browser in fullscrean");
        waitTools = new WaitTools(driver);
        driver.get(baseUrl);
        logger.info("Open url");
    }

        @AfterEach
        public void driverQuit() {
            if(driver != null) {
                logger.info("Close browser");
                driver.close();
                driver.quit();
            }
        }

    @Test
    public void AuthTest() {
        String signInButtonLocator = "//button[text()='Войти']";

        waitTools.waitForCondition(ExpectedConditions.presenceOfElementLocated(By.xpath(signInButtonLocator)));
        waitTools.waitForCondition(ExpectedConditions.elementToBeClickable(By.xpath(signInButtonLocator)));

        WebElement signInButton = driver.findElement(By.xpath(signInButtonLocator));

        String signInPopupSelector = "#__PORTAL__ > div";
        Assertions.assertTrue(waitTools.waitForCondition(ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(By.cssSelector(signInPopupSelector)))),
                "Error! SignInPopup is open");

        signInButton.click();

        WebElement authPopupElement = driver.findElement(By.cssSelector(signInPopupSelector));

        Assertions.assertTrue(waitTools.waitForCondition(ExpectedConditions.visibilityOf(authPopupElement)),
                "Error! SignInPopup is not visible");

        driver.findElement(By.xpath("//div[./input[@name='email']]")).click();

        WebElement emailInputField = driver.findElement(By.cssSelector("input[name='email']"));
        waitTools.waitForCondition(ExpectedConditions.stalenessOf(emailInputField));
        emailInputField.sendKeys(login);
        logger.info("Login was entered");

        WebElement passwordInputField = driver.findElement(By.cssSelector("input[type='password']"));

        driver.findElement(By.xpath("//div[./input[@type='password']]")).click();
        waitTools.waitForCondition(ExpectedConditions.stalenessOf(passwordInputField));
        passwordInputField.sendKeys(password);
        logger.info("Password was entered");

        driver.findElement(By.cssSelector("#__PORTAL__ button")).click();
        logger.info("Successful authorization");

     Assertions.assertTrue(waitTools
                .waitForCondition(ExpectedConditions
                        .presenceOfElementLocated(By.cssSelector("img[src*='blue-owl']"))));

        String cookies = driver.manage().getCookies().toString();
        logger.info("Cookies:" + cookies);
    }

    }

