package com.service.runnersmap.dto;

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
  private String sender;
  private String message;
  private LocalDateTime sentAt; // 메시지 전송시간

}
