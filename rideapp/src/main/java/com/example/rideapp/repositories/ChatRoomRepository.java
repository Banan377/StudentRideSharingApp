
package com.example.rideapp.repositories;

import com.example.rideapp.models.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    ChatRoom findByRideId(Long rideId);
}
