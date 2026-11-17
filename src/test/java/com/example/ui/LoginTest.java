package com.example.ui;

import com.example.ui.pages.LoginPage;
import com.example.ui.pages.ProductsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static com.example.constants.TestData.PASSWORD;
import static com.example.constants.TestData.USERNAME;

class LoginTest extends BaseUiTest {

    @Test
    @DisplayName("Standard user logs in and sees the Products page with products")
    void loginWithValidCredentialsOpensProductsPage() {
        ProductsPage productsPage = new LoginPage()
                .open()
                .login(USERNAME, PASSWORD);

        webdriver().shouldHave(urlContaining("/inventory.html"));
        productsPage.title().shouldHave(exactText("Products"));
        productsPage.products().shouldHave(sizeGreaterThan(0));
    }
}
