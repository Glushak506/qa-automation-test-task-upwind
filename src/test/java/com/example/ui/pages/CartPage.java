package com.example.ui.pages;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Selenide.$$;

public class CartPage {

    private final ElementsCollection itemNames = $$("[data-test='inventory-item-name']");

    public ElementsCollection itemNames() {
        return itemNames;
    }
}