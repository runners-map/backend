package com.service.runnersmap.controller;

import com.service.runnersmap.dto.LoginResponse;
import com.service.runnersmap.dto.UserDto.AccountDeleteDto;
import com.service.runnersmap.dto.UserDto.AccountInfoDto;
import com.service.runnersmap.dto.UserDto.AccountUpdateDto;
import com.service.runnersmap.dto.UserDto.LoginDto;
import com.service.runnersmap.dto.UserDto.SignUpDto;
import com.service.runnersmap.entity.RefreshToken;
import com.service.runnersmap.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  // 회원가입 API
  @PostMapping("/sign-up")
  public ResponseEntity<Void> signUp(@RequestBody SignUpDto signUpDto) {
    userService.signUp(signUpDto);
    return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
  }


  // 로그인 API
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginDto) {
    LoginResponse tokenResponse = userService.login(loginDto);
    return ResponseEntity.ok(tokenResponse); // 200 OK
  }


  // 로그아웃 API
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails userDetails) {

    String email = userDetails.getUsername();
    userService.logout(email);
    return ResponseEntity.ok().build(); // 200 OK
  }


  // 회원탈퇴 API
  @DeleteMapping("/my-page")
  public ResponseEntity<Void> deleteAccount(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestBody AccountDeleteDto accountDeleteDto) {

    String email = userDetails.getUsername();
    userService.deleteAccount(email, accountDeleteDto);
    return ResponseEntity.noContent().build(); // 204 No Content
  }


  // 회원정보 조회 API
  @GetMapping("/my-page")
  public ResponseEntity<AccountInfoDto> getUserInfo(
      @AuthenticationPrincipal UserDetails userDetails) {

    String email = userDetails.getUsername();
    AccountInfoDto accountInfoDto = userService.getAccountInfo(email);
    return ResponseEntity.ok(accountInfoDto); // 200 OK
  }


  // 회원정보 수정 API
  @PatchMapping("/my-page")
  public ResponseEntity<Void> updateAccount(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestBody AccountUpdateDto accountUpdateDto) {

    String email = userDetails.getUsername();
    userService.updateAccount(email, accountUpdateDto);
    return ResponseEntity.ok().build(); // 200 OK

  }


  // 리프레시 토큰으로 엑세스 토큰 갱신
  @PostMapping("/refresh")
  public ResponseEntity<LoginResponse> refreshToken(@RequestBody LoginResponse refreshToken) {

    LoginResponse newTokenResponse = userService.refreshAccessToken(refreshToken.getRefreshToken());
    return ResponseEntity.ok(newTokenResponse);

  }
}