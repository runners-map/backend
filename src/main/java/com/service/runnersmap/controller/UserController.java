package com.service.runnersmap.controller;

import com.service.runnersmap.dto.TokenResponse;
import com.service.runnersmap.dto.UserDto.AccountDeleteDto;
import com.service.runnersmap.dto.UserDto.LoginDto;
import com.service.runnersmap.dto.UserDto.SignUpDto;
import com.service.runnersmap.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  public ResponseEntity<String> signUp(@RequestBody SignUpDto signUpDto) {
    userService.signUp(signUpDto);
    return ResponseEntity.ok("회원가입 되었습니다.");
  }

  // 로그인 API
  @PostMapping("/login")
  public ResponseEntity<TokenResponse> login(@RequestBody LoginDto loginDto) {
    TokenResponse tokenResponse = userService.login(loginDto);
    return ResponseEntity.ok(tokenResponse);
  }

  // 회원탈퇴 API
  @DeleteMapping("/account")
  public ResponseEntity<String> deleteAccount(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestBody AccountDeleteDto accountDeleteDto) {

    String email = userDetails.getUsername();
    userService.deleteAccount(email, accountDeleteDto);
    return ResponseEntity.ok("계정이 삭제됐습니다.");
  }

}
