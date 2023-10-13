package com.example.UserManagement.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.example.UserManagement.Dto.SignInResponseDTO;
import com.example.UserManagement.Dto.SignUpDTO;
import com.example.UserManagement.Dto.SignUpResponseDTO;
import com.example.UserManagement.Dto.UserDetailsDTO;

public interface UserService {
  public SignUpResponseDTO signUp (SignUpDTO signUpDTO) throws NoSuchAlgorithmException;
  public SignInResponseDTO signIn (String userEmail , String password) throws Exception;
  public List<UserDetailsDTO> userDetails ();
}
