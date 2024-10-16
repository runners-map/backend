package com.service.runnersmap.service;

import com.service.runnersmap.component.JwtTokenProvider;
import com.service.runnersmap.dto.TokenResponse;
import com.service.runnersmap.dto.UserDto.AccountDeleteDto;
import com.service.runnersmap.dto.UserDto.AccountInfoDto;
import com.service.runnersmap.dto.UserDto.AccountUpdateDto;
import com.service.runnersmap.dto.UserDto.LoginDto;
import com.service.runnersmap.dto.UserDto.SignUpDto;
import com.service.runnersmap.entity.FileStorage;
import com.service.runnersmap.entity.RefreshToken;
import com.service.runnersmap.entity.User;
import com.service.runnersmap.exception.RunnersMapException;
import com.service.runnersmap.repository.RefreshTokenRepository;
import com.service.runnersmap.repository.UserRepository;
import com.service.runnersmap.type.ErrorCode;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenRepository refreshTokenRepository;
  private final FileStorageService fileStorageService;

  /**
   * 회원가입 이메일, 비밀번호, 닉네임, 성별 입력
   */
  public void signUp(SignUpDto signUpDto) {

    log.info("회원가입 시도: {} ", signUpDto.getEmail());
    // 중복 회원가입 불가
    if (userRepository.findByEmail(signUpDto.getEmail()).isPresent()) {
      throw new RunnersMapException(ErrorCode.ALREADY_EXISTS_USER);
    }
    // 비밀번호 확인 로직 추가
    if (!signUpDto.getPassword().equals(signUpDto.getConfirmPassword())) {
      throw new RunnersMapException(ErrorCode.NOT_VALID_PASSWORD);
    }
    // 동일 닉네임 사용 불가
    if (userRepository.findByNickname(signUpDto.getNickname()).isPresent()) {
      throw new RunnersMapException(ErrorCode.ALREADY_EXISTS_NICKNAME);
    }

    User user = User.builder()
        .email(signUpDto.getEmail())
        .password(passwordEncoder.encode(signUpDto.getPassword()))
        .nickname(signUpDto.getNickname())
        .gender(signUpDto.getGender())
        .createdAt(LocalDateTime.now())
        .build();
    userRepository.save(user);
    log.info("회원가입 완료");
  }

  /**
   * 로그인 이메일, 비밀번호 입력
   */
  public TokenResponse login(LoginDto loginDto) {
    log.info("로그인 요청: {} ", loginDto.getEmail())
    ;
    User user = userRepository.findByEmail(loginDto.getEmail())
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_USER));

    if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
      throw new RunnersMapException(ErrorCode.NOT_VALID_PASSWORD);
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

    log.info("로그인 성공");
    return new TokenResponse(accessToken, refreshToken);
  }

  /**
   * 회원탈퇴 로그인 된 상태에서 비밀번호 재입력 후 계정삭제
   */
  @Transactional
  public void deleteAccount(String email, AccountDeleteDto accountDeleteDto) {
    log.info("회원탈퇴 요청: {}", email);

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_USER));

    // 입력한 비밀번호와 저장된 비밀번호가 일치하는지 확인
    if (!passwordEncoder.matches(accountDeleteDto.getPassword(), user.getPassword())) {
      throw new RunnersMapException(ErrorCode.NOT_VALID_PASSWORD);
    }

    // 리프레시 토큰 삭제
    refreshTokenRepository.deleteByUser(user);

    // 사용자 삭제
    userRepository.delete(user);
    log.info("회원탈퇴 완료");
  }

  /**
   * 로그아웃
   */
  @Transactional
  public void logout(String email) {
    log.info("로그아웃 요청: {} ", email);
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_USER));
    // 리프레시 토큰 삭제
    refreshTokenRepository.deleteByUser(user);
    // SecurityContext 초기화
    SecurityContextHolder.clearContext();
    log.info("로그아웃 완료");
  }

  /**
   * 회원정보 조회
   */
  public AccountInfoDto getAccountInfo(String email) {
    log.info("회원정보 조회 요청: {}", email);
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_USER));

    String profileImageUrl = "";
    if (user.getProfileImage() != null) {
      profileImageUrl = user.getProfileImage().getStoredFileName();
      // profileImageUrl = "https://" + 버킷이름 + ".s3." + " 리전" + ".amazonaws.com/" + user.getProfileImage().getStoredFileName();
    }

    return AccountInfoDto.builder()
        .nickname(user.getNickname())
        .email(user.getEmail())
        .gender(user.getGender())
        .profileImage(profileImageUrl) // 만약 등록된 프사가 없는 경우, 빈 문자열 반환
        .build();
  }

  /**
   * 회원정보 수정 (닉네임, 비밀번호)
   */
  @Transactional
  public void updateAccount(String email, AccountUpdateDto accountUpdateDto) {

    log.info("회원정보 수정 요청 : {}", email);

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_USER));

    // 닉네임 수정(중복 방지)
    if (accountUpdateDto.getNewNickname() != null && !accountUpdateDto.getNewNickname().isEmpty()) {
      if (userRepository.findByNickname(accountUpdateDto.getNewNickname()).isPresent()) {
        throw new RunnersMapException(ErrorCode.ALREADY_EXISTS_NICKNAME);
      }
      user.setNickname(accountUpdateDto.getNewNickname());
    }

    // 비밀번호 수정
    if (accountUpdateDto.getNewPassword() != null && !accountUpdateDto.getNewPassword().isEmpty()) {
      if (!accountUpdateDto.getNewPassword().equals(accountUpdateDto.getNewConfirmPassword())) {
        throw new RunnersMapException(ErrorCode.NOT_VALID_PASSWORD);
      }
      user.setPassword(passwordEncoder.encode(accountUpdateDto.getNewPassword()));
    }

    user.setUpdatedAt(LocalDateTime.now());
    userRepository.save(user);
    log.info("회원정보 수정 완료");
  }

  /**
   * 프로필 등록/수정
   */
  @Transactional
  public void updateProfileImage(String email, MultipartFile file) throws IOException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_USER));

    // 프로필사진 업로드
    FileStorage uploadedFile = fileStorageService.uploadProfileImage(file, user);

    // 기존 프사가 있는 경우, 삭제 후 새 이미지로 대체
    user.setProfileImage(uploadedFile);
    user.setUpdatedAt(LocalDateTime.now());
    userRepository.save(user);
  }
}
