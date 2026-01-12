package com.example.Hotel_Management_System.dto;

import com.example.Hotel_Management_System.entity.Booking;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)  //“When converting this object to JSON, exclude any fields that are null.”
public class RoomDto {
    private long id;
    private String roomType;
    private BigDecimal roomPrice;
    private String roomPhotoUrl;
    private String roomDescription;
    private List<BookingDto> bookings = new ArrayList<>();
}

/**

 import com.fasterxml.jackson.annotation.JsonInclude;

 @JsonInclude(JsonInclude.Include.NON_NULL)
 public class User {
 private String name;
 private String email;
 private Integer age;

 // getters & setters

 }
 ------------------------------------------------------------

 User user = new User();
 user.setName("Alice");
 user.setEmail(null);
 user.setAge(25);

 JSON output (response)
 {
 "name": "Alice",
 "age": 25
 }


 The email field is not included because it is null.


 Without @JsonInclude(NON_NULL)

 The JSON would look like:

 {
 "name": "Alice",
 "email": null,
 "age": 25
 }


 * **/
