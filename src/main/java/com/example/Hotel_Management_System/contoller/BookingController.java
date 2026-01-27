package com.example.Hotel_Management_System.contoller;


import com.example.Hotel_Management_System.dto.Response;
import com.example.Hotel_Management_System.entity.Booking;
import com.example.Hotel_Management_System.service.interfac.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private IBookingService bookingService;

    @PutMapping("/book-room/{roomId}/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> saveBookings(@PathVariable Long rooId,
                                                 @PathVariable Long userId,
                                                 @RequestBody Booking bookingRequest){

        Response response = bookingService.saveBooking(rooId, userId, bookingRequest);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
