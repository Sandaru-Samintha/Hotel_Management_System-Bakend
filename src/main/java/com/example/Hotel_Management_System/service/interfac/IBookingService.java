package com.example.Hotel_Management_System.service.interfac;

import com.example.Hotel_Management_System.dto.Response;
import com.example.Hotel_Management_System.entity.Booking;

public interface IBookingService {

    Response saveBooking(Long roomId , Long userId , Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(Long bookingId);
}
