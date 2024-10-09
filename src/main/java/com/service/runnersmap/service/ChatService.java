package com.service.runnersmap.service;

import com.service.runnersmap.UserRepository;
import com.service.runnersmap.dto.ChatMessageDto;
import com.service.runnersmap.entity.ChatMessage;
import com.service.runnersmap.entity.ChatRoom;
import com.service.runnersmap.entity.User;
import com.service.runnersmap.repository.ChatMessageRepository;
import com.service.runnersmap.repository.ChatRoomRepository;
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


  /**
   * 사용자가 채팅방에 들어왔을 때 입장 알림 메시지 전송
   */
  public void handleUserEnter(ChatMessageDto chatMessageDto) {

    User sender = userRepository.findById(chatMessageDto.getSenderId())
        .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

    // UserPost 테이블에서 사용자의 참여상태 확인
    // boolean isExiting = !userPostRepository.existsByUserIdAndPostId(sender.getId(), chatMessageDto.getChatRoomId());
    //
//    if (isExisting) {
//      String enterMessage = sender.getNickname() + "님이 채팅방에 입장하셨습니다.";
//      chatMessageDto = ChatMessageDto.builder()
//        .chatRoomId(chatMessageDto.getChatRoomId())
//        .senderId(sender.getId())
//        .senderNickname(sender.getNickname())
//        .message(enterMessage)
//        .build();
//    }

// UserPost에 참여기록 추가
//    UserPost userPost = UserPost.builder()
//        .user(sender)
//        .post(chatRoom.getPost())
//        .build();
//    userPostRepository.save(userPost);

    // 브로커로 메시지 전송
    template.convertAndSend("/sub/chat/room/" + chatMessageDto.getChatRoomId(), chatMessageDto);
  }


  /**
   * 퇴장시 퇴장 알림 메시지 전송 메서드
   */
  public void handleUserExit(ChatMessageDto chatMessageDto) {

    User sender = userRepository.findById(chatMessageDto.getSenderId())
        .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

    // UserPost에서 사용자 삭제
    //userPostRepository.deleteByUserIdAndPostId(sender.getId(), chatMessageDto.getChatRoomId());

    String exitMessage = sender.getNickname() + "님이 채팅방을 나갔습니다.";
    chatMessageDto = ChatMessageDto.builder()
        .chatRoomId(chatMessageDto.getChatRoomId())
        .senderId(sender.getId())
        .senderNickname(sender.getNickname())
        .message(exitMessage)
        .build();

    template.convertAndSend("/sub/chat/room/" + chatMessageDto.getChatRoomId(), chatMessageDto);
  }


  /**
   * 메시지 저장 & 전송하는 메서드
   */
  public void saveAndBroadcastMessage(ChatMessageDto chatMessageDto) {

    ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getChatRoomId())
        .orElseThrow(() -> new RuntimeException("존재하지 않는 채팅방입니다."));

    User sender = userRepository.findById(chatMessageDto.getSenderId())
        .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

    // 데이터베이스에 저장될 메시지
    ChatMessage message = ChatMessage.builder()
        .chatRoom(chatRoom)
        .sender(sender)
        .message(chatMessageDto.getMessage())
        .sentAt(LocalDateTime.now())
        .build();
    chatMessageRepository.save(message);

    // 클라이언트에게 전달할 메시지
    ChatMessageDto responseDto = ChatMessageDto.builder()
        .chatRoomId(chatRoom.getId())
        .senderId(sender.getId())
        .senderNickname(sender.getNickname())
        .message(message.getMessage())
        .sentAt(message.getSentAt())
        .build();
    template.convertAndSend("/sub/chat/room/" + chatMessageDto.getChatRoomId(), responseDto);
  }


  /**
   * 메시지를 조회하는 메서드
   */
  public List<ChatMessageDto> getMessages(Long chatRoomId) {

    List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(chatRoomId);

    return messages.stream()
        .map(message -> ChatMessageDto.builder()
            .chatRoomId(message.getChatRoom().getId())
            .senderId(message.getSender().getId())
            .senderNickname(message.getSender().getNickname())
            .message(message.getMessage())
            .sentAt(message.getSentAt())
            .build())
        .collect(Collectors.toList());
  }
}
