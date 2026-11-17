package com.example.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Booking as Restful Booker sends and returns it.
 *
 */
public record Booking(
        @JsonProperty("firstname") String firstName,
        @JsonProperty("lastname") String lastName,
        @JsonProperty("totalprice") int totalPrice,
        @JsonProperty("depositpaid") boolean depositPaid,
        @JsonProperty("bookingdates") BookingDates bookingDates,
        @JsonProperty("additionalneeds") String additionalNeeds) {
}
