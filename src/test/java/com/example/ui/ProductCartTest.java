package com.example.ui;

import com.example.ui.pages.CartPage;
import com.example.ui.pages.LoginPage;
import com.example.ui.pages.ProductsPage;
import com.example.utils.PriceUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.example.constants.TestData.PASSWORD;
import static com.example.constants.TestData.USERNAME;

class ProductCartTest extends BaseUiTest {

    @Test
    @DisplayName("Products are sorted by price (low to high) and the cheapest one can be added to the cart")
    void sortedProductCanBeAddedToCart() {
        ProductsPage productsPage = new LoginPage()
                .open()
                .login(USERNAME, PASSWORD);
        productsPage.products().shouldHave(sizeGreaterThan(0));

        List<String> expectedPrices = productsPage.productPrices().texts().stream()
                .sorted(Comparator.comparingDouble(PriceUtils::toNumber))
                .toList();

        productsPage.sortBy("lohi");
        productsPage.productPrices().shouldHave(exactTexts(expectedPrices));

        String productName = productsPage.productNames().first().getText();
        CartPage cartPage = productsPage
                .addToCart(productName)
                .openCart();

        cartPage.itemNames().shouldHave(exactTexts(productName));
    }
}
