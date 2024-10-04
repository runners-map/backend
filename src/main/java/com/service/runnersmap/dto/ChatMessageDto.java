package com.service.runnersmap.dto;

import com.service.runnersmap.entity.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {

  private Long chatRoomId;
  private Long senderId;
  private String message;
  private LocalDateTime sentAt; // 메시지 전송시간

}
