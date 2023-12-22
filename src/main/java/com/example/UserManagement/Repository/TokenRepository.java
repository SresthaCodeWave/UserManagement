package com.example.UserManagement.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.UserManagement.Entity.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

//  @Query("select t FROM Token t inner join User u" +
//      "WHERE t.user = u.userId " +
//      "AND (t.expired = false OR t.revoked = false)")
  List<Token> findAllByUserAndExpiredFalseAndRevokedFalse(Integer id);

  Optional<Token> findByToken(String token);
}
