package com.example.UserManagement.Service.ServiceImplementation;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.UserManagement.Dto.AuthResponse;
import com.example.UserManagement.Dto.AuthenticationRequest;
import com.example.UserManagement.Dto.AuthenticationResponse;
import com.example.UserManagement.Dto.SignInResponseDTO;
import com.example.UserManagement.Dto.SignUpDTO;
import com.example.UserManagement.Dto.SignUpResponseDTO;
import com.example.UserManagement.Dto.UserDetailsDTO;
import com.example.UserManagement.Entity.Token;
import com.example.UserManagement.Entity.TokenType;
import com.example.UserManagement.Entity.Users;
import com.example.UserManagement.Repository.TokenRepository;
import com.example.UserManagement.Repository.UserTableRepository;
import com.example.UserManagement.Service.JwtService;
import com.example.UserManagement.Service.UserService;

import javax.xml.bind.DatatypeConverter;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UserTableRepository userTableRepository;

  private  PasswordEncoder passwordEncoder;
  @Autowired
  JwtService jwtService;
  @Autowired
  TokenRepository tokenRepository;
  private  AuthenticationManager authenticationManager;

//  public UserServiceImpl(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
//    this.passwordEncoder = passwordEncoder;
//    this.authenticationManager = authenticationManager;
//  }

  @Override
  public AuthenticationResponse signUp(SignUpDTO signUpDTO) {
    SignUpResponseDTO signUpResponseDTO = new SignUpResponseDTO();

    Users userAvailable = userTableRepository.findByEmail(signUpDTO.getUsername());
    Users users = new Users();
    if (userAvailable != null) {
      signUpResponseDTO.setFlag(false);
      signUpResponseDTO.setMessage("User Already Present with this email : " + " " + signUpDTO.getUsername());
    } else {
      try {


        BeanUtils.copyProperties(signUpDTO, users, "password");

        // Hash the password and set it
        users.setPassword(hashPassword(signUpDTO.getPassword()));
        signUpResponseDTO.setFlag(true);
        signUpResponseDTO.setMessage("User Saved Successfully, TimeStamp: " + new Date());
      } catch (Exception e) {
        signUpResponseDTO.setFlag(false);
        signUpResponseDTO.setMessage("Failed to Save User, TimeStamp: " + new Date());
      }
    }
      var jwtToken = jwtService.generateToken(users);
      var refreshToken = jwtService.generateRefreshToken(users);
      userTableRepository.save(users);
      saveUserToken(users, jwtToken);


    return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
  }
  @Override
  public SignInResponseDTO authenticate(String email, String password) throws Exception{
//    authenticationManager.authenticate(
//        new UsernamePasswordAuthenticationToken(
//            email,
//            password
//        )
//    );
    SignInResponseDTO signInResponseDTO=new SignInResponseDTO();
    Users user = userTableRepository.findByEmail(email);
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
//    var user = userTableRepository.findByEmail(email);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    signInResponseDTO.setFlag(true);
    signInResponseDTO.setMessage("Sign In Success ! Welcome :" + " " + user.getFirstName());
    signInResponseDTO.setAccessToken(jwtToken);
    signInResponseDTO.setRefreshToken(refreshToken);
//    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return signInResponseDTO;
  }
  private void revokeAllUserTokens(Users user) {
    var validUserTokens = tokenRepository.findAllByUserAndExpiredFalseAndRevokedFalse(user.getUserId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  private void saveUserToken(Users user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
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
//    signInResponseDTO.setAuthenticationToken(user.getToken());
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

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return null;
  }
  //  @Override
//  public Users findByEmail(String email){
//    Users users = userTableRepository.findByEmail(email);
//    return users;
//  }
//
//  public AuthenticationResponse register(SignUpDTO request) {
//    User user= new User()
//        user.firstname(request.getFirstname())
//
//        .lastname(request.getLastname())
//        .email(request.getEmail())
//        .password(passwordEncoder.encode(request.getPassword()))
//        .role(request.getRole())
//        .build();
//    var savedUser = repository.save(user);
//
//
//  }
  @Override
  public AuthResponse verifyTokens( String token){
    AuthResponse authResponse = new AuthResponse();
    String email= jwtService.extractUsername(token);
    Users user= userTableRepository.findByEmail(email);
    if(user != null){
      authResponse.setId(user.getUserId());
      authResponse.set_id(user.getUserId());
      authResponse.setUsername(user.getEmail());
    }
 return authResponse;
  }

}
