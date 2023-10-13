package com.example.UserManagement.Service.ServiceImplementation;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.UserManagement.Dto.SignInResponseDTO;
import com.example.UserManagement.Dto.SignUpDTO;
import com.example.UserManagement.Dto.SignUpResponseDTO;
import com.example.UserManagement.Dto.UserDetailsDTO;
import com.example.UserManagement.Entity.Users;
import com.example.UserManagement.Repository.UserTableRepository;
import com.example.UserManagement.Service.UserService;

import javax.xml.bind.DatatypeConverter;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UserTableRepository userTableRepository;

  @Override
  public SignUpResponseDTO signUp(SignUpDTO signUpDTO) {
    SignUpResponseDTO signUpResponseDTO = new SignUpResponseDTO();

    // Check if the user with the given email already exists
    Users userAvailable = userTableRepository.findByEmail(signUpDTO.getEmail());

    if (userAvailable != null) {
      signUpResponseDTO.setFlag(false);
      signUpResponseDTO.setMessage("User Already Present with this email : " + " " + signUpDTO.getEmail());
    } else {
      try {
        Users users = new Users();

        // Copy properties from signUpDTO to users, excluding the password
        BeanUtils.copyProperties(signUpDTO, users, "password");

        // Hash the password and set it
        users.setPassword(hashPassword(signUpDTO.getPassword()));
        users.setToken(UUID.randomUUID());

        // Save the user
        userTableRepository.save(users);

        signUpResponseDTO.setFlag(true);
        signUpResponseDTO.setMessage("User Saved Successfully, TimeStamp: " + new Date());
      } catch (Exception e) {
        signUpResponseDTO.setFlag(false);
        signUpResponseDTO.setMessage("Failed to Save User, TimeStamp: " + new Date());
      }
    }

    return signUpResponseDTO;
  }


  private String hashPassword(String password) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(password.getBytes());
    byte[] digest = md.digest();
    String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
    return myHash;
  }

  @Override
  public SignInResponseDTO signIn(String userEmail, String password) throws Exception {
    SignInResponseDTO signInResponseDTO = new SignInResponseDTO();
    Users user = userTableRepository.findByEmail(userEmail);
    if (!Objects.nonNull(user)) {
      signInResponseDTO.setFlag(false);
      signInResponseDTO.setMessage("Try Sign-Up !");
    }
    try {
      if (!user.getPassword().equals(hashPassword(password))) {
        signInResponseDTO.setFlag(false);
        signInResponseDTO.setMessage("Wrong Password");
      }
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      throw new Exception(e.getMessage());
    }
    signInResponseDTO.setFlag(true);
    signInResponseDTO.setMessage("Sign In Success ! Welcome :" + " " + user.getFirstName());
    signInResponseDTO.setAuthenticationToken(user.getToken());
    return signInResponseDTO;
  }

  @Override
  public List<UserDetailsDTO> userDetails() {
    List<UserDetailsDTO> userDetailsDTOList = new ArrayList<>();
    List<Users> users = userTableRepository.findAll();

    for (Users user : users) {
      UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
      userDetailsDTO.setFirstName(user.getFirstName());
      userDetailsDTO.setLastName(user.getLastName());
      userDetailsDTO.setEmail(user.getEmail());
      userDetailsDTO.setPhoneNumber(user.getPhoneNumber());

      userDetailsDTOList.add(userDetailsDTO);
    }
    return userDetailsDTOList;
  }

}
