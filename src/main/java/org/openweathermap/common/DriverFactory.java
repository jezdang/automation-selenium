package org.openweathermap.common;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DriverFactory extends EventFiringWebDriver {
    private static final Logger LOGGER = Logger.getLogger(DriverFactory.class);

    public static WebDriver webDriver;

    private static final Thread close_thread = new Thread() {
        @Override
        public void run() {
            webDriver.quit();
        }
    };

    static {
        Runtime.getRuntime().addShutdownHook(close_thread);
        try {
            webDriver = BrowserFactory.getDriver();
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            LOGGER.info("########## Start new shared driver [" + timeStamp + "] ##########");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new Error(throwable);
        }
    }

    public DriverFactory() {
        super(webDriver);
        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }


}