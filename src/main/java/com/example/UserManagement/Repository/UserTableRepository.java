package com.example.UserManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.UserManagement.Entity.Users;

@Repository
public interface UserTableRepository extends JpaRepository<Users,Integer> {
  Users findByEmail(String email);
}
