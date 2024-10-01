package com.service.runnersmap.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker   // stomp 사용을 위해 선언
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  // WebSocketConfigurer : 1대1 채팅방
  // WebSocketMessageBrokerConfigurer : 단톡방 - 메시징 시스템 필요

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws-stomp")  // 클라이언트가 WebSocket 연결을 위해 사용할 STOMP 엔드포인트(URL)
        .setAllowedOriginPatterns("*")  // 일단은 모든 출처에서 연결 허용 (추후 수정 해야 할 듯)
        .withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/sub");  // (서버 -> 클라이언트) 메시지를 전송할 경로
    registry.setApplicationDestinationPrefixes("/pub");    // (클라이언트 -> 서버) 메시지를 전송할 경로
  }


}
