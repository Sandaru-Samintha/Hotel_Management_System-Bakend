package com.example.Hotel_Management_System.utils;

import com.example.Hotel_Management_System.dto.BookingDto;
import com.example.Hotel_Management_System.dto.RoomDto;
import com.example.Hotel_Management_System.dto.UserDto;
import com.example.Hotel_Management_System.entity.Booking;
import com.example.Hotel_Management_System.entity.Room;
import com.example.Hotel_Management_System.entity.User;

import java.security.SecureRandom;
import java.util.stream.Collectors;

public class Utils {

    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final SecureRandom secureRandom = new SecureRandom();


    public static String generateRandomAlphanumeric(int length){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0;i<length ;i++){
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    public static UserDto mapUserEntityToUserDTO(User user){
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRole(user.getRole());

        return userDto;
    }

    public static RoomDto mapRoomEntityToRoomDTO(Room room){
        RoomDto roomDto = new RoomDto();

        roomDto.setId(room.getId());
        roomDto.setRoomType(room.getRoomType());
        roomDto.setRoomPrice(room.getRoomPrice());
        roomDto.setRoomPhotoUrl(room.getRoomPhotoUrl());

        return roomDto;
    }

    public static BookingDto mapBookingEntityToBookingDTO(Booking booking){
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(bookingDto.getId());
        bookingDto.setCheckInDate(bookingDto.getCheckInDate());
        bookingDto.setCheckOutDate(bookingDto.getCheckOutDate());
        bookingDto.setNumOfAdults(bookingDto.getNumOfAdults());
        bookingDto.setNumOfChildren(bookingDto.getNumOfChildren());
        bookingDto.setTotalNumOfGuest(bookingDto.getTotalNumOfGuest());
        bookingDto.setBookingConfirmationCode(bookingDto.getBookingConfirmationCode());
        return bookingDto;

    }

    public static RoomDto mapRoomEntityToRoomDTOPlusBookings(Room room){
        RoomDto roomDto = new RoomDto();

        roomDto.setId(room.getId());
        roomDto.setRoomType(room.getRoomType());
        roomDto.setRoomPrice(room.getRoomPrice());
        roomDto.setRoomPhotoUrl(room.getRoomPhotoUrl());

        if(room.getBookings() != null){
            roomDto.setBookings(room.getBookings().stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList()));
        }
        return roomDto;
    }

    public static UserDto mapUserEntityToUserDTOPlusUserBookingsAndRoom(User user){
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRole(user.getRole());

        if(!user.getBookings().isEmpty()){
            userDto.setBookings(user.getBookings().stream().map(booking -> mapBookingEntityToBookingDTOPlusBookedRooms(booking,false)).collect(Collectors.toList()));
        }
        return userDto;
    }

    public static BookingDto mapBookingEntityToBookingDTOPlusBookedRooms(Booking booking,boolean mapUser){
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(bookingDto.getId());
        bookingDto.setCheckInDate(bookingDto.getCheckInDate());
        bookingDto.setCheckOutDate(bookingDto.getCheckOutDate());
        bookingDto.setNumOfAdults(bookingDto.getNumOfAdults());
        bookingDto.setNumOfChildren(bookingDto.getNumOfChildren());
        bookingDto.setTotalNumOfGuest(bookingDto.getTotalNumOfGuest());
        bookingDto.setBookingConfirmationCode(bookingDto.getBookingConfirmationCode());

        if(mapUser){
           bookingDto.setUser(Utils.mapUserEntityToUserDTO(booking.getUser()));
        }
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

}
