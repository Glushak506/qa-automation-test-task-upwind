package com.example.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.example.config.TestConfig;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;

@Tag("ui")
public abstract class BaseUiTest {

    @BeforeAll
    static void setUp() {
        Configuration.baseUrl = TestConfig.uiBaseUrl();
        Configuration.browserCapabilities.setCapability("webSocketUrl", true);
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterEach
    void tearDown() {
        Selenide.closeWebDriver();
    }
}
