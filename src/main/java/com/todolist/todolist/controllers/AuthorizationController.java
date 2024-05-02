package com.todolist.todolist.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.todolist.todolist.domain.user.AuthorizationDto;
import com.todolist.todolist.domain.user.LoginResponseDto;
import com.todolist.todolist.domain.user.User;
import com.todolist.todolist.domain.user.UserRegisterDto;
import com.todolist.todolist.infra.security.TokenService;
import com.todolist.todolist.repositories.UserRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("auth")
public class AuthorizationController {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TokenService tokenService;

  @SuppressWarnings("rawtypes")
  @PostMapping("/login")
  @CrossOrigin(origins = "http://localhost:5173")
  public ResponseEntity login(@RequestBody @Valid AuthorizationDto data) {
    var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
    var auth = this.authenticationManager.authenticate(usernamePassword);

    var token = tokenService.generateToken((User) auth.getPrincipal());

    return ResponseEntity.ok(new LoginResponseDto(token));
  }

  @SuppressWarnings("rawtypes")
  @PostMapping("/register")
  public ResponseEntity register(@RequestBody @Valid UserRegisterDto data) {
    if (this.userRepository.findByLogin(data.login()) != null) {
      return ResponseEntity.badRequest().build();
    }

    String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
    User newUser = new User(data.login(), encryptedPassword, data.name(), data.surname());
    this.userRepository.save(newUser);
    return ResponseEntity.ok().build();
  }

}
