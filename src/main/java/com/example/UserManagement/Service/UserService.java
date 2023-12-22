package com.example.UserManagement.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.catalina.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.UserManagement.Dto.AuthResponse;
import com.example.UserManagement.Dto.AuthenticationRequest;
import com.example.UserManagement.Dto.AuthenticationResponse;
import com.example.UserManagement.Dto.SignInResponseDTO;
import com.example.UserManagement.Dto.SignUpDTO;
import com.example.UserManagement.Dto.SignUpResponseDTO;
import com.example.UserManagement.Dto.UserDetailsDTO;
import com.example.UserManagement.Entity.Users;

public interface UserService extends UserDetailsService {
  public AuthenticationResponse signUp (SignUpDTO signUpDTO) throws NoSuchAlgorithmException;
  public SignInResponseDTO signIn (String userEmail , String password) throws Exception;
  public List<UserDetailsDTO> userDetails ();
   SignInResponseDTO authenticate(String userEmail, String password)throws Exception;
//  Users findByEmail(String email);
    AuthResponse verifyTokens( String token);
}
