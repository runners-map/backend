package com.service.runnersmap.service;

import com.service.runnersmap.component.JwtTokenProvider;
import com.service.runnersmap.dto.TokenResponse;
import com.service.runnersmap.dto.UserDto.AccountDeleteDto;
import com.service.runnersmap.dto.UserDto.LoginDto;
import com.service.runnersmap.dto.UserDto.SignUpDto;
import com.service.runnersmap.entity.RefreshToken;
import com.service.runnersmap.entity.User;
import com.service.runnersmap.exception.custom.UnAuthorizedException;
import com.service.runnersmap.exception.custom.UserNotFoundException;
import com.service.runnersmap.repository.RefreshTokenRepository;
import com.service.runnersmap.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenRepository refreshTokenRepository;

  /**
   * 회원가입 이메일, 비밀번호, 닉네임, 성별 입력
   */
  public void signUp(SignUpDto signUpDto) {

    // 중복 회원가입 불가
    if (userRepository.findByEmail(signUpDto.getEmail()).isPresent()) {
      throw new IllegalArgumentException("이미 회원가입 된 이메일입니다.");
    }
    // 비밀번호 확인 로직 추가
    if (!signUpDto.getPassword().equals(signUpDto.getConfirmPassword())) {
      throw new IllegalArgumentException("비밀번호를 다시 확인해주세요");
    }
    // 동일 닉네임 사용 불가
    if (userRepository.findByNickname(signUpDto.getNickname()).isPresent()) {
      throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
    }

    User user = User.builder()
        .email(signUpDto.getEmail())
        .password(passwordEncoder.encode(signUpDto.getPassword()))
        .nickname(signUpDto.getNickname())
        .gender(signUpDto.getGender())
        .createdAt(LocalDateTime.now())
        .build();
    userRepository.save(user);
  }

  /**
   * 로그인 이메일, 비밀번호 입력
   */
  public TokenResponse login(LoginDto loginDto) {
    User user = userRepository.findByEmail(loginDto.getEmail())
        .orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

    if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
      throw new UnAuthorizedException("비밀번호가 일치하지 않습니다.");
    }

    //토큰 생성
    String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail());
    String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

    // 리프레시 토큰 저장
    RefreshToken refreshTokenEntity = RefreshToken.builder()
        .token(refreshToken)
        .user(user)
        .build();
    refreshTokenRepository.save(refreshTokenEntity);

    return new TokenResponse(accessToken, refreshToken);
  }

  /**
   * 회원탈퇴 로그인 된 상태에서 비밀번호 재입력 후 계정삭제
   */
  @Transactional
  public void deleteAccount(String email, AccountDeleteDto accountDeleteDto) {

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

    // 입력한 비밀번호와 저장된 비밀번호가 일치하는지 확인
    if (!passwordEncoder.matches(accountDeleteDto.getPassword(), user.getPassword())) {
      throw new UnAuthorizedException("비밀번호가 일치하지 않습니다.");
    }

    // 리프레시 토큰 삭제
    refreshTokenRepository.deleteByUser(user);

    // 사용자 삭제
    userRepository.delete(user);
  }

  /**
   * 로그아웃
   */
  @Transactional
  public void logout(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
    // 리프레시 토큰 삭제
    refreshTokenRepository.deleteByUser(user);
  }
}
