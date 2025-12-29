package com.demo.manager.selenium.productTest;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.junit.jupiter.api.Assertions.*;

public class GestioneProdottiTest {
    private WebDriver driver;
    private ProductPage productPage;

    @BeforeEach
    public void setup() {
        // Assicurati che il driver sia nel PATH o usa WebDriverManager
        driver = new ChromeDriver();
        driver.get("http://localhost:5173"); // L'URL dove gira il tuo Vue
        productPage = new ProductPage(driver);
    }

    @Test
    @DisplayName("Dovrebbe aggiungere un nuovo prodotto correttamente")
    public void testAggiuntaProdotto() {
        productPage.clickAggiungi();
        productPage.compilaForm("Laptop Test", "SN-123", 10);
        productPage.salvaProdotto();

        // Verifica che la tabella sia apparsa dopo il salvataggio
        assertTrue(productPage.isTabellaVisibile(), "La tabella dei prodotti dovrebbe essere visibile");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}