package com.example.Hotel_Management_System.service.impl;

import com.example.Hotel_Management_System.dto.Response;
import com.example.Hotel_Management_System.entity.Booking;
import com.example.Hotel_Management_System.entity.Room;
import com.example.Hotel_Management_System.entity.User;
import com.example.Hotel_Management_System.exception.OurException;
import com.example.Hotel_Management_System.repository.BookingRepository;
import com.example.Hotel_Management_System.repository.RoomRepository;
import com.example.Hotel_Management_System.repository.UserRepository;
import com.example.Hotel_Management_System.service.interfac.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private IBookingService iBookingService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;




    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {

        Response response = new Response();

        try {
            if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
                throw new IllegalArgumentException("Check in date must com after check out date");
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            List<Booking> exitingBookings = room.getBookings();
            if (!roomIsAvailable(bookingRequest, exitingBookings)) {
              throw new OurException("Room no available for selected date range");
            }
        }catch(OurException e){

        }catch(Exception e){

        }

        return response;

    }



    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        return null;
    }

    @Override
    public Response getAllBookings() {
        return null;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        return null;
    }


    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> exitingBookings) {

        return exitingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                ||(bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                ||(bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))


                                ||(bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))

                );

    }
}
