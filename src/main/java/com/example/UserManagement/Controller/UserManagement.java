package com.example.UserManagement.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.UserManagement.Dto.SignInResponseDTO;
import com.example.UserManagement.Dto.SignUpDTO;
import com.example.UserManagement.Dto.SignUpResponseDTO;
import com.example.UserManagement.Dto.UserDetailsDTO;
import com.example.UserManagement.Helper.UserModuleApiPath;
import com.example.UserManagement.Service.UserService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api("UserManagement API's")
@RequestMapping(UserModuleApiPath.BASE_PATH)
@RestController
public class UserManagement {

  @Autowired
  UserService userService;

  @Operation(description = "API ENDPOINT FOR SIGN-UP", summary = "SignUp Api for user to signUp in the Management System")
  @PostMapping(UserModuleApiPath.SIGN_UP)
  public ResponseEntity<SignUpResponseDTO> SignUp(@RequestBody SignUpDTO signupDto) throws Exception {
    log.warn("Invoking API for Sign-Up with UserName : {} and UserEmail : {}", signupDto.getFirstName(),
        signupDto.getEmail());
    return new ResponseEntity(userService.signUp(signupDto), HttpStatus.OK);
  }

  @Operation(description = "API ENDPOINT FOR SIGN-IN")
  @PostMapping(UserModuleApiPath.SIGN_IN)
  public ResponseEntity<SignInResponseDTO> SignIn(@RequestParam String userEmail, String password) throws Exception {
    log.warn("Invoking API for Sign-Up with UserEmail : {}", userEmail);
    return new ResponseEntity(userService.signIn(userEmail, password), HttpStatus.OK);
  }

  @Operation(description = "API ENDPOINT FOR LIST ALL USERS DETAILS")
  @GetMapping(UserModuleApiPath.DETAILS)
  public ResponseEntity<List<UserDetailsDTO>> details() throws Exception {
    log.warn("Invoking API for Fetching User Details");
    return new ResponseEntity(userService.userDetails(), HttpStatus.OK);
  }

}
