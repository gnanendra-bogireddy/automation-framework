package UI;

import Util.ui.DriverInitilization;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class BrowserActions {
    private DriverInitilization driverInitilization = new DriverInitilization();
    private final WebDriver driver = this.launchBrowser();

    public WebDriver launchBrowser() {
        String driverTye = System.getProperty("browser", "chrome");
        return driverInitilization.driverSetup(driverTye);
    }
    public void getURL(String url, int timeout) {
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
    }


}
