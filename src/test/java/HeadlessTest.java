import factory.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tools.WaitTools;

// Задание 1. Открытие в headless режиме
public class HeadlessTest {

    static Logger logger = LogManager.getLogger(HeadlessTest.class);
    private WebDriver driver;
    private WaitTools waitTools;

    private String duckUrl = System.getProperty("duck.url");

    @BeforeEach
    public void initDriver() {
     //   driver = new DriverFactory("--headless").create();
        driver = new DriverFactory("--start-fullscrean").create();
        logger.info("Start driver and open browser in headless");
        waitTools = new WaitTools(driver);
        driver.get(duckUrl);
        logger.info("Open url");
    }

    @AfterEach
    public void driverQuit() {
        if (driver != null) {
            logger.info("Close browser");
            driver.close();
            driver.quit();
        }
    }

    @Test
    public void duckTest() {
        driver.findElement(By.id("searchbox_input")).sendKeys("отус" + Keys.ENTER);
        logger.info("Search is completed");

        WebElement firstLink = driver.findElement
              (By.xpath("//span[text()='Онлайн‑курсы для профессионалов, дистанционное обучение современным ...']"));
        waitTools.waitForCondition(ExpectedConditions.stalenessOf(firstLink));

      Assertions.assertEquals("Онлайн‑курсы для профессионалов, дистанционное обучение современным ...", firstLink.getText());
      logger.info("Link is correct");
    }

}
