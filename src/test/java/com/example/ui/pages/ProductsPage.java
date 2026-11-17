package com.example.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProductsPage {

    private final SelenideElement title = $("[data-test='title']");
    private final ElementsCollection products = $$("[data-test='inventory-item']");
    private final ElementsCollection productNames = $$("[data-test='inventory-item-name']");
    private final ElementsCollection productPrices = $$("[data-test='inventory-item-price']");
    private final SelenideElement sortSelect = $("[data-test='product-sort-container']");
    private final SelenideElement cartLink = $("[data-test='shopping-cart-link']");

    public SelenideElement title() {
        return title;
    }

    public ElementsCollection products() {
        return products;
    }

    public ElementsCollection productNames() {
        return productNames;
    }

    public ElementsCollection productPrices() {
        return productPrices;
    }

    public ProductsPage sortBy(String optionValue) {
        sortSelect.selectOptionByValue(optionValue);
        return this;
    }

    public ProductsPage addToCart(String productName) {
        products.findBy(text(productName)).$("button").click();
        return this;
    }

    public CartPage openCart() {
        cartLink.click();
        return new CartPage();
    }
}