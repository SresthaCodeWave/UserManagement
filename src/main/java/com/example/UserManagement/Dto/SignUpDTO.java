package com.example.UserManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private String password;

}
