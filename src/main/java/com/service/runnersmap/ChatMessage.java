package com.service.runnersmap;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long ChatMessageId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chat_room_id")
  private ChatRoom chatRoom;

  // Private User user;

  private String message;

  // 보낸 시간까지 표시할까요?
  // private Timestamp timestamp;

}
