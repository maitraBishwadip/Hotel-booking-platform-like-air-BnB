package com.SpringBootProject.AirBnB.controllers;

import com.SpringBootProject.AirBnB.dto.LoginDto;
import com.SpringBootProject.AirBnB.dto.LoginResponseDto;
import com.SpringBootProject.AirBnB.dto.SignUpRequestDto;
import com.SpringBootProject.AirBnB.dto.UserDto;
import com.SpringBootProject.AirBnB.security.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")

        public ResponseEntity<UserDto> signup(@RequestBody SignUpRequestDto signUpRequestDto) {

            return  new ResponseEntity<>(authService.signUp(signUpRequestDto), HttpStatus.CREATED);
        }

    @PostMapping("/login")
        public ResponseEntity<String[]> login (@RequestBody LoginDto LoginDto, HttpServletRequest
        request, HttpServletResponse response){

            String[] tokens = authService.login(LoginDto);

            Session.Cookie cookie = new Session.Cookie("refreshToken", tokens[1]);
            cookie.setHttpOnly(true);

            httpServletResponse.addCookie(cookie);
            return ResponseEntity.ok(new LoginResponseDto(tokens[0]));




        }

    }



