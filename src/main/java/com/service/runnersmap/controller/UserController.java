package com.service.runnersmap.controller;

import com.service.runnersmap.dto.TokenResponse;
import com.service.runnersmap.dto.UserDto.AccountDeleteDto;
import com.service.runnersmap.dto.UserDto.LoginDto;
import com.service.runnersmap.dto.UserDto.SignUpDto;
import com.service.runnersmap.exception.custom.UnAuthorizedException;
import com.service.runnersmap.exception.custom.UserNotFoundException;
import com.service.runnersmap.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<Void> signUp(@RequestBody SignUpDto signUpDto) {
    try {
      userService.signUp(signUpDto);
      return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad request
    }
  }

  // 로그인 API
  @PostMapping("/login")
  public ResponseEntity<TokenResponse> login(@RequestBody LoginDto loginDto) {
    try {
      TokenResponse tokenResponse = userService.login(loginDto); // 200 OK
      return ResponseEntity.ok(tokenResponse);
    } catch (UnAuthorizedException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
    }

  }

  // 로그아웃 API
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails userDetails) {
    try {
      String email = userDetails.getUsername();
      userService.logout(email);
      return ResponseEntity.ok().build(); // 200 OK
    } catch (UserNotFoundException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request
    }

  }

  // 회원탈퇴 API
  @DeleteMapping("/account")
  public ResponseEntity<Void> deleteAccount(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestBody AccountDeleteDto accountDeleteDto) {
    try {
      String email = userDetails.getUsername();
      userService.deleteAccount(email, accountDeleteDto);
      return ResponseEntity.noContent().build(); // 204 No Content
    } catch (UserNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
    } catch (UnAuthorizedException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
    }

  }

}
