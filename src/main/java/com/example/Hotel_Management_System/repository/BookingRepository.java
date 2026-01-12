package com.example.Hotel_Management_System.repository;

import com.example.Hotel_Management_System.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findAllByRoomId(Long roomId);

    List<Booking>findByBookingConfirmationCode(String confirmationCode);

    List<Booking>findByUserId(Long userId);
}
