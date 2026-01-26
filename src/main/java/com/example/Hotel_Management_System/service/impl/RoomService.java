package com.example.Hotel_Management_System.service.impl;

import com.example.Hotel_Management_System.dto.Response;
import com.example.Hotel_Management_System.dto.RoomDto;
import com.example.Hotel_Management_System.entity.Room;
import com.example.Hotel_Management_System.repository.BookingRepository;
import com.example.Hotel_Management_System.repository.RoomRepository;
import com.example.Hotel_Management_System.service.AwsS3Service;
import com.example.Hotel_Management_System.service.interfac.IRoomService;
import com.example.Hotel_Management_System.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService implements IRoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AwsS3Service awsS3Service;


    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
         Response response = new Response();

         try{
             String imageUrl = awsS3Service.saveImageToS3(photo);
             Room room = new Room();
             room.setRoomPhotoUrl(imageUrl);
             room.setRoomType(roomType);
             room.setRoomPrice(roomPrice);
             room.setRoomDescription(description);
             Room savedRoom = roomRepository.save(room);
             RoomDto roomDto = Utils.mapRoomEntityToRoomDTO(savedRoom);
             response.setStatusCode(200);
             response.setMessage("Successful");
             response.setRoom(roomDto);

         }catch (Exception e){
             response.setStatusCode(500);
             response.setMessage("Error saving a room " + e.getMessage());
         }

        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Response getAllRooms() {
        Response response = new Response();

        try{
            List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
            List<RoomDto> roomDtoList = Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoomList(roomDtoList);

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }

        return response;
        
    }

    @Override
    public Response deleteRoom(Long roomId) {
        return null;
    }

    @Override
    public Response updateRoom(Long roomId, String roomType, BigDecimal roomPrice, MultipartFile photo) {
        return null;
    }

    @Override
    public Response getRoomById(Long roomId) {
        return null;
    }

    @Override
    public Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        return null;
    }

    @Override
    public Response getAllAvailableRooms() {
        return null;
    }
}
