package org.openweathermap.common;

import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.common.Utils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class BrowserFactory implements SauceOnDemandSessionIdProvider {
    private final static String USER_NAME = Utils.readPropertyOrEnv("SAUCE_USERNAME", "");
    private final static String ACCESS_KEY = Utils.readPropertyOrEnv("SAUCE_ACCESS_KEY", "");
    private static final String SELENIUM_URI = "@ondemand.saucelabs.com:443/wd/hub";
    public static final String SAUCE_LABS_URL = "https://" + USER_NAME + ":" + ACCESS_KEY + SELENIUM_URI;
    private static final long SAUCELABS_ID = System.currentTimeMillis() % 100000;
    public static WebDriver driver;
    private static boolean useSauceLabsHub;

    private static String sessionId;

    static WebDriver getBrowser(String browserName) {

        /* Using Chrome as default browser */
        if (browserName == null) {
            WebDriverManager.chromedriver().setup();
            return new ChromeDriver();
        }

        /* Based on browserName value - running with corresponding browser driver */
        switch (browserName.toUpperCase()) {
            case "IE":
                WebDriverManager.iedriver().setup();
                return new InternetExplorerDriver();
            case "EDGE":
                WebDriverManager.edgedriver().setup();
                return new EdgeDriver();
            case "FIREFOX":
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver();
            default:
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver();
        }
    }

    public static WebDriver getDriver() {
        String browserName = System.getProperty("browserName");

        /* Run test on Sauce Labs Hub */
        if (useSauceLabsHub) {
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setBrowserName(System.getenv("Browser"));
            //caps.setVersion(System.getenv("SELENIUM_VERSION"));
            caps.setCapability("platform", System.getenv("OS"));
            try {
                driver = new RemoteWebDriver(new URL(SAUCE_LABS_URL), caps);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            //Sauce labs reporting
            String message = String.format("SauceOnDemandSessionID=%1$s job-name=%2$s",
                    (((RemoteWebDriver) driver).getSessionId()).toString(), System.getenv("JOB_NAME"));
            System.out.println(message);
        } else {
            driver = getBrowser(browserName);
            sessionId = (((RemoteWebDriver) driver).getSessionId()).toString();
            driver.manage().timeouts().implicitlyWait(Wait.EXPLICIT_WAIT, TimeUnit.SECONDS);
        }
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        return driver;
    }

    @Override
    public String getSessionId() {
        return this.sessionId;
    }
}

