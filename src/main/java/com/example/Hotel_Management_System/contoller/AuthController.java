package com.example.Hotel_Management_System.contoller;


import com.example.Hotel_Management_System.dto.LoginRequest;
import com.example.Hotel_Management_System.dto.Response;
import com.example.Hotel_Management_System.entity.User;
import com.example.Hotel_Management_System.service.interfac.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody User user){
        Response response = userService.register(user);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> register(@RequestBody LoginRequest loginRequest){
        Response response = userService.login(loginRequest);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
