package com.demo.manager.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.time.Duration;

public class FirstScript {
    public static void main(String[] args) {
        // initialize the WebDriver
        WebDriver driver = new EdgeDriver();

        // using get function to navigate in website
        driver.get("https://www.selenium.dev/selenium/web/web-form.html");

        // get the title from the website
        driver.getTitle();

        // establish waiting strategy
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        // using following command to find an element
        WebElement textBox = driver.findElement(By.name("my-text"));
        WebElement submitButton = driver.findElement(By.cssSelector("button"));

        // take an action for an element
        textBox.sendKeys("Selenium");
        submitButton.click();

        // request element information
        WebElement message = driver.findElement(By.id("message"));
        message.getText();

        // end the session
        driver.quit();
    }
}
