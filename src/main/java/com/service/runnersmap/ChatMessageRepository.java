package com.service.runnersmap;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

  List<ChatMessage> findByChatRoomId(Long chatRoomId);

}
