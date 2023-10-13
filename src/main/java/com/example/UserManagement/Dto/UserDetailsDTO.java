package com.example.UserManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
}
