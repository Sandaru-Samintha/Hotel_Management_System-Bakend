package com.example.Hotel_Management_System.service.impl;

import com.example.Hotel_Management_System.dto.LoginRequest;
import com.example.Hotel_Management_System.dto.Response;
import com.example.Hotel_Management_System.dto.UserDto;
import com.example.Hotel_Management_System.entity.User;
import com.example.Hotel_Management_System.exception.OurException;
import com.example.Hotel_Management_System.repository.UserRepository;
import com.example.Hotel_Management_System.service.interfac.IUserService;
import com.example.Hotel_Management_System.utils.JWTUtils;
import com.example.Hotel_Management_System.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

   @Autowired
   private UserRepository userRepository;


   @Autowired
   private PasswordEncoder passwordEncoder;

   @Autowired
   private JWTUtils jwtUtils;

   @Autowired
   private AuthenticationManager  authenticationManager;


    @Override
    public Response register(User user) {
        Response response = new Response();
        try {
            if(user.getRole() == null || user.getRole().isBlank()){
                user.setRole("USER");
            }
            if (userRepository.existsByEmail(user.getEmail())) {
                throw  new OurException(user.getEmail() + "Already Exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDto userDto = Utils.mapUserEntityToUserDTO(savedUser);
            response.setStatusCode(200);
            response.setUser(userDto);

        }catch (OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Registration" + e.getMessage());

        }

        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()-> new OurException("User Not Found"));

            var token = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("Successful");


        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occurred during user login" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllUsers() {

        Response response = new Response();
        try{
            List <User> userList = userRepository.findAll();
            List <UserDto> userDtoList = Utils.mapUserListEntityToUserListDTO(userList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUserList(userDtoList);

        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all users" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {

        Response response = new Response();

        try{
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(()-> new OurException("User Not Found"));
            UserDto userDto = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUser(userDto);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all users booking history" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(String userId) {

        Response response = new Response();

        try{
            userRepository.findById(Long.valueOf(userId)).orElseThrow(()-> new OurException("User Not Found"));
            userRepository.deleteById(Long.valueOf(userId));
            response.setStatusCode(200);
            response.setMessage("Successful");

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error  deleting user " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(String userId) {
        Response response = new Response();

        try{
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(()-> new OurException("User Not Found"));
            UserDto userDto = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUser(userDto);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting user " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response = new Response();

        try{
            User user = userRepository.findByEmail(email).orElseThrow(()-> new OurException("User Not Found"));
            UserDto userDto = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUser(userDto);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting user information " + e.getMessage());
        }
        return response;
    }
}
