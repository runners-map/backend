package com.service.runnersmap.auth.handler;

import com.nimbusds.oauth2.sdk.token.RefreshToken;
import com.service.runnersmap.auth.dto.GoogleUserInfo;
import com.service.runnersmap.auth.dto.KakaoUserInfo;
import com.service.runnersmap.auth.dto.OAuthUserInfo;
import com.service.runnersmap.token.repository.RefreshTokenRepository;
import com.service.runnersmap.user.User;
import com.service.runnersmap.user.repository.UserRepository;
import com.service.runnersmap.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  @Value("${jwt.redirect}")
  private String REDIRECT_URI;

  @Value("${jwt.access-token.expiration-time}")
  private long ACCESS_TOKEN_EXPIRATION_TIME;

  @Value("${jwt.refresh-token.expiration-time}")
  private long REFRESH_TOKEN_EXPIRATION_TIME;

  private OAuthUserInfo oAuthUserInfo = null;

  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication; // 토큰
    final String provider = token.getAuthorizedClientRegistrationId(); // provider 추출

    // 구글 || 카카오 로그인 요청
    switch (provider) {
      case "google" -> {
        log.info("구글 로그인 요청");
        oAuthUserInfo = new GoogleUserInfo(token.getPrincipal().getAttributes());
      }
      case "kakao" -> {
        log.info("카카오 로그인 요청");
        oAuthUserInfo = new KakaoUserInfo(token.getPrincipal().getAttributes());
      }
    }

    // 정보 추출
    String providerId = oAuthUserInfo.getProviderId();
    String name = oAuthUserInfo.getName();

    User existUser = userRepository.findByProviderId(providerId);
    User user;

    if (existUser == null) {
      log.info("신규 사용자입니다. 등록을 진행합니다.");

      user = User.builder()
          .userId(UUID.randomUUID())
          .name(name)
          .provider(provider)
          .providerId(providerId)
          .build();
      userRepository.save(user);
    } else {
      log.info("기존 사용자입니다.");
      refreshTokenRepository.deleteByUserId(existUser.getUserId());
      user = existUser;
    }

    log.info("사용자 이름 : {}", name);
    log.info("PROVIDER : {}", provider);
    log.info("PROVIDER_ID : {}", providerId);

    // 리프레쉬 토큰 발급 후 저장
    String refreshToken = jwtUtil.generateRefreshToken(user.getUserId(), REFRESH_TOKEN_EXPIRATION_TIME);

    RefreshToken newRefreshToken = RefreshToken.builder()
        .userId(user.getUserId())
        .token(refreshToken)
        .build();
    refreshTokenRepository.save(newRefreshToken);

    // 액세스 토큰 발급
    String accessToken = jwtUtil.generateAccessToken(user.getUserId(), ACCESS_TOKEN_EXPIRATION_TIME);

    String encodedName = URLEncoder.encode(name, "UTF-8");
    String redirectUri = String.format(REDIRECT_URI, encodedName, accessToken, refreshToken);
    getRedirectStrategy().sendRedirect(request, response, redirectUri);
  }
}