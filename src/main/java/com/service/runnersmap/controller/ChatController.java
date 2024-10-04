package com.service.runnersmap.controller;

import com.service.runnersmap.ChatService;
import com.service.runnersmap.dto.ChatMessageDto;
import com.service.runnersmap.entity.ChatMessage;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

  private static final Logger log = LoggerFactory.getLogger(ChatController.class);
  private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달
  private final ChatService chatService;

  // 사용자가 채팅방에 들어갈 때
  // 특정 채팅방에 들어가야 하니 {chatRoomID} 같은 게 더 필요하겠죠..?
  @MessageMapping(value = "/enter")
  public void enter(ChatMessageDto message) {
    message.setMessage(message.getSender() + "님이 채팅방에 참여하였습니다.");
    template.convertAndSend("/sub/chat/room/" + message.getChatRoomId(), message);
  }

  // 메시지 전송
  @MessageMapping(value = "/message")
  public void sendMessage(ChatMessageDto message) {
     try {
       chatService.saveMessage(message);
       // 클라이언트에세 메시지 전송
       template.convertAndSend("/sub/chat/room/" + message.getChatRoomId(), message);
     } catch (RuntimeException e) {
       log.error("메시지 전송 중 오류 발생 : {} ", e.getMessage());
     }

  }

  // 메시지 조회
  @GetMapping("/messages/{chatRoomId}")
  public ResponseEntity<List<ChatMessageDto>> getMessages(@PathVariable Long chatRoomId) {

    List<ChatMessage> messages = chatService.getMessages(chatRoomId);
    if (messages.isEmpty()) {
      return ResponseEntity.notFound().build(); // 메시지가 없는 경우 404 반환
    }

    List<ChatMessageDto> messageDTOs = messages.stream()
        .map(message -> ChatMessageDto.builder()
            .chatRoomId(chatRoomId)
            .sender(message.getSender())
            .message(message.getMessage())
            .sentAt(message.getSentAt())
            .build())
        .collect(Collectors.toList());

    return ResponseEntity.ok(messageDTOs); // 메시지 반환
  }
}