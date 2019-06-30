package org.ouracademy.justask;

import java.awt.Robot;
import java.io.IOException;

import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Open
 */
public class Open extends Command {

    @Override
    public String getName() {
        return "open";
    }

    @Override
    public void execute(Robot robot, String text) {
        System.setProperty("webdriver.chrome.driver", "/usr/bin/google-chrome");
        ChromeDriver driver = new ChromeDriver();

        // And now use this to visit Google
        driver.get("http://www.google.com");

    }

}