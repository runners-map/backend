package com.service.runnersmap;

import com.service.runnersmap.dto.ChatMessageDto;
import com.service.runnersmap.entity.ChatMessage;
import com.service.runnersmap.entity.ChatRoom;
import com.service.runnersmap.entity.User;
import com.service.runnersmap.repository.ChatMessageRepository;
import com.service.runnersmap.repository.ChatRoomRepository;
import com.service.runnersmap.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

  private final ChatMessageRepository chatMessageRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final UserRepository userRepository;


  // 메시지 저장
  public void saveMessage(ChatMessageDto chatMessageDTO) {

    // 채팅방 조회
    ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDTO.getChatRoomId())
        .orElseThrow(() -> new RuntimeException("존재하지 않는 채팅방입니다."));

    User sender = userRepository.findById(chatMessageDTO.getSenderId())
        .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

    ChatMessage message = new ChatMessage();

    // 로그인 한 상태면 작성하지 않아도 될까요?
    // STOMP의 경우 헤드에 인증 관련한 걸 넣을 수 있다고 해서 좀 더 알아보겠습니다.
    message.setSender(sender);

    message.setMessage(chatMessageDTO.getMessage());
    message.setSentAt(LocalDateTime.now());
    message.setChatRoom(chatRoom);

    chatMessageRepository.save(message);
  }


  // 메시지 조회
  public List<ChatMessage> getMessages(Long chatRoomId) {
    return chatMessageRepository.findByChatRoomId(chatRoomId);
  }
  
}
