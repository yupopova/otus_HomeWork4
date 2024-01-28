import factory.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tools.WaitTools;

import java.util.List;

// Задание 2. Открытие в режиме киоска
public class KioskTest {

    static Logger logger = LogManager.getLogger(KioskTest.class);
    private WebDriver driver;
    private WaitTools waitTools;

    private String layoutsUrl = System.getProperty("layouts.url");

    @BeforeEach
    public void initDriver() {
        driver = new DriverFactory("--kiosk").create();
        logger.info("Start driver and open browser in kiosk");
        waitTools = new WaitTools(driver);
        driver.get(layoutsUrl);
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
    public void imageOpenInModalWindow() {
        List<WebElement> images = driver.findElements(By.cssSelector("div.content-overlay"));
        logger.info("Images are available");

        waitTools.waitForCondition(ExpectedConditions.stalenessOf(images.get(0)));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView()", images.get(0));
        images.get(0).click();
        logger.info("Click on image");

        Assertions.assertTrue(waitTools
                .waitForCondition(ExpectedConditions
                        .presenceOfElementLocated(By.cssSelector("div.pp_pic_holder.light_rounded"))),
                "Modal window is not open");
        logger.info("Modal window is open");

        Assertions.assertTrue(waitTools
                        .waitForCondition(ExpectedConditions
                                .presenceOfElementLocated(By.cssSelector("div.pp_hoverContainer"))),
                "Image is not displayed in modal window");
        logger.info("Image is displayed in modal window");
    }
}