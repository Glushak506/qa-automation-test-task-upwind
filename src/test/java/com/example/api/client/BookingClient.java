package com.example.api.client;

import com.example.api.model.Booking;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static com.example.config.TestConfig.apiBaseUrl;
import static io.restassured.RestAssured.given;

/**
 * One method = one HTTP call. The client sends requests. and the test decides what to check.
 */
public class BookingClient {

    private final RequestSpecification spec = new RequestSpecBuilder()
            .setBaseUri(apiBaseUrl())
            .setContentType(ContentType.JSON)
            .setAccept("application/json")
            .addFilter(new AllureRestAssured())
            .build();

    public Response createBooking(Booking booking) {
        return given(spec)
                .body(booking)
                .post("/booking");
    }

    public Response getBooking(int bookingId) {
        return given(spec)
                .pathParam("id", bookingId)
                .get("/booking/{id}");
    }
}
