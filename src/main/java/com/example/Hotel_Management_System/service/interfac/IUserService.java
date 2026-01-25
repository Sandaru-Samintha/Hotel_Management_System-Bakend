package com.example.Hotel_Management_System.service.interfac;

import com.example.Hotel_Management_System.dto.LoginRequest;
import com.example.Hotel_Management_System.dto.Response;
import com.example.Hotel_Management_System.entity.User;

public interface IUserService {

    Response register(User loginRequest);
    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String userId);

}
