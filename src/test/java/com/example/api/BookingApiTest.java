package com.example.api;

import com.example.api.client.BookingClient;
import com.example.api.model.Booking;
import com.example.api.model.BookingDates;
import com.example.api.model.CreatedBooking;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("api")
class BookingApiTest {

    private final BookingClient bookingClient = new BookingClient();

    //also we can use @TestTag("abc1234") i think it's very important to project bureaucracy
    @Test
    @DisplayName("Created booking can be read back by its id with the same data")
    void createdBookingCanBeRetrievedById() {
        Booking newBooking = new Booking(
                "John",
                "Smith",
                150,
                true,
                new BookingDates("2027-01-10", "2027-01-15"),
                "Breakfast");

        CreatedBooking created = bookingClient.createBooking(newBooking)
                .then()
                .statusCode(200)
                .extract()
                .as(CreatedBooking.class);

        assertTrue(created.bookingId() > 0, "Booking id should be a positive number");

        Booking saved = bookingClient.getBooking(created.bookingId())
                .then()
                .statusCode(200)
                .extract()
                .as(Booking.class);

        assertAll("Booking returned by GET /booking/{id}",
                () -> assertEquals(newBooking.firstName(), saved.firstName(), "firstName"),
                () -> assertEquals(newBooking.lastName(), saved.lastName(), "lastName"),
                () -> assertEquals(newBooking.totalPrice(), saved.totalPrice(), "totalPrice"),
                () -> assertEquals(newBooking.depositPaid(), saved.depositPaid(), "depositPaid"),
                () -> assertEquals(newBooking.bookingDates(), saved.bookingDates(), "bookingDates"));
    }

    @Test
    @DisplayName("Reading a booking that does not exist returns 404")
    void nonExistentBookingReturnsNotFound() {
        bookingClient.getBooking(0)
                .then()
                .statusCode(404);
    }
}
