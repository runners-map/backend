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
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

  private final ChatMessageRepository chatMessageRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final UserRepository userRepository;
  private final SimpMessagingTemplate template;


  // 사용자가 채팅방에 들어왔을 때
  public void handleUserEnter(ChatMessageDto chatMessageDto) {
    chatMessageDto = ChatMessageDto.builder()
        .chatRoomId(chatMessageDto.getChatRoomId())
        .senderId(chatMessageDto.getSenderId())
        .message(chatMessageDto.getSenderId() + "님이 채팅방에 입장하셨습니다.")
        .build();

    // 브로커로 메시지 전송
    template.convertAndSend("/sub/chat/room/" + chatMessageDto.getChatRoomId(), chatMessageDto);
  }

  // 메시지 저장
  public void saveMessage(ChatMessageDto chatMessageDTO) {

    // 채팅방 조회
    ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDTO.getChatRoomId())
        .orElseThrow(() -> new RuntimeException("존재하지 않는 채팅방입니다."));

    User sender = userRepository.findById(chatMessageDTO.getSenderId())
        .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));


    ChatMessage message = ChatMessage.builder()
        .chatRoom(chatRoom)
        // 로그인 한 상태면 작성하지 않아도 될까요?
        // STOMP의 경우 헤드에 인증 관련한 걸 넣을 수 있다고 해서 좀 더 알아보겠습니다.
        .sender(sender)
        .message(chatMessageDTO.getMessage())
        .sentAt(LocalDateTime.now())
        .build();

    // 메시지 저장
    chatMessageRepository.save(message);

    template.convertAndSend("/sub/chat/room/" + chatMessageDTO.getChatRoomId(), chatMessageDTO);

  }


  // 메시지 조회
  public List<ChatMessageDto> getMessages(Long chatRoomId) {

    List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(chatRoomId);

    return messages.stream()
        .map(message -> ChatMessageDto.builder()
            .chatRoomId(message.getChatRoom().getId())
            .senderId(message.getSender().getId())
            .message(message.getMessage())
            .sentAt(message.getSentAt())
            .build())
        .collect(Collectors.toList());
  }
  
}
