package com.example.rideapp.repositories;

import com.example.rideapp.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRoomId(Long roomId);
}
