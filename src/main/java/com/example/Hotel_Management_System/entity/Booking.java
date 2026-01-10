package com.example.Hotel_Management_System.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "bookings")
public class Booking {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Check in date is required")
    private LocalDate checkInDate;

    @Future(message = "Check out date must be in the future")
    private LocalDate checkOutDate;

    @Min(value = 1 ,message = "Number of adults not be less than 1")
    private int numOfAdults;

    @Min(value = 0 , message = "Number of children must be less than 0")
    private int numOfChildren;
    private int totalNumOfGuest;
    private String bookingConfirmationCode;


    @ManyToMany(fetch = FetchType.EAGER) //Related entity (User) is loaded immediately when you load a Booking so we use FetchType.EAGER (Immediate Loading)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY) //Related entity (Room) is NOT loaded immediately ,FetchType.LAZY (On-Demand Loading)
    @JoinColumn(name = "room_id")
    private Room room;


    public void calculateTotalNumberOfGuest(){
        this.totalNumOfGuest = this.numOfAdults + this.numOfChildren;
    }

    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
        calculateTotalNumberOfGuest();
    }

    public void setNumOfChildren(int numOfChildren) {
        this.numOfChildren = numOfChildren;
        calculateTotalNumberOfGuest();
    }
}
