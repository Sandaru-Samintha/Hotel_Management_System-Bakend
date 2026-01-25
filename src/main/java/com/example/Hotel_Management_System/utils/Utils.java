package com.example.Hotel_Management_System.utils;

// Import DTO classes used for transferring data to client
import com.example.Hotel_Management_System.dto.BookingDto;
import com.example.Hotel_Management_System.dto.RoomDto;
import com.example.Hotel_Management_System.dto.UserDto;

// Import Entity classes that represent database tables
import com.example.Hotel_Management_System.entity.Booking;
import com.example.Hotel_Management_System.entity.Room;
import com.example.Hotel_Management_System.entity.User;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

// Utility class containing helper and mapping methods
public class Utils {

    // String containing all allowed characters for random code generation
    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // SecureRandom is used for generating secure random values
    private static final SecureRandom secureRandom = new SecureRandom();

    // Generates a random alphanumeric string of given length
    // Commonly used for booking confirmation codes
    public static String generateRandomAlphanumeric(int length){
        StringBuilder stringBuilder = new StringBuilder();

        // Loop to generate each character of the string
        for(int i = 0; i < length; i++){
            // Generate a random index from the allowed characters
            int randomIndex =
                    secureRandom.nextInt(ALPHANUMERIC_STRING.length());

            // Get the character at the generated index
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);

            // Append the character to the result string
            stringBuilder.append(randomChar);
        }

        // Return the generated random string
        return stringBuilder.toString();
    }

    // Converts User entity to UserDto (basic user information)
    public static UserDto mapUserEntityToUserDTO(User user){
        UserDto userDto = new UserDto();

        // Copy values from User entity to UserDto
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRole(user.getRole());

        return userDto;
    }

    // Converts Room entity to RoomDto (basic room details)
    public static RoomDto mapRoomEntityToRoomDTO(Room room){
        RoomDto roomDto = new RoomDto();

        // Copy room details from entity to DTO
        roomDto.setId(room.getId());
        roomDto.setRoomType(room.getRoomType());
        roomDto.setRoomPrice(room.getRoomPrice());
        roomDto.setRoomPhotoUrl(room.getRoomPhotoUrl());
        roomDto.setRoomDescription(room.getRoomDescription());

        return roomDto;
    }

    // Converts Booking entity to BookingDto (without room and user details)
    public static BookingDto mapBookingEntityToBookingDTO(Booking booking){
        BookingDto bookingDto = new BookingDto();

        // Set booking information in DTO
        bookingDto.setId(bookingDto.getId());
        bookingDto.setCheckInDate(booking.getCheckInDate());
        bookingDto.setCheckOutDate(booking.getCheckOutDate());
        bookingDto.setNumOfAdults(booking.getNumOfAdults());
        bookingDto.setNumOfChildren(booking.getNumOfChildren());
        bookingDto.setTotalNumOfGuest(booking.getTotalNumOfGuest());
        bookingDto.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        return bookingDto;
    }

    // Converts Room entity to RoomDto including its bookings
    public static RoomDto mapRoomEntityToRoomDTOPlusBookings(Room room){
        RoomDto roomDto = new RoomDto();

        // Copy room details
        roomDto.setId(room.getId());
        roomDto.setRoomType(room.getRoomType());
        roomDto.setRoomPrice(room.getRoomPrice());
        roomDto.setRoomPhotoUrl(room.getRoomPhotoUrl());
        roomDto.setRoomDescription(room.getRoomDescription());

        // Map bookings only if they exist
        if(room.getBookings() != null){
            roomDto.setBookings(
                    room.getBookings()
                            .stream()
                            .map(Utils::mapBookingEntityToBookingDTO)
                            .collect(Collectors.toList())
            );
        }

        return roomDto;
    }

    // Converts User entity to UserDto including bookings and rooms
    public static UserDto mapUserEntityToUserDTOPlusUserBookingsAndRoom(User user){

        UserDto userDto = new UserDto();

        // Copy user details
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRole(user.getRole());

        // Map bookings only if the user has bookings
        if(!user.getBookings().isEmpty()){
            userDto.setBookings(user.getBookings().stream().map(booking -> mapBookingEntityToBookingDTOPlusBookedRooms(booking, false)).collect(Collectors.toList()));
        }

        return userDto;
    }

    // Converts Booking entity to BookingDto including booked room
    // mapUser determines whether user details should be included
    public static BookingDto mapBookingEntityToBookingDTOPlusBookedRooms(Booking booking, boolean mapUser){

        BookingDto bookingDto = new BookingDto();

        // Copy booking details
        bookingDto.setId(booking.getId());
        bookingDto.setCheckInDate(booking.getCheckInDate());
        bookingDto.setCheckOutDate(booking.getCheckOutDate());
        bookingDto.setNumOfAdults(booking.getNumOfAdults());
        bookingDto.setNumOfChildren(booking.getNumOfChildren());
        bookingDto.setTotalNumOfGuest(booking.getTotalNumOfGuest());
        bookingDto.setBookingConfirmationCode(booking.getBookingConfirmationCode()
        );

        // Include user details only if mapUser is true
        if(mapUser){
            bookingDto.setUser(Utils.mapUserEntityToUserDTO(booking.getUser()));
        }

        // Include room details if a room is associated with the booking
        if(booking.getRoom() != null){
            RoomDto roomDto = new RoomDto();

            roomDto.setId(booking.getRoom().getId());
            roomDto.setRoomType(booking.getRoom().getRoomType());
            roomDto.setRoomPrice(booking.getRoom().getRoomPrice());
            roomDto.setRoomPhotoUrl(booking.getRoom().getRoomPhotoUrl());
            roomDto.setRoomDescription(booking.getRoom().getRoomDescription());

            bookingDto.setRoom(roomDto);
        }

        return bookingDto;
    }

    // Converts a list of User entities into a list of User DTOs
    public static List<UserDto> mapUserListEntityToUserListDTO(List<User> userList){
        return userList.stream().map(Utils::mapUserEntityToUserDTO).collect(Collectors.toList());
    }

    // Converts a list of Room entities into a list of Room DTOs
    public static List<RoomDto> mapRoomListEntityToRoomListDTO(List<Room> roomList){
        return roomList.stream().map(Utils::mapRoomEntityToRoomDTO).collect(Collectors.toList());
    }

    // Converts a list of Booking entities into a list of Booking DTOs
    public static List<BookingDto> mapBookingListEntityToBookingListDTO(List<Booking> bookingList){
        return bookingList.stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList());
    }

}
