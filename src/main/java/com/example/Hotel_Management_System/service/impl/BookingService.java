package com.example.Hotel_Management_System.service.impl;

import com.example.Hotel_Management_System.dto.BookingDto;
import com.example.Hotel_Management_System.dto.Response;
import com.example.Hotel_Management_System.entity.Booking;
import com.example.Hotel_Management_System.entity.Room;
import com.example.Hotel_Management_System.entity.User;
import com.example.Hotel_Management_System.exception.OurException;
import com.example.Hotel_Management_System.repository.BookingRepository;
import com.example.Hotel_Management_System.repository.RoomRepository;
import com.example.Hotel_Management_System.repository.UserRepository;
import com.example.Hotel_Management_System.service.interfac.IBookingService;
import com.example.Hotel_Management_System.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;


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

            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setBookingConfirmationCode(bookingConfirmationCode);
        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a booking : " + e.getMessage());
        }

        return response;

    }



    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response();

        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(()-> new OurException("Booking Not Found"));
            BookingDto bookingDto = Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking,true);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setBooking(bookingDto);

        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error find booking by confirmation code : " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response = new Response();

        try {
            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
            List<BookingDto> bookingDtoList = Utils.mapBookingListEntityToBookingListDTO(bookingList) ;
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setBookingList(bookingDtoList);

        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all bookings : " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();

        try {
            Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new OurException("Booking Does Not Exist"));
            bookingRepository.deleteById(bookingId);
            response.setStatusCode(200);
            response.setMessage("Successful");


        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error canceling a booking : " + e.getMessage());
        }

        return response;
    }



    /**
     * Checks whether a room is available for a given booking request.
     *
     * The room is considered unavailable if the requested check-in and check-out
     * dates overlap in any way with any existing booking.
     *
     * This method scans through all existing bookings and ensures that:
     *  - The requested check-in date does not match another booking's check-in date.
     *  - The requested stay does not fall inside another booking's date range.
     *  - The requested stay does not fully contain another booking.
     *  - The requested stay does not partially overlap another booking.
     *  - Edge cases such as same start/end dates are also treated as conflicts.
     *
     * If none of the existing bookings conflict with the request, the room is
     * considered available.
     *
     * @param bookingRequest the requested booking
     * @param exitingBookings list of existing bookings
     * @return true if no date conflicts exist, false otherwise
     */
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
