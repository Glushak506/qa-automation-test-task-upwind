package com.example.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response of POST /booking: the new id and the saved booking.
 */
public record CreatedBooking(
        @JsonProperty("bookingid") int bookingId,
        Booking booking ) {}
