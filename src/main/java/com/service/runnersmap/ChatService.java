package com.service.runnersmap;

import com.service.runnersmap.dto.ChatMessageDto;
import com.service.runnersmap.entity.ChatMessage;
import com.service.runnersmap.entity.ChatRoom;
import com.service.runnersmap.repository.ChatMessageRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

  private final ChatMessageRepository chatMessageRepository;;

  // 메시지 저장
  public void saveMessage(ChatMessageDto chatMessageDTO) {
    ChatMessage message = new ChatMessage();
    message.setSender(chatMessageDTO.getSender());
    message.setMessage(chatMessageDTO.getMessage());
    message.setSentAt(LocalDateTime.now());

    ChatRoom chatRoom = new ChatRoom(); // 여기서 적절한 방법으로 채팅방을 조회해야 합니다.
    chatRoom.setId(chatMessageDTO.getChatRoomId());
    message.setChatRoom(chatRoom);

    chatMessageRepository.save(message);
  }


  // 메시지 조회
  public List<ChatMessage> getMessages(Long chatRoomId) {
    return chatMessageRepository.findByChatRoomId(chatRoomId);
  }

}
