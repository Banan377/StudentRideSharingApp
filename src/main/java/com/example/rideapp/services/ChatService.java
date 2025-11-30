
package com.example.rideapp.services;

import com.example.rideapp.models.ChatRoom;
import com.example.rideapp.repositories.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public ChatRoom createRoom(Long rideId) {
        ChatRoom room = new ChatRoom(rideId);
        return chatRoomRepository.save(room);
    }

    public ChatRoom getRoomByRideId(Long rideId) {
        return chatRoomRepository.findByRideId(rideId);
    }
}
