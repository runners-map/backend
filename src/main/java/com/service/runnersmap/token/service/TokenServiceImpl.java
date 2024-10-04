package com.service.runnersmap.token.service;

import com.service.runnersmap.token.RefreshToken;
import com.service.runnersmap.token.dto.TokenResponse;
import com.service.runnersmap.token.exception.TokenErrorResult;
import com.service.runnersmap.token.exception.TokenException;
import com.service.runnersmap.token.repository.RefreshTokenRepository;
import com.service.runnersmap.util.JwtUtil;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
  @Value("${jwt.access-token.expiration-time}")
  private long ACCESS_TOKEN_EXPIRATION_TIME;
  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtUtil jwtUtil;
  @Override
  public TokenResponse reissueAccessToken(String authorizationHeader) {
    String refreshToken = jwtUtil.getTokenFromHeader(authorizationHeader);
    String userId = jwtUtil.getUserIdFromToken(refreshToken);
    RefreshToken existRefreshToken = refreshTokenRepository.findByUserId(UUID.fromString(userId));
    String accessToken = null;
    if (!existRefreshToken.getToken().equals(refreshToken) || jwtUtil.isTokenExpired(refreshToken)) {
      // 리프레쉬 토큰이 다르거나, 만료된 경우(재로그인)
      refreshTokenRepository.delete(existRefreshToken);
      throw new TokenException(TokenErrorResult.INVALID_REFRESH_TOKEN);
    } else {
      // 액세스 토큰 재발급
      accessToken = jwtUtil.generateAccessToken(UUID.fromString(userId), ACCESS_TOKEN_EXPIRATION_TIME);
    }
    return TokenResponse.builder()
        .accessToken(accessToken)
        .build();
  }
}