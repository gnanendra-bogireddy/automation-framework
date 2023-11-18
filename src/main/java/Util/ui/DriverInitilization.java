package Util.ui;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

public class DriverInitilization {

    public WebDriver driverSetup(String browserType) {
        switch (browserType) {
            case "edge":
                return new EdgeDriver();
            case "firefox":
                return new FirefoxDriver();
            case "safari":
                return new SafariDriver();
            default:
                return new ChromeDriver();
        }
    }

    public void remoteDriverSetUp() {

    }

}
