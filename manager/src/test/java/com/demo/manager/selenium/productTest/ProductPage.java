package com.demo.manager.selenium.productTest;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProductPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    // Selettori basati sui data-test
    private By addBtn = By.cssSelector("[data-test='add-product']");
    private By nameInput = By.cssSelector("[data-test='product-name']");
    private By codeInput = By.cssSelector("[data-test='product-code']");
    private By qtyInput = By.cssSelector("[data-test='product-quantity']");
    private By saveBtn = By.cssSelector("[data-test='save-product']");
    private By table = By.cssSelector("[data-test='products-table']");

    public void clickAggiungi() {
        wait.until(ExpectedConditions.elementToBeClickable(addBtn)).click();
    }

    public void compilaForm(String nome, String codice, int qta) {
        // Aspetta che il form sia visibile (v-if="showForm")
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        driver.findElement(nameInput).sendKeys(nome);
        driver.findElement(codeInput).sendKeys(codice);
        driver.findElement(qtyInput).sendKeys(String.valueOf(qta));
    }

    public void salvaProdotto() {
        driver.findElement(saveBtn).click();
    }

    public boolean isTabellaVisibile() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(table)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}