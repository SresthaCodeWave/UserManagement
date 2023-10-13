package com.example.UserManagement.Dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponseDTO {
  private boolean flag;
  private String message;
  private UUID authenticationToken;
}
