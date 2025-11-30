
package com.example.rideapp.services;

import com.example.rideapp.models.Message;
import com.example.rideapp.repositories.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getMessages(Long roomId) {
        return messageRepository.findByRoomId(roomId);
    }
}
