
package com.example.rideapp.controllers;

import com.example.rideapp.models.ChatRoom;
import com.example.rideapp.models.Message;
import com.example.rideapp.services.ChatService;
import com.example.rideapp.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageService messageService;

    // 1. هذا يجب أن يكون الأول
    @PostMapping("/room/create")
    public ChatRoom createChatRoom(@RequestParam Long rideId) {
        return chatService.createRoom(rideId);
    }

    @PostMapping("/message/send")
    public Message sendMessage(@RequestBody Message message) {
        return messageService.save(message);
    }

    @GetMapping("/room/{rideId}/messages")
    public List<Message> getMessagesByRide(@PathVariable Long rideId) {
        ChatRoom room = chatService.getRoomByRideId(rideId);
        if (room != null) {
            return messageService.getMessages(room.getId());
        }
        return List.of();
    }

    @GetMapping("/room/{rideId}")
    public ChatRoom getRoomByRide(@PathVariable Long rideId) {
        ChatRoom room = chatService.getRoomByRideId(rideId);
        if (room == null) {
            // إذا ما فيه غرفة، أنشئ واحدة تلقائياً
            room = chatService.createRoom(rideId);
        }
        return room;
    }

    // إضافة للاختبار
    @GetMapping("/message/send")
    public String testMessage() {
        return "✅ Chat API is working! Use POST to send messages.";
    }
}