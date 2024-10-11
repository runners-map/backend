package com.service.runnersmap.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

  // 비밀키
  @Value("${spring.jwt.secret}")
  private String secretKey;

  // 만료 시간
  private static final long TOKEN_EXPIRED_TIME = 1000 * 60 * 60; // 1시간
  private static final long REFRESH_TOKEN_EXPIRED_TIME = 1000 * 60 * 60 * 24 * 15; // 리프레시토큰 15일
  // 알고리즘
  private static final SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;

  // 엑세스 토큰(jwt) 생성
  public String generateAccessToken(String email) {
    String token = Jwts.builder()
        .setSubject(email)
        .setIssuedAt(new Date())  // 발행시간
        .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRED_TIME)) // 만료시간
        .signWith(algorithm, secretKey)
        .compact(); // 토큰을 문자열로 변환하여 반환
    log.info("생성된 Access Token : {}", token);
    return token;
  }

  // 토큰에서 정보 추출
  public Claims extractClaims(String token) {
    return Jwts.parser()
        .setSigningKey(secretKey) // 비밀키 설정
        .parseClaimsJws(token) // 토큰 파싱
        .getBody(); // Claims 객체 반환
  }

  // 토큰에서 이메일 추출
  public String extractUserEmail(String token) {
    return extractClaims(token).getSubject();
  }

  // 토큰 유효성 검사
  public boolean validateToken(String token, String email) {
    try {
      String extractedEmail = extractUserEmail(token); // 토큰에서 이메일 추출
      boolean isValid =
          extractedEmail.equals(email) && !isTokenExpired(token); // 이메일이 일치하고 토큰이 만료되지 않았는지 확인
      log.info("토큰 유효성 검사 - 사용자 : {}, 유효성 : {}", email, isValid);
      return isValid;
    } catch (ExpiredJwtException e) {
      log.warn("만료된 JWT 토큰" + e.getMessage());
      return false;
    } catch (RuntimeException e) {
      log.error("JWT 토큰 오류" + e.getMessage());
      return false;
    }
  }

  // 토큰 만료여부 확인
  public boolean isTokenExpired(String token) {
    Date expiredDate = extractClaims(token).getExpiration(); // 만료일차 추출
    return expiredDate.before(new Date()); // 현재보다 이전이면 만료
  }

  // 리프레시 토큰 생성
  public String generateRefreshToken(String email) {
    String refreshToken = Jwts.builder()
        .setSubject(email)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRED_TIME))
        .signWith(algorithm, secretKey)
        .compact();
    log.info("리프레시 토큰 : {}", refreshToken);
    return refreshToken;
  }
}