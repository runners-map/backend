package com.service.runnersmap.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.service.runnersmap.component.JwtTokenProvider;
import com.service.runnersmap.dto.LoginResponse;
import com.service.runnersmap.dto.UserDto.LoginDto;
import com.service.runnersmap.dto.UserDto.SignUpDto;
import com.service.runnersmap.entity.RefreshToken;
import com.service.runnersmap.entity.User;
import com.service.runnersmap.exception.RunnersMapException;
import com.service.runnersmap.repository.RefreshTokenRepository;
import com.service.runnersmap.repository.UserRepository;
import com.service.runnersmap.type.ErrorCode;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserServiceTest {

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @MockBean
  private JwtTokenProvider jwtTokenProvider;

  @MockBean
  private RefreshTokenRepository refreshTokenRepository;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    userService = new UserService(userRepository, passwordEncoder, jwtTokenProvider,
        refreshTokenRepository, null);
  }

  @Test
  public void 회원가입_성공() {
    SignUpDto signUpDto = new SignUpDto("test@mail.com", "qwerty1!", "qwerty1!", "nickname",
        "M");

    // 중복된 이메일이나 닉네임이 없도록 설정
    when(userRepository.findByEmail(signUpDto.getEmail())).thenReturn(Optional.empty());
    when(userRepository.findByNickname(signUpDto.getNickname())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(signUpDto.getPassword())).thenReturn("encodedPassword");

    userService.signUp(signUpDto);

    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  void 회원가입_비밀번호_확인_실패() {
    SignUpDto signUpDto = new SignUpDto("test@mail.com", "password123!", "qwerty123!",
        "nickname", "M");

    RunnersMapException exception = assertThrows(RunnersMapException.class,
        () -> userService.signUp(signUpDto));

    assertEquals(ErrorCode.NOT_VALID_PASSWORD, exception.getErrorCode());
  }

  @Test
  void 회원가입_비밀번호_유효성검사_실패() {
    SignUpDto signUpDto = new SignUpDto("test@mail.com", "simple", "simple", "nickname", "M");

    when(userRepository.findByEmail(signUpDto.getEmail())).thenReturn(Optional.empty());
    when(userRepository.findByNickname(signUpDto.getNickname())).thenReturn(Optional.empty());

    RunnersMapException exception = assertThrows(RunnersMapException.class,
        () -> userService.signUp(signUpDto));

    assertEquals(ErrorCode.NEED_MORE_PW_CONDITION, exception.getErrorCode());
  }

  @Test
  public void 회원가입_중복이메일_실패() {
    SignUpDto signUpDto = new SignUpDto("test@mail.com", "qwerty1!", "qwerty1!", "nickname1",
        "M");

    // 중복된 이메일 설정
    when(userRepository.findByEmail(signUpDto.getEmail())).thenReturn(Optional.of(new User()));
    RunnersMapException exception = assertThrows(RunnersMapException.class,
        () -> userService.signUp(signUpDto));

    assertEquals(ErrorCode.ALREADY_EXISTS_USER, exception.getErrorCode());
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  public void 회원가입_중복닉네임_실패() {
    SignUpDto signUpDto = new SignUpDto("test1@mail.com", "qwerty1!", "qwerty1!", "nickname",
        "M");

    // 중복된 닉네임 설정
    when(userRepository.findByNickname(signUpDto.getNickname())).thenReturn(
        Optional.of(new User()));
    RunnersMapException exception = assertThrows(RunnersMapException.class,
        () -> userService.signUp(signUpDto));

    assertEquals(ErrorCode.ALREADY_EXISTS_NICKNAME, exception.getErrorCode());
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  public void 로그인_성공() {
    LoginDto loginDto = new LoginDto("test@mail.com", "password");
    User user = User.builder().email("test@mail.com").password("encodedPassword").build();

    when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(true);
    when(jwtTokenProvider.generateAccessToken(user.getEmail())).thenReturn("accessToken");
    when(jwtTokenProvider.generateRefreshToken(user.getEmail())).thenReturn("refreshToken");
    when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.empty());

    LoginResponse response = userService.login(loginDto);

    assertNotNull(response);
    assertEquals("accessToken", response.getAccessToken());
    assertEquals("refreshToken", response.getRefreshToken());
    verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
  }

  @Test
  public void 로그인_존재하지않는회원_실패() {
    LoginDto loginDto = new LoginDto("test@mail.com", "qwerty1!");

    when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.empty());
    RunnersMapException exception = assertThrows(RunnersMapException.class,
        () -> userService.login(loginDto));

    assertEquals(ErrorCode.NOT_FOUND_USER, exception.getErrorCode());
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  public void 로그인_비밀번호불일치_실패() {
    LoginDto loginDto = new LoginDto("test@mail.com", "qwerty1!");
    User user = User.builder().email("test@mail.com").password("encodedPassword").build();

    when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(false);
    RunnersMapException exception = assertThrows(RunnersMapException.class,
        () -> userService.login(loginDto));

    assertEquals(ErrorCode.NOT_VALID_PASSWORD, exception.getErrorCode());
    verify(refreshTokenRepository, never()).save(any(RefreshToken.class));
  }

}